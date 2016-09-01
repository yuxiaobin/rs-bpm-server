package com.xb.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.xb.common.WFConstants;
import com.xb.persistent.WfTask;
import com.xb.service.IWfApiService;
import com.xb.service.IWfTaskAssignService;
import com.xb.service.IWfTaskService;
import com.xb.vo.TaskOptVO;

@Service
public class WfApiServiceImpl implements IWfApiService {
	
	private static final int STATUS_CODE_SUCC = WFConstants.ApiParams.STATUS_CODE_SUCC;
	private static final int STATUS_CODE_FAIL = WFConstants.ApiParams.STATUS_CODE_FAIL;
	private static final int STATUS_CODE_INVALID = WFConstants.ApiParams.STATUS_CODE_INVALID;
	
	private static final String RETURN_CODE = WFConstants.ApiParams.RETURN_CODE;
	private static final String RETURN_MSG = WFConstants.ApiParams.RETURN_MSG;
	
	@Autowired
	IWfTaskService taskService;
	@Autowired
	IWfTaskAssignService taskAssignService;
	
	@Override
	public boolean validateOperate(TaskOptVO optVO, JSONObject result) {
		WfTask task = taskService.selectById(optVO.getNextTaskId());
		if(task==null){
			result.put(RETURN_CODE, STATUS_CODE_INVALID);
			result.put(RETURN_MSG, "invalid taskId");
			return false;
		}
		String taskTypeCode = task.getTaskType();
		if(WFConstants.TaskTypes.S.getTypeCode().equals(taskTypeCode)
				|| WFConstants.TaskTypes.E.getTypeCode().equals(taskTypeCode)){
			//no need to validate assigner
		}else{
			List<String> assignedUserIdList = taskAssignService.getAssignedUsersByTaskId(optVO.getNextTaskId());
			if(assignedUserIdList!=null){
				String[] nextAssigners = optVO.getNextAssigners().split(",");
				boolean hasInvaldUser = false;
				StringBuilder invalidUsers = new StringBuilder();
				for(String userId : nextAssigners){
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
		String optCode = optVO.getOptCode();
		String comments = optVO.getComments();
		JSONObject txChoices = task.getTxChoicesJson();
		JSONObject txPrChoices = task.getTxPrChoicesJson();
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
			break;
		case WFConstants.OptTypes.REJECT:
			Boolean allowGoBack = null;
			Boolean signWhenGoBack = null;
			if(txChoices!=null){
				allowGoBack = txChoices.getBoolean("AllowGoBack");
				signWhenGoBack = txChoices.getBoolean("SignWhenGoBack");
			}
			if(allowGoBack==null || !allowGoBack){
				result.put(RETURN_CODE, STATUS_CODE_INVALID);
				result.put(RETURN_MSG, "不允许退回");
				return false;
			}
			if(signWhenGoBack!=null && signWhenGoBack && StringUtils.isEmpty(comments)){
				result.put(RETURN_CODE, STATUS_CODE_INVALID);
				result.put(RETURN_MSG, "退回时必须签署意见");
				return false;
			}
			break;
		case WFConstants.OptTypes.RECALL:
			Boolean allowReCall = null;
			Boolean signWhenReCall = null;
			if(txChoices!=null){
				allowReCall = txChoices.getBoolean("AllowReCall");
				signWhenReCall = txChoices.getBoolean("SignWhenReCall");
				if(allowReCall==null || !allowReCall){
					result.put(RETURN_CODE, STATUS_CODE_INVALID);
					result.put(RETURN_MSG, "Not Allow to Recall(optCode="+optCode+")");
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
				result.put(RETURN_CODE, STATUS_CODE_INVALID);
				result.put(RETURN_MSG, "Not Allow to Veto(optCode="+optCode+")");
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
