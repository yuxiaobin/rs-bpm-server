package com.xb.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.xb.common.WFConstants;
import com.xb.persistent.WfAwt;
import com.xb.persistent.WfInstHist;
import com.xb.persistent.WfInstance;
import com.xb.persistent.WfTask;
import com.xb.persistent.mapper.WfAwtMapper;
import com.xb.service.IWfAwtService;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfInstanceService;
import com.xb.service.IWfTaskService;
import com.xb.vo.TaskOptVO;

/**
 *
 * WfAwt 表数据服务层接口实现类
 *
 */
@Service
public class WfAwtServiceImpl extends CommonServiceImpl<WfAwtMapper, WfAwt> implements IWfAwtService {
	
	private static Logger log = Logger.getLogger(WfAwtServiceImpl.class);
	
	@Autowired
	IWfInstanceService instService;
	@Autowired
	IWfInstHistService histService;
	@Autowired
	IWfTaskService taskService;

	/**
	 * 根据登录用户，获取待办事宜。
	 */
	public List<WfAwt> getAwtByUserId(String userId){
		
		return baseMapper.getAwtByUserId(userId);
	}
	
	public WfAwt getAwtByParam(String rsWfId, int instNum, String currUserId){
		Map<String,Object> parmMap = new HashMap<String,Object>();
		parmMap.put("rsWfId", rsWfId);
		parmMap.put("instNum", instNum);
		parmMap.put("currUserId", currUserId);
		List<WfAwt> awtList = baseMapper.getAwtByParam(parmMap);
		if(awtList!=null && !awtList.isEmpty()){
			for(WfAwt awt:awtList){
				String completeFlag = awt.getCompleteFlag();
				if(completeFlag==null || completeFlag.equals("N")){
					return awt;
				}
			}
			return awtList.get(0);
		}
		return null;
	}
	
