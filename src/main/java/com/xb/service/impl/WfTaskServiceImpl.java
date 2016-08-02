package com.xb.service.impl;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.xb.common.WFConstants;
import com.xb.persistent.RsModule;
import com.xb.persistent.WfDef;
import com.xb.persistent.WfInstHist;
import com.xb.persistent.WfInstance;
import com.xb.persistent.WfTask;
import com.xb.persistent.WfTaskConn;
import com.xb.persistent.mapper.WfTaskMapper;
import com.xb.service.IRsModuleService;
import com.xb.service.IWfDefService;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfInstanceService;
import com.xb.service.IWfTaskConnService;
import com.xb.service.IWfTaskService;
import com.xb.vo.TaskVO;

/**
 *
 * WfTask 表数据服务层接口实现类
 *
 */
@Service
public class WfTaskServiceImpl extends SuperServiceImpl<WfTaskMapper, WfTask> implements IWfTaskService {
	
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
	
	public List<TaskVO> getTasksInbox(String userId){
		return baseMapper.getTasksInbox(userId);
	}
	
	@Transactional
	public void startWF4Module(String moduleId, String currUserId){
		RsModule modParm = new RsModule();
		modParm.setModId(moduleId);
		RsModule rsModule = moduleService.selectOne(modParm);
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
		WfInstHist histParm = new WfInstHist();
		histParm.setHistId(histId);
		WfInstHist histCurr = histService.selectOne(histParm);
		histCurr.setSTATUS(WFConstants.WFStatus.DONE);
		histService.updateById(histCurr);
		
		WfTaskConn connParm = new WfTaskConn();
		connParm.setSourceTaskId(histCurr.getTaskId());
		//for user task source, should only have one connection
		WfTaskConn conn = taskConnService.selectOne(connParm);
		if(conn==null){
			System.err.println("no conn record for taksId="+histCurr.getTaskId()+", workflow over");
			return;
		}
		String targetTaskId = conn.getTargetTaskId();
		WfTask taskParm = new WfTask();
		taskParm.setTaskId(targetTaskId);
		WfTask taskNext = this.selectOne(taskParm);
		
		WfInstHist histNext = new WfInstHist();
		histNext.setInstId(histCurr.getInstId());
		histNext.setOptSeq(histCurr.getOptSeq()+1);
		histNext.setOptType(opt);//AP:Approve, RJ:Reject
		histNext.setOptUser(userId);
		histNext.setSTATUS(WFConstants.WFStatus.IN_PROCESS);
		histNext.setWfId(histCurr.getWfId());
		if(WFConstants.OptTypes.REJECT.equals(opt)){
			histNext.setTaskId(conn.getSourceTaskId());
			histNext.setNextAssigner(histCurr.getOptUser());
		}else{
			histNext.setTaskId(taskNext.getTaskId());
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
		WfTaskConn connParm = new WfTaskConn();
		connParm.setSourceTaskId(currHist.getTaskId());
		//for user task source, should only have one connection
		WfTaskConn conn = taskConnService.selectOne(connParm);
		if(conn==null){
			System.err.println("no conn record for taksId="+currHist.getTaskId()+", workflow over");
			return;
		}
		WfTask taskParm = new WfTask();
		taskParm.setTaskId(conn.getTargetTaskId());
		WfTask taskNext = this.selectOne(taskParm);
		if(taskNext==null){
			System.out.println("prepareNextTask====nextTask is "+taskNext);
			return;
		}
		
		WfInstance instParm = new WfInstance();
		instParm.setInstId(currHist.getInstId());
		WfInstance inst = instService.selectOne(instParm);
		inst.setCurrTaskId(taskNext.getTaskId());
		
		if(WFConstants.OptTypes.REJECT.equals(currHist.getOptType())){
			System.out.println("task is rejected, auto route to requester");
			instService.updateById(inst);
			return;
		}
		if(WFConstants.TaskTypes.E.getTypeCode().equals(taskNext.getTaskType())){
			currHist.setSTATUS(WFConstants.WFStatus.DONE);
			histService.updateById(currHist);
			inst.setWfStatus(WFConstants.WFStatus.DONE);
			instService.updateById(inst);
			System.out.println("update instance to Done status");
			return;
		}
		
	}
	
	//TODO:
	public void checkNextStepJob(final WfInstance inst, final WfInstHist hist){
		/*final IWfTaskService f_taskService = this;
		final IWfTaskConnService f_connService = taskConnService;
		executor.execute(new Runnable() {
			@Override
			public void run() {
				WfTaskConn conn = new WfTaskConn();
				conn.setSourceTaskId(hist.getTaskId());
				f_connService.selectList(conn);
//				f_connService.selectOne(entity)
			}
		});*/
	}
}