package com.xb.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.xb.common.WFConstants;
import com.xb.persistent.WfInstHist;
import com.xb.persistent.WfInstance;
import com.xb.persistent.WfTask;
import com.xb.service.IWfApiService;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfInstanceService;
import com.xb.service.IWfTaskAssignService;
import com.xb.service.IWfTaskService;
import com.xb.vo.TaskOptVO;

@Service
public class WfApiServiceImpl implements IWfApiService {
	
	private static final int STATUS_CODE_SUCC = WFConstants.ApiParams.STATUS_CODE_SUCC;
//	private static final int STATUS_CODE_FAIL = WFConstants.ApiParams.STATUS_CODE_FAIL;
	private static final int STATUS_CODE_INVALID = WFConstants.ApiParams.STATUS_CODE_INVALID;
	private static final int STATUS_CODE_OPT_NOT_ALLOW = WFConstants.ApiParams.STATUS_CODE_OPT_NOT_ALLOW;
	private static final String STATUS_MSG_OPT_NOT_ALLOW = WFConstants.ApiParams.STATUS_MSG_OPT_NOT_ALLOW;
	
	private static final String RETURN_CODE = WFConstants.ApiParams.RETURN_CODE;
	private static final String RETURN_MSG = WFConstants.ApiParams.RETURN_MSG;
	
	@Autowired
	IWfTaskService taskService;
	@Autowired
	IWfInstHistService histService;
	@Autowired
	IWfInstanceService instService;
	@Autowired
	IWfTaskAssignService taskAssignService;
	
