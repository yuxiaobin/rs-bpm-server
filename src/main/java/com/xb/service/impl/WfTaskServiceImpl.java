package com.xb.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.xb.common.WFConstants;
import com.xb.persistent.RsModule;
import com.xb.persistent.RsWorkflow;
import com.xb.persistent.WfDef;
import com.xb.persistent.WfInstHist;
import com.xb.persistent.WfInstance;
import com.xb.persistent.WfTask;
import com.xb.persistent.WfTaskAssign;
import com.xb.persistent.WfTaskConn;
import com.xb.persistent.mapper.WfTaskMapper;
import com.xb.service.IRsModuleService;
import com.xb.service.IRsWorkflowService;
import com.xb.service.IWfDefService;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfInstanceService;
import com.xb.service.IWfTaskAssignService;
import com.xb.service.IWfTaskConnService;
import com.xb.service.IWfTaskService;
import com.xb.vo.TaskVO;
import com.xb.vo.WFDetailVO;

/**
 *
 * WfTask 表数据服务层接口实现类
 *
 */
@Service
public class WfTaskServiceImpl extends CommonServiceImpl<WfTaskMapper, WfTask> implements IWfTaskService {
	
	private static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

	@Autowired
	IWfInstHistService histService;
	@Autowired
	IWfTaskConnService taskConnService;
	@Autowired
	IWfInstanceService instService;
	@Autowired
	IRsModuleService moduleService;
	@Autowired
	IWfDefService wfDefService;
	@Autowired
	IRsWorkflowService rsWfService;
	@Autowired
	IWfTaskAssignService taskAssignerService;
	
	public List<TaskVO> getTasksInbox(String userId){
		String parmUserId = ","+userId+",";
		return baseMapper.getTasksInbox(userId,parmUserId);
	}
	
	@Transactional
	public void startWF4Module(String moduleId, String currUserId){
		RsModule rsModule = moduleService.selectById(moduleId);
		if (rsModule == null) {
			return;
		}
		if (StringUtils.isEmpty(rsModule.getRsWfId())) {
			return;
		}
		WfDef wfDefParm = new WfDef();
		wfDefParm.setRsWfId(rsModule.getRsWfId());
		List<WfDef> wfDefList = wfDefService.selectList(wfDefParm, "version desc");
		if (wfDefList == null || wfDefList.isEmpty()) {
			return;
		}
		String wfId = wfDefList.get(0).getWfId();
		WfInstance wfInst = new WfInstance();
		wfInst.setWfId(wfId);
		wfInst.setWfStatus(WFConstants.WFStatus.IN_PROCESS);
		instService.insert(wfInst);
		
		WfTask taskParm = new WfTask();
		taskParm.setWfId(wfId);
		List<WfTask> taskList = this.selectList(taskParm);
		WfTask startTask = null;
		for(WfTask task:taskList){
			if(WFConstants.TaskTypes.S.getTypeCode().equals(task.getTaskType())){
				startTask = task;
				break;
			}
		}
		WfInstHist hist = new WfInstHist();
		hist.setInstId(wfInst.getInstId());
		hist.setWfId(wfId);
		hist.setTaskId(startTask.getTaskId());
		hist.setOptSeq(1);
		hist.setOptUser(currUserId);
		hist.setOptType(WFConstants.OptTypes.REQUEST);//R:Request
		hist.setSTATUS(WFConstants.WFStatus.IN_PROCESS);
		histService.insert(hist);
		
		prepareNextTask(hist);
	}

	@Transactional
	public void processTask(String instId, String userId, String opt){
		WfInstance inst = instService.selectById(instId);
		if(inst==null || WFConstants.WFStatus.DONE.equals(inst.getWfStatus())){
			System.out.println("processTask===== current instance status is DONE,  process is rejected");
			return;
		}
		WfInstHist histParm = new WfInstHist();
		histParm.setInstId(instId);
		histParm.setSTATUS(WFConstants.WFStatus.IN_PROCESS);
		WfInstHist histCurr = histService.selectOne(histParm);
		if(histCurr==null){
			System.err.println("processTask====no IN_PROCESS history found for instId"+instId);
			return;
		}
		histCurr.setSTATUS(WFConstants.WFStatus.DONE);
		histService.updateById(histCurr);
		WfTask taskCurrNext = null;
		if(WFConstants.OptTypes.REJECT.equals(histCurr.getOptType())){
			taskCurrNext = baseMapper.selectById(getPrevTaskId(histCurr.getTaskId()));
		}else{
			taskCurrNext = this.selectById(getNextTask(histCurr.getTaskId()));
		}
		
		WfInstHist histNext = new WfInstHist();
		histNext.setInstId(histCurr.getInstId());
		histNext.setOptSeq(histCurr.getOptSeq()+1);
		histNext.setOptType(opt);//AP:Approve, RJ:Reject, RQ:Request
		histNext.setOptUser(userId);
		histNext.setSTATUS(WFConstants.WFStatus.IN_PROCESS);
		histNext.setWfId(histCurr.getWfId());
		histNext.setTaskId(taskCurrNext.getTaskId());
		if(WFConstants.OptTypes.REJECT.equals(opt)){
			//for rejected task, should get who requested it
			histParm = new WfInstHist();
			histParm.setInstId(instId);
			histParm.setTaskId(getPrevTaskId(histNext.getTaskId()));
			histParm.setSTATUS(WFConstants.WFStatus.DONE);
			List<WfInstHist> prevSameTaskHistList = histService.selectList(histParm, "OPT_SEQ desc");
			if(prevSameTaskHistList!=null && !prevSameTaskHistList.isEmpty()){
				histNext.setNextAssigner(prevSameTaskHistList.get(0).getOptUser());
			}else{
				histNext.setNextAssigner(histCurr.getOptUser());
			}
		}else{
//			histNext.setNextAssigner(taskCurrNext.getAssignUsers());//TODO: set assngiers
		}
		histService.insert(histNext);
		prepareNextTask(histNext);
	}
	
