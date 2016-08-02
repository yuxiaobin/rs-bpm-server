package com.xb.service.impl;

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
import com.xb.persistent.WfTaskConn;
import com.xb.persistent.mapper.WfTaskMapper;
import com.xb.service.IRsModuleService;
import com.xb.service.IRsWorkflowService;
import com.xb.service.IWfDefService;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfInstanceService;
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
	
	public List<TaskVO> getTasksInbox(String userId){
		return baseMapper.getTasksInbox(userId);
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
	public void processTask(String histId, String userId, String opt){
		WfInstHist histCurr = histService.selectById(histId);
		histCurr.setSTATUS(WFConstants.WFStatus.DONE);
		histService.updateById(histCurr);
		String nextTaskId = "";
		WfTask taskNext = null;
		if(WFConstants.OptTypes.REJECT.equals(histCurr.getOptType())){
			WfTaskConn connParm = new WfTaskConn();
			connParm.setTargetTaskId(histCurr.getTaskId());
			//for user task source, should only have one connection
			WfTaskConn conn = taskConnService.selectOne(connParm);
			nextTaskId = conn.getSourceTaskId();
			taskNext = baseMapper.selectById(nextTaskId);
		}else{
			WfTaskConn connParm = new WfTaskConn();
			connParm.setSourceTaskId(histCurr.getTaskId());
			//for user task source, should only have one connection
			WfTaskConn conn = taskConnService.selectOne(connParm);
			if(conn==null){
				System.err.println("no conn record for taksId="+histCurr.getTaskId()+", workflow over");
				return;
			}
			nextTaskId = conn.getTargetTaskId();
			taskNext = this.selectById(nextTaskId);
		}
		
		WfInstHist histNext = new WfInstHist();
		histNext.setInstId(histCurr.getInstId());
		histNext.setOptSeq(histCurr.getOptSeq()+1);
		histNext.setOptType(opt);//AP:Approve, RJ:Reject, RQ:Request
		histNext.setOptUser(userId);
		histNext.setSTATUS(WFConstants.WFStatus.IN_PROCESS);
		histNext.setWfId(histCurr.getWfId());
		histNext.setTaskId(taskNext.getTaskId());
		if(WFConstants.OptTypes.REJECT.equals(opt)){
			histNext.setNextAssigner(histCurr.getOptUser());
		}else{
			histNext.setNextAssigner(taskNext.getAssignUsers());
		}
		histService.insert(histNext);
		prepareNextTask(histNext);
	}
	
	@Transactional
	public void prepareNextTask(final WfInstHist currHist){
		/*
		 * 条件节点的判断先不做//TODO:
		 */
		WfInstance inst = instService.selectById(currHist.getInstId());
		if(WFConstants.OptTypes.REJECT.equals(currHist.getOptType())){
			WfTaskConn connParm = new WfTaskConn();
			connParm.setTargetTaskId(currHist.getTaskId());
			//for user task source, should only have one connection
			WfTaskConn conn = taskConnService.selectOne(connParm);
			WfTask taskPrev = this.selectById(conn.getSourceTaskId());
			inst.setCurrTaskId(taskPrev.getTaskId());
			instService.updateById(inst);
		}
		else{
			WfTaskConn connParm = new WfTaskConn();
			connParm.setSourceTaskId(currHist.getTaskId());
			//for user task source, should only have one connection
			WfTaskConn conn = taskConnService.selectOne(connParm);
			if(conn==null){
				System.err.println("no conn record for taksId="+currHist.getTaskId()+", workflow over");
				return;
			}
			WfTask taskNext = this.selectById(conn.getTargetTaskId());
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
			}
			instService.updateById(inst);
		}
		
	}
	
	/**
	 * 获取当前工作流状态
	 * @param histId
	 * @return
	 */
	public WFDetailVO getWFStatus(String histId){
		WFDetailVO result = new WFDetailVO();
		WfInstHist hist = histService.selectById(histId);
		WfDef wfDef = wfDefService.selectById(hist.getWfId());
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
		List<WfTask> taskList = baseMapper.getTaskListWithStatus(hist.getInstId());
		WfInstance instance = instService.selectById(hist.getInstId());
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
}