	@Override
	public boolean validateOperate(TaskOptVO optVO, JSONObject result) {
		boolean isNextAssignerEmpty = true;
		String[] nextAssignerArray = null;
		String nextAssigners = optVO.getNextAssigners();
		if(StringUtils.isEmpty(nextAssigners)){
			isNextAssignerEmpty = true;
		}else{
			nextAssignerArray = nextAssigners.split(",");
			for(String str:nextAssignerArray){
				if(!StringUtils.isEmpty(str.trim())){
					isNextAssignerEmpty = false;
					break;
				}
			}
		}
		String optCode = optVO.getOptCode();
		if(WFConstants.OptTypes.LET_ME_DO.equals(optCode)){
			return true;
		}
		if(WFConstants.OptTypes.FORWARD.equals(optCode)){
			if(isNextAssignerEmpty){
				result.put(RETURN_CODE, STATUS_CODE_INVALID);
				result.put(RETURN_MSG, "提交必须指定处理人");
				return false;
			}else{
				return true;
			}
		}
		WfInstance instParm = new WfInstance();
		instParm.setInstNum(optVO.getInstNum());
		instParm.setRefMkid(optVO.getGnmkId());
		WfInstance inst = instService.selectOne(instParm);
		if(inst==null){
			result.put(RETURN_CODE, STATUS_CODE_INVALID);
			result.put(RETURN_MSG, "invalid parameter");
			return false;
		}
		optVO.setRsWfId(inst.getRsWfId());
		
		WfTask nextTask = null;
		if(WFConstants.OptTypes.RECALL.equals(optCode)){
//			task = taskService.selectById(optVO.getCurrTaskId());//recall: no need current taskId
			WfInstHist parm = new WfInstHist();
			parm.setOptUser(optVO.getCurrUserId());
			parm.setInstId(inst.getInstId());
			List<WfInstHist> hist4CurrUser = histService.selectList(parm, "OPT_SEQ DESC");
			if(hist4CurrUser==null || hist4CurrUser.isEmpty()){
				result.put(RETURN_CODE, STATUS_CODE_OPT_NOT_ALLOW);
				result.put(RETURN_MSG, STATUS_MSG_OPT_NOT_ALLOW);
				return false;
			}
			nextTask = taskService.selectById(hist4CurrUser.get(0).getTaskId());
		}else{
			nextTask = taskService.selectById(optVO.getNextTaskId());
		}
		
		if(nextTask==null){
			result.put(RETURN_CODE, STATUS_CODE_INVALID);
			result.put(RETURN_MSG, "invalid taskId:"+optVO.getNextTaskId());
			return false;
		}
		String taskTypeCode = nextTask.getTaskType();
		
		boolean isNextStartEndTask = false;
		
			
		if(WFConstants.TaskTypes.S.getTypeCode().equals(taskTypeCode)
				|| WFConstants.TaskTypes.E.getTypeCode().equals(taskTypeCode)){
			isNextStartEndTask = true;
		}else{
			if(!WFConstants.OptTypes.RECALL.equals(optCode)){
				List<String> assignedUserIdList = taskAssignService.getAssignedUsersByTaskId(optVO.getNextTaskId());
				if(assignedUserIdList!=null){
					boolean hasInvaldUser = false;
					StringBuilder invalidUsers = new StringBuilder();
					for(String userId : nextAssignerArray){
						if(!assignedUserIdList.contains(userId)){
							hasInvaldUser = true;
							invalidUsers.append(userId).append(",");
						}
					}
					if(hasInvaldUser){
						result.put(RETURN_CODE, STATUS_CODE_INVALID);
						result.put(RETURN_MSG, "invalid users:"+invalidUsers.toString());
						return false;
					}
				}
			}
		}
		WfTask currTask = taskService.getCurrentTaskByRefNum(optVO);
		if(currTask==null){
			result.put(RETURN_CODE, STATUS_CODE_OPT_NOT_ALLOW);
			result.put(RETURN_MSG, STATUS_MSG_OPT_NOT_ALLOW);
			return false;
		}
		JSONObject txChoices = currTask.getTxChoicesJson();
		JSONObject txPrChoices = currTask.getTxPrChoicesJson();
		String comments = optVO.getComments();
		switch (optCode) {
		case WFConstants.OptTypes.COMMIT:
			Boolean SignWhenGo = null;
			if(txPrChoices!=null){
				SignWhenGo = txPrChoices.getBoolean("SignWhenGo");
			}
			if(SignWhenGo!=null && SignWhenGo && StringUtils.isEmpty(comments)){
				result.put(RETURN_CODE, STATUS_CODE_INVALID);
				result.put(RETURN_MSG, "提交时必须签署意见");
				return false;
			}
			if(!isNextStartEndTask && isNextAssignerEmpty){
				result.put(RETURN_CODE, STATUS_CODE_INVALID);
				result.put(RETURN_MSG, "提交必须指定处理人");
				return false;
			}
			break;
		case WFConstants.OptTypes.REJECT:
			Boolean allowGoBack = null;
			Boolean signWhenGoBack = null;
			if(txChoices!=null){
				allowGoBack = txChoices.getBoolean("AllowGoBack");
				signWhenGoBack = txChoices.getBoolean("SignWhenGoBack");
			}
			if(allowGoBack==null || !allowGoBack){
				result.put(RETURN_CODE, STATUS_CODE_OPT_NOT_ALLOW);
				result.put(RETURN_MSG, STATUS_MSG_OPT_NOT_ALLOW);
				return false;
			}
			if(signWhenGoBack!=null && signWhenGoBack && StringUtils.isEmpty(comments)){
				result.put(RETURN_CODE, STATUS_CODE_INVALID);
				result.put(RETURN_MSG, "退回时必须签署意见");
				return false;
			}
			if(isNextAssignerEmpty){
				result.put(RETURN_CODE, STATUS_CODE_INVALID);
				result.put(RETURN_MSG, "退回必须指定处理人");
				return false;
			}
			break;
		/*case WFConstants.OptTypes.FORWARD://did this validation above*/
		case WFConstants.OptTypes.RECALL:
			txChoices = nextTask.getTxChoicesJson();
			Boolean allowReCall = null;
			Boolean signWhenReCall = null;
			if(txChoices!=null){
				allowReCall = txChoices.getBoolean("AllowReCall");
				signWhenReCall = txChoices.getBoolean("SignWhenReCall");
				if(allowReCall==null || !allowReCall){
					result.put(RETURN_CODE, STATUS_CODE_OPT_NOT_ALLOW);
					result.put(RETURN_MSG, STATUS_MSG_OPT_NOT_ALLOW);
					return false;
				}
				if(signWhenReCall!=null && signWhenReCall && StringUtils.isEmpty(comments)){
					result.put(RETURN_CODE, STATUS_CODE_INVALID);
					result.put(RETURN_MSG, "撤回时必须签署意见");
					return false;
				}
			}
			break;
		case WFConstants.OptTypes.VETO:
			Boolean allowVeto = null;
			Boolean signWhenVeto = null;
			if(txChoices!=null){
				allowVeto = txChoices.getBoolean("AllowVeto");
				signWhenVeto = txChoices.getBoolean("SignWhenVeto");
			}
			if(allowVeto==null || !allowVeto){
				result.put(RETURN_CODE, STATUS_CODE_OPT_NOT_ALLOW);
				result.put(RETURN_MSG, STATUS_MSG_OPT_NOT_ALLOW);
				return false;
			}
			if(signWhenVeto!=null && signWhenVeto && StringUtils.isEmpty(comments)){
				result.put(RETURN_CODE, STATUS_CODE_INVALID);
				result.put(RETURN_MSG, "否决时必须签署意见");
				return false;
			}
			break;
		default:
			break;
		}
		result.put(RETURN_CODE, STATUS_CODE_SUCC);
		return true;
	}

}