	private String getPrevTaskId(String currTaskId){
		WfTaskConn connParm = new WfTaskConn();
		connParm.setTargetTaskId(currTaskId);
		WfTaskConn conn = taskConnService.selectOne(connParm);
		if(conn!=null){
			return conn.getSourceTaskId();
		}
		return null;
	}
	
	private String getNextTask(String currTaskId){
		WfTaskConn connParm = new WfTaskConn();
		connParm.setSourceTaskId(currTaskId);
		WfTaskConn conn = taskConnService.selectOne(connParm);
		if(conn!=null){
			return conn.getTargetTaskId();
		}
		return null;
	}
		
	
	@Transactional
	public void prepareNextTask(final WfInstHist currHist){
		/*
		 * 条件节点的判断先不做//TODO:
		 */
		WfInstance inst = instService.selectById(currHist.getInstId());
		if(WFConstants.OptTypes.REJECT.equals(currHist.getOptType())){
			WfTask taskPrev = this.selectById(getPrevTaskId(currHist.getTaskId()));
			inst.setCurrTaskId(taskPrev.getTaskId());
			instService.updateById(inst);
		}
		else{
			WfTask taskNext = this.selectById(getNextTask(currHist.getTaskId()));
			if(taskNext==null){
				System.out.println("prepareNextTask====nextTask is "+taskNext);
				return;
			}
			inst.setCurrTaskId(taskNext.getTaskId());
			if(WFConstants.TaskTypes.E.getTypeCode().equals(taskNext.getTaskType())){
				currHist.setSTATUS(WFConstants.WFStatus.DONE);
				histService.updateById(currHist);
				inst.setWfStatus(WFConstants.WFStatus.DONE);
				System.out.println("update instance to Done status");
			}else{
//				currHist.setNextAssigner(taskNext.getAssignUsers());//TODO: set assngiers
				histService.updateById(currHist);
			}
			instService.updateById(inst);
		}
		
	}
	
	/**
	 * 获取当前工作流状态
	 * @param histId
	 * @return
	 */
	public WFDetailVO getWFStatus(String instId){
		WFDetailVO result = new WFDetailVO();
		WfInstance instance = instService.selectById(instId);
		WfDef wfDef = wfDefService.selectById(instance.getWfId());
		result.setWfDef(wfDef);
		RsWorkflow rsWf = rsWfService.selectById(wfDef.getRsWfId());
		
		if (rsWf == null) {
			return result;
		}
		result.setRsWF(rsWf);
		RsModule modParm = new RsModule();
		modParm.setRsWfId(rsWf.getRsWfId());
		RsModule rsModule = moduleService.selectOne(modParm);
		if(rsModule==null){
			System.out.println("getWFStatus===rsModule is null for rsWfId="+rsWf.getRsWfId());
			return result;
		}
		result.setModule(rsModule);
		List<WfTask> taskList = baseMapper.getTaskListWithStatus(instance.getInstId());
		
		if(WFConstants.WFStatus.DONE.equals(instance.getWfStatus())){
			for(WfTask task:taskList){
				task.setProcessedFlag("Y");
			}
		}
		result.setTasks(taskList);
		
		WfTaskConn connParm = new WfTaskConn();
		connParm.setWfId(wfDef.getWfId());
		result.setConns(taskConnService.selectList(connParm));
		return result;
	}
	
	public void batchCreateTasksWithAssigners(List<WfTask> taskList){
		List<WfTaskAssign> assignerAllList = new LinkedList<WfTaskAssign>();
		for(WfTask task:taskList){
			if(task.getAssignerList()!=null){
				assignerAllList.addAll(task.getAssignerList());
			}
		}
		this.insertBatch(taskList);
		if(assignerAllList!=null && !assignerAllList.isEmpty()){
			taskAssignerService.insertBatch(assignerAllList);
		}
	}
	
	public List<WfTask> selectTasksWithAssigners(String wfId){
		WfTask parm = new WfTask();
		parm.setWfId(wfId);
		List<WfTask> taskList = this.selectList(parm);
		if(taskList!=null){
			for(WfTask task:taskList){
				task.setAssignerList(taskAssignerService.selectTaskAssignerListWithName(task.getTaskId()));
			}
		}
		return taskList;
	}
	
}