	private boolean renew4Commit(WfAwt prev, WfTask currtask, WfInstance wfInst, String currUserId){
		String instId = prev.getInstId();
		if(WFConstants.TxCodes.COUNTERSIGN.equals(currtask.getTxType())){
			prev.setCompleteFlag("Y");
			this.updateById(prev);
			String csOptJson = currtask.getSignChoices();
			JSONObject csOpt = (JSONObject) JSONObject.parse(csOptJson);
			WfAwt parm = new WfAwt();
			parm.setInstId(instId);
			parm.setCompleteFlag("Y");
			int completedCount = this.selectCount(parm);
			if(Boolean.TRUE.toString().equals(csOpt.getString("AllHandledThenGo"))){
				parm.setCompleteFlag(null);
				int allCount = this.selectCount(parm);
				if(allCount!=completedCount){
					updateCurrAssigners4CS(wfInst, currUserId);//not finished, return
					return false;
				}
			}
			else{
				if(Boolean.TRUE.toString().equals(csOpt.getString("PartHandledThenGo"))){
					int AtLeastHandled = csOpt.getIntValue("AtLeastHandled");
					if(AtLeastHandled==0){
						AtLeastHandled = 1;
					}
					if(completedCount<AtLeastHandled){
						updateCurrAssigners4CS(wfInst, currUserId);
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public boolean renewReject(){
		return true;
	}
	
	/**
	 * 
	 * @param instId
	 * @param currUserId
	 * @return
	 */
	private boolean renewRecall(String instId, String currUserId){
		synchronized (instId) {
			WfInstHist histParm = new WfInstHist();
			histParm.setInstId(instId);
			List<WfInstHist> histlist = histService.selectList(histParm, "OPT_SEQ desc");
			if(histlist.size()<2){
				log.warn("renewRecall(): histlist size<2, no action for recall");
			}else{
				WfInstHist prevDone = histlist.get(0);
				WfInstHist prevTwo = null;
				for(WfInstHist hist:histlist){
					if(currUserId.equals(hist.getOptUser())){
						prevTwo = hist;
						break;
					}
				}
				if(prevTwo == null){
					prevTwo = histlist.get(1);
				}
				String taskId = prevDone.getTaskId();
				String histIdPre = prevTwo.getHistId();
				Date taskBegin = prevDone.getTaskBegin();
				Date taskEnd = prevDone.getTaskEnd();
				WfTask prevTask = taskService.selectById(prevDone.getTaskId()); 
				Date alarmDate = calculateDate(prevDone.getTaskBegin(), prevTask.getAlarmTimeTp(), prevTask.getAlarmTime());
				String[] taskOwnerArray = prevTwo.getTaskOwner().split(",");
				List<WfAwt> insertlist = new ArrayList<WfAwt>(taskOwnerArray.length);
				WfAwt awt = null;
				for(String owner:taskOwnerArray){
					if(!StringUtils.isEmpty(owner)){
						awt = new WfAwt();
						awt.setTaskIdCurr(taskId);
						awt.setHistIdPre(histIdPre);
						awt.setInstId(instId);
						awt.setAwtBegin(taskBegin);
						awt.setAwtEnd(taskEnd);
						awt.setAwtAlarm(alarmDate);
						awt.setAssignerId(owner);
						insertlist.add(awt);
					}
				}
				this.insertBatch(insertlist);
			}
			return false;
		}
		
	}
	
	public void renewAwt(WfAwt prev, WfTask currtask, WfTask nextTask,  TaskOptVO optVO, String currUserId){
		if(nextTask!=null && WFConstants.TaskTypes.E.getTypeCode().equals(nextTask.getTaskType())){
			optVO.setNextEndTaskFlag(true);
		}else{
			optVO.setNextEndTaskFlag(false);
		}
		String instId = prev.getInstId();
		WfInstance wfInst = instService.selectById(instId);
		String optCode = optVO.getOptCode();
		boolean needNextStep = false;
		switch (optCode) {
		case WFConstants.OptTypes.COMMIT:
			needNextStep = renew4Commit(prev, currtask, wfInst, currUserId);
			break;
		case WFConstants.OptTypes.REJECT:
			needNextStep = renewReject();
			break;
		case WFConstants.OptTypes.FORWARD:
			needNextStep = true;
			nextTask = currtask;
			optVO.setNextTaskId(nextTask.getTaskId());
			break;
		case WFConstants.OptTypes.LET_ME_DO:
			nextTask = currtask;
			optVO.setNextTaskId(nextTask.getTaskId());
			optVO.setNextAssigners(currUserId);
			needNextStep = true;
			break;
		case WFConstants.OptTypes.RECALL:
			needNextStep = renewRecall(instId, currUserId);
			break;
		default:
			break;
		}
		if(needNextStep){
			clearAwtUpdateInstGoNextStep(wfInst, optVO, nextTask);
		}
	}
	
	
	/**
	 * Clear Current Awt,  update Instance. Go next step: insert new Awt if needed.
	 * @param wfInst
	 * @param optVO
	 * @param nextTask
	 */
	private void clearAwtUpdateInstGoNextStep(WfInstance wfInst, TaskOptVO optVO, WfTask nextTask){
		String instId = wfInst.getInstId();
		WfAwt parm = new WfAwt();
		parm.setInstId(instId);
		parm.setCompleteFlag(null);
		this.deleteSelective(parm);
		//create new awt(s) with next taskId
		String nextAssigners = optVO.getNextAssigners();
		if(nextAssigners!=null && !optVO.isNextEndTaskFlag()){
			String[] nextAssignersArr = nextAssigners.split(",");
			WfAwt awt = null;
			Date beginDate = new Date();
			Date limitDate = calculateDate(beginDate, nextTask.getTimeLimitTp(), nextTask.getTimeLimit());
			Date alarmDate = nextTask.getAlarmTime()==null?null:calculateDate(beginDate, nextTask.getAlarmTimeTp(), nextTask.getAlarmTime());
			for(String assigner : nextAssignersArr){
				if(!StringUtils.isEmpty(assigner)){
					awt = new WfAwt();
					awt.setAssignerId(assigner);
					awt.setAwtBegin(beginDate);
					awt.setAwtEnd(limitDate);
					awt.setAwtAlarm(alarmDate);
					awt.setHistIdPre(optVO.getPrevInstHistId());
					awt.setInstId(instId);
					awt.setTaskIdCurr(optVO.getNextTaskId());
					this.insert(awt);
				}
			}
		}
		wfInst.setCurrAssigners(nextAssigners);
		if(optVO.isNextEndTaskFlag()){
			wfInst.setWfStatus(WFConstants.WFStatus.DONE);
		}
		instService.updateById(wfInst);
	}
	
	private Date calculateDate(Date beginDate, String timeType, Integer amount){
		Date limitDate = null;
		timeType = timeType==null?"":timeType;
		amount = amount==null?0:amount;
		switch (timeType) {
		case "M":
			limitDate = DateUtils.addMinutes(beginDate, amount);
			break;
		case "H":
			limitDate = DateUtils.addHours(beginDate, amount);
			break;
		case "D":
			limitDate = DateUtils.addDays(beginDate, amount);
			break;
		default:
//			limitDate = beginDate;
			break;
		}
		return limitDate;
	}
	
	private void updateCurrAssigners4CS(WfInstance wfInst, String currUserId){
		String currAssigners4Inst = wfInst.getCurrAssigners();
		if(currAssigners4Inst!=null){
			if(currAssigners4Inst.contains(currUserId+",")){
				wfInst.setCurrAssigners(currAssigners4Inst.replace(currUserId+",", ""));
				instService.updateById(wfInst );//更新当前处理人
				wfInst.setCurrAssigners(currAssigners4Inst);
			}
		}
	}

	@Override
	public List<WfAwt> getAwtListByInstId(String instId) {
		Map<String,Object> parmMap = new HashMap<String,Object>();
		parmMap.put("instId", instId);
		return baseMapper.getAwtByParam(parmMap);
	}
}