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
import com.xb.common.BusinessException;
import com.xb.common.WFConstants;
import com.xb.persistent.WfAwt;
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
			WfAwt parm = new WfAwt();
			parm.setInstId(instId);
			parm.setCompleteFlag("N");
			int incompletedCount = this.selectCount(parm);
			if(incompletedCount==0){
				return true;//没有未完成的待办事宜
			}
			
			String csOptJson = currtask.getSignChoices();
			JSONObject csOpt = (JSONObject) JSONObject.parse(csOptJson);
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
	 * 撤回操作：优先取awt.optUserPrev=currUserId, 
	 * 		如果存在：
	 * 				判断awt.taskIdPrev是否为null：
	 * 						为null表示已经撤回过，无法再执行撤回操作<END>；
	 * 						不为null，判断awt.taskIdPrev是否与awt.currTaskId相同，
	 * 								同：表示要撤回的是forward操作
	 * 								不同：撤回操作将跨事物节点：新建awt(设置taskIdPrev=null),更新inst.optdUsersPrev:剔除currUserId(防止再转交后的再撤回)
	 * 		如果不存在：
	 * 				查看wf_inst.optdUsersPrev是否包含currUserId：
	 * 						如果没有：直接抛异常，无法撤回<END>;
	 * 						如果有：判断prevTask是否可撤回： 
	 * 								不可->抛异常<END>;
	 * 								可->撤回：新建awt(设置taskIdPrev=null), 更新inst.optdUsersPrev:剔除currUserId<END>; 
	 * 	
	 * @param instId
	 * @param currUserId
	 * @return
	 * @throws Exception 
	 */
	private boolean renewRecall(WfInstance inst, WfTask nextTask, String currUserId) throws BusinessException{
		String instId = inst.getInstId();
		synchronized (instId) {
			WfAwt awtParm = new WfAwt();
			awtParm.setInstId(instId);
			awtParm.setOptUsersPre(currUserId);
			WfAwt awt = this.selectOne(awtParm);
			if(awt!=null){
				String prevTaskId = awt.getTaskIdPre();
				if(prevTaskId==null){
					log.error("renewRecall(): prefTaskId is null for instId="+instId+", optUsersPre="+currUserId+", recall is not allowed");
					throw new BusinessException("RECALL-ERROR","Recall is not allowed");
				}
				if(awt.getTaskIdCurr().equals(awt.getTaskIdPre())){
					awt.setAssignerId(currUserId);
					awt.setTaskIdPre(null);
					awt.setOptUsersPre(currUserId);
					this.updateById(awt);
				}else{
					awtParm.setOptUsersPre(null);
					this.deleteSelective(awtParm);
					awt.setWfAwtId(null);;//新建一条awt
					awt.setTaskIdCurr(inst.getCurrTaskId());
					awt.setAssignerId(currUserId);
					awt.setOptUsersPre(currUserId);
					awt.setTaskIdPre(null);
					Date beginDate = new Date();
					awt.setAwtBegin(beginDate);
					awt.setAwtEnd(calculateDate(beginDate, nextTask.getTimeLimitTp(), nextTask.getTimeLimit()));
					awt.setAwtAlarm(nextTask.getAlarmTime()==null?null:calculateDate(beginDate, nextTask.getAlarmTimeTp(), nextTask.getAlarmTime()));
					removeUserFromOptUserPrev(inst, currUserId);
				}
			}
			else{
				String optdUserPre = inst.getOptUsersPre();
				if(optdUserPre==null || !optdUserPre.contains(currUserId)){
					log.error("renewRecall(): optUsersPre="+optdUserPre+", not contains currUserId="+currUserId+", recall is not allowed");
					throw new BusinessException("RECALL-ERROR","Recall is not allowed");
				}
				String prevTaskId = inst.getTaskIdPre();
				if(StringUtils.isEmpty(prevTaskId)){
					log.error("renewRecall(): prevTaskId is empty, recall is not allowed");
					throw new BusinessException("RECALL-ERROR","Recall is not allowed");
				}
				WfTask recallTask = taskService.selectById(prevTaskId);
				if(recallTask==null){
					log.error("renewRecall(): no wfTask record found for prevTaskId"+prevTaskId+", recall is not allowed");
					throw new BusinessException("RECALL-ERROR","Recall is not allowed");
				}
				JSONObject txChoices = recallTask.getTxChoicesJson();
				Boolean allowReCall = null;
				if(txChoices!=null){
					allowReCall = txChoices.getBoolean("AllowReCall");
				}
				if(allowReCall==null || !allowReCall){
					log.error("renewRecall(): preTask setting AllowReCall is null or false, recall is not allowed");
					throw new BusinessException("RECALL-ERROR","Recall is not allowed");
				}
				awt = new WfAwt();//新建一条awt
				awt.setTaskIdCurr(prevTaskId);
				awt.setAssignerId(currUserId);
				awt.setOptUsersPre(currUserId);
				awt.setTaskIdPre(null);
				Date beginDate = new Date();
				awt.setAwtBegin(beginDate);
				awt.setAwtEnd(calculateDate(beginDate, nextTask.getTimeLimitTp(), nextTask.getTimeLimit()));
				awt.setAwtAlarm(nextTask.getAlarmTime()==null?null:calculateDate(beginDate, nextTask.getAlarmTimeTp(), nextTask.getAlarmTime()));
				removeUserFromOptUserPrev(inst, currUserId);
			}
			return false;
		}
	}
	
	public void renewAwt(WfAwt prev, WfTask currtask, WfTask nextTask,  TaskOptVO optVO, String currUserId) throws BusinessException{
		if(nextTask!=null && WFConstants.TaskTypes.E.getTypeCode().equals(nextTask.getTaskType())){
			optVO.setNextEndTaskFlag(true);
		}else{
			optVO.setNextEndTaskFlag(false);
		}
		WfInstance wfInst = instService.selectById(prev.getInstId());
		
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
			needNextStep = renew4Forward(wfInst, optVO, currUserId);
			break;
		case WFConstants.OptTypes.LET_ME_DO:
			needNextStep = renew4LetMeDo(wfInst, currtask, currUserId);
			break;
		case WFConstants.OptTypes.RECALL:
			needNextStep = renewRecall(wfInst, nextTask, currUserId);
			break;
		default:
			break;
		}
		if(needNextStep){
			clearAwtUpdateInstGoNextStep(wfInst, optVO, nextTask, currUserId);
		}
	}
	
	private boolean renew4Forward(WfInstance wfInst, TaskOptVO optVO, String currUserId){
		WfAwt awtParm = new WfAwt();
		awtParm.setInstId(wfInst.getInstId());
		awtParm.setAssignerId(currUserId);
		WfAwt awt = this.selectOne(awtParm);
		if(awt==null){
			log.error("renew4Forward(): no awt found for instId="+wfInst.getInstId()+", currUserid="+currUserId);
			return false;
		}
		removeUserFromOptUserPrev(wfInst, currUserId);
		
		awt.setAssignerId(optVO.getNextAssigners());
		awt.setOptUsersPre(currUserId);
		awt.setTaskIdPre(awt.getTaskIdCurr());
		this.updateById(awt);
		return false;
	}
	
	private void removeUserFromOptUserPrev(WfInstance wfInst, String currUserId){
		String optdUsers = wfInst.getOptUsersPre();
		if(optdUsers!=null && optdUsers.contains(currUserId)){
			wfInst.setOptUsersPre(optdUsers.replace(currUserId+",", ""));
			instService.updateById(wfInst);
		}
	}
	
	private boolean renew4LetMeDo(WfInstance wfInst,WfTask currtask, String currUserId){
		WfAwt awtParm = new WfAwt();
		awtParm.setInstId(wfInst.getInstId());
		awtParm.setTaskIdCurr(currtask.getTaskId());
		List<WfAwt> awtList = this.selectList(awtParm);
		WfAwt currUserAwt = null;
		if(awtList==null || awtList.isEmpty()){
			log.error("renew4LetMeDo(): no awtList find for wfInstId="+wfInst.getInstId()+", currTaskId="+currtask.getTaskId()+", let me do ignored!");
			return false;
		}
		List<String> deleteIdList = new ArrayList<String>(awtList.size());
		for(WfAwt awt: awtList){
			if(!awt.getAssignerId().equals(currUserId)){
				deleteIdList.add(awt.getWfAwtId());
			}else{
				currUserAwt = awt;
			}
		}
		if(currUserAwt==null){
			log.debug("renew4LetMeDo(): currUserAwt is null for currUserId="+currUserId+", instId="+wfInst.getInstId());
			currUserAwt = awtList.get(0);
			currUserAwt.setAssignerId(currUserId);
			currUserAwt.setWfAwtId(null);
			this.insert(currUserAwt);
		}
		this.deleteBatchIds(deleteIdList);
		return false;
	}
	
	/**
	 * Clear Current Awt,  update Instance. Go next step: insert new Awt if needed.
	 * @param wfInst
	 * @param optVO
	 * @param nextTask
	 */
	private void clearAwtUpdateInstGoNextStep(WfInstance wfInst, TaskOptVO optVO, WfTask nextTask, String currUserId){
		String optdUsers = wfInst.getOptUsersPre();
		if(StringUtils.isEmpty(optdUsers)){
			wfInst.setOptUsersPre(currUserId+",");
		}else{
			if(!optdUsers.contains(currUserId)){
				wfInst.setOptUsersPre(optdUsers+currUserId+",");
			}
		}
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
					/**
					 * 当任务流转到下一个节点，下一个节点的awt数据：设置optUsersPre&taskIdPre
					 */
					awt.setOptUsersPre(wfInst.getOptUsersPre());
					awt.setTaskIdPre(wfInst.getCurrTaskId());
					awt.setAssignerId(assigner);
					awt.setAwtBegin(beginDate);
					awt.setAwtEnd(limitDate);
					awt.setAwtAlarm(alarmDate);
					awt.setInstId(instId);
					awt.setTaskIdCurr(optVO.getNextTaskId());
					this.insert(awt);
				}
			}
		}
		/**
		 * 当事务流转到下一个节点时，重置currTaskid&optdUsers
		 * 为上一步操作人（可能是多个）和上一个任务节点
		 */
		
		wfInst.setCurrAssigners(nextAssigners);
		wfInst.setTaskIdPre(wfInst.getCurrTaskId());
		wfInst.setCurrTaskId(nextTask.getTaskId());
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
		String optdUsers = wfInst.getOptUsersPre();
		if(optdUsers==null){
			optdUsers = currUserId+",";
		}else{
			if(!optdUsers.contains(currUserId)){
				optdUsers += currUserId+",";
			}
		}
		/**
		 * 事务未流转，更新该task的处理人
		 */
		wfInst.setOptUsersPre(optdUsers);
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

	@Override
	public List<WfAwt> getAwt4Recall(String rsWfId, int instNum, String currRecallUser) {
		Map<String,Object> parmMap = new HashMap<String,Object>();
		parmMap.put("rsWfId", rsWfId);
		parmMap.put("instNum", instNum);
		parmMap.put("currRecallUser", currRecallUser);
		return baseMapper.getAwt4Recall(parmMap);
	}
}