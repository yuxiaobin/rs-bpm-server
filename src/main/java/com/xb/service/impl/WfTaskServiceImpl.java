package com.xb.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.xb.common.WFConstants;
import com.xb.persistent.RsModule;
import com.xb.persistent.RsWorkflow;
import com.xb.persistent.WfAwt;
import com.xb.persistent.WfDef;
import com.xb.persistent.WfInstHist;
import com.xb.persistent.WfInstance;
import com.xb.persistent.WfTask;
import com.xb.persistent.WfTaskAssign;
import com.xb.persistent.WfTaskConn;
import com.xb.persistent.mapper.WfTaskMapper;
import com.xb.service.IRsModuleService;
import com.xb.service.IRsWorkflowService;
import com.xb.service.IWfAwtService;
import com.xb.service.IWfDefService;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfInstanceService;
import com.xb.service.IWfTaskAssignService;
import com.xb.service.IWfTaskConnService;
import com.xb.service.IWfTaskService;
import com.xb.vo.TaskOptVO;
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
	@Autowired
	IWfAwtService awtService;
	
	/**
	 * 获取待办事宜
	 */
	public List<WfAwt> getTasksInbox(String userId){
		return awtService.getAwtByUserId(userId);
	}
	
	@Transactional
	public void startWF4Module(String moduleId, String rsWfId, String currUserId){
		RsModule rsModule = moduleService.selectById(moduleId);
		if (rsModule == null) {
			return;
		}
		WfDef wfDefParm = new WfDef();
		wfDefParm.setRsWfId(rsWfId);
		List<WfDef> wfDefList = wfDefService.selectList(wfDefParm, "version desc");
		if (wfDefList == null || wfDefList.isEmpty()) {
			return;
		}
		String wfId = wfDefList.get(0).getWfId();
		WfInstance instParm = new WfInstance();
		instParm.setRsWfId(rsWfId);
		List<WfInstance> instList4RsWfId = instService.selectList(instParm, "INST_NUM desc");
		int instNumCurr = 1;
		if(instList4RsWfId!=null && !instList4RsWfId.isEmpty()){
			instNumCurr = instList4RsWfId.get(0).getInstNum()+1;
		}
		
		WfInstance wfInst = new WfInstance();
		wfInst.setWfId(wfId);
		wfInst.setWfStatus(WFConstants.WFStatus.IN_PROCESS);
		wfInst.setRsWfId(rsWfId);
		wfInst.setInstNum(instNumCurr);
		wfInst.setRefMkid(moduleId);
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
		//待办事宜
		WfAwt awt = new WfAwt();
		awt.setAssignerId(currUserId);
		awt.setInstId(wfInst.getInstId());
		awt.setTaskIdCurr(startTask.getTaskId());
		awt.setHistIdPre(null);//发起时无hist记录
		awt.setAwtBegin(new Date());
		
		awtService.insert(awt);
	}

	@Transactional
	public void processTask(TaskOptVO optVO, String currUserId){
		WfAwt awt = awtService.getAwtByParam(optVO.getRsWfId(), optVO.getInstNum(), currUserId);
		if(awt==null){
			awt = awtService.getAwtByParam(optVO.getRsWfId(), optVO.getInstNum(), null);
		}
		if(awt==null){
			System.err.println("cannot get awt record for optVO="+optVO);
		}
		WfTask nextTask = this.selectById(optVO.getNextTaskId());
		optVO.setWfId(nextTask.getWfId());
		optVO.setPrevInstHistId(histService.createHistRecord(optVO, awt ,currUserId));
		WfTask task = this.selectById(awt.getTaskIdCurr());
		WfInstance wfInst = instService.selectById(awt.getInstId());
		if(WFConstants.TaskTypes.E.getTypeCode().equals(nextTask.getTaskType())){
			optVO.setNextEndTaskFlag(true);
		}else{
			optVO.setNextEndTaskFlag(false);
		}
		awtService.renewAwt(awt, task, wfInst, optVO, currUserId);
	}
	
	@Deprecated
	@Transactional
	public void processTask(String instId, String userId, String opt){
		WfInstance inst = instService.selectById(instId);
		if(inst==null || WFConstants.WFStatus.DONE.equals(inst.getWfStatus())){
			System.out.println("processTask===== current instance status is DONE,  process is rejected");
			return;
		}
		WfInstHist histParm = new WfInstHist();
		histParm.setInstId(instId);
		WfInstHist histCurr = histService.selectOne(histParm);
		if(histCurr==null){
			System.err.println("processTask====no IN_PROCESS history found for instId"+instId);
			return;
		}
		histService.updateById(histCurr);
		WfTask taskCurrNext = null;
		if(WFConstants.OptTypes.REJECT.equals(histCurr.getOptType())){
			taskCurrNext = baseMapper.selectById(getPrevTaskId(histCurr.getTaskId()));
		}else{
			taskCurrNext = this.selectById(getNextTaskId(histCurr.getTaskId()));
		}
		
		WfInstHist histNext = new WfInstHist();
		histNext.setInstId(histCurr.getInstId());
		histNext.setOptSeq(histCurr.getOptSeq()+1);
		histNext.setOptType(opt);//AP:Approve, RJ:Reject, RQ:Request
		histNext.setOptUser(userId);
//		histNext.setWfId(histCurr.getWfId());
		histNext.setTaskId(taskCurrNext.getTaskId());
		if(WFConstants.OptTypes.REJECT.equals(opt)){
			//for rejected task, should get who requested it
			histParm = new WfInstHist();
			histParm.setInstId(instId);
			histParm.setTaskId(getPrevTaskId(histNext.getTaskId()));
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
	
	private String getNextTaskId(String currTaskId){
		WfTaskConn connParm = new WfTaskConn();
		connParm.setSourceTaskId(currTaskId);
		WfTaskConn conn = taskConnService.selectOne(connParm);
		if(conn!=null){
			return conn.getTargetTaskId();
		}
		return null;
	}
		
	public WFDetailVO getWFStatus(String rsWfId, Integer instNum){
		WfInstance parmInst = new WfInstance();
		parmInst.setRsWfId(rsWfId);
		parmInst.setInstNum(instNum);
		List<WfInstance> instList = instService.selectList(parmInst);
		if(instList!=null && !instList.isEmpty()){
			return getWFStatus(instList.get(0).getInstId());
		}
		return null;
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
	
	public List<WfTask> getNextTasksByOptCode(TaskOptVO optVO){
		String optCode = optVO.getOptCode();
		WfAwt awt = awtService.getAwtByParam(optVO.getRsWfId(), optVO.getInstNum(), optVO.getCurrUserId());
		if(awt==null){
			awt = awtService.getAwtByParam(optVO.getRsWfId(), optVO.getInstNum(), null);
		}
		if(awt==null){
			return null;
		}
		List<WfTask> nextTaskList = null;
		if(WFConstants.OptTypes.COMMIT.equals(optCode)){
			nextTaskList = new ArrayList<WfTask>(1);
			nextTaskList.add(this.selectById(getNextTaskId(awt.getTaskIdCurr())));
			return nextTaskList;
		}
		else if(WFConstants.OptTypes.REJECT.equals(optCode)){
			nextTaskList = new ArrayList<WfTask>(1);
			nextTaskList.add(this.selectById(getPrevTaskId(awt.getTaskIdCurr())));
			//TODO: how to get previous tasks??
			return nextTaskList;
		}
		//TODO: other cases, if need to get next task list.
		return null;
	}
	
	
	public JSONObject getNextAssignersByOptCode(TaskOptVO optVO){
		WfAwt awt = awtService.getAwtByParam(optVO.getRsWfId(), optVO.getInstNum(), optVO.getCurrUserId());
		if(awt==null){
			awt = awtService.getAwtByParam(optVO.getRsWfId(), optVO.getInstNum(), null);
		}
		if(awt==null){
			return null;
		}
		return taskAssignerService.getUsersGroupsByTaskId(awt.getTaskIdCurr()).toJSONObject();
	}
}