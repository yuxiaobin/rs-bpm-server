package com.xb.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.xb.common.WFConstants;
import com.xb.persistent.RsWorkflow;
import com.xb.persistent.WfAwt;
import com.xb.persistent.WfDef;
import com.xb.persistent.WfInstHist;
import com.xb.persistent.WfInstance;
import com.xb.persistent.WfTask;
import com.xb.persistent.WfTaskAssign;
import com.xb.persistent.WfTaskConn;
import com.xb.persistent.mapper.WfTaskMapper;
import com.xb.service.IRsWorkflowService;
import com.xb.service.IWfAwtService;
import com.xb.service.IWfDefService;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfInstanceService;
import com.xb.service.IWfTaskAssignService;
import com.xb.service.IWfTaskConnService;
import com.xb.service.IWfTaskService;
import com.xb.vo.TaskOptVO;
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
	
	public Integer startWFByGnmkId(String gnmkId, String userId){
		RsWorkflow wfparm = new RsWorkflow();
		wfparm.setGnmkId(gnmkId);
		RsWorkflow wf = rsWfService.selectOne(wfparm);
		if(wf==null){
			return null;
		}
		return startWF4Module(wf.getRsWfId(),userId);
	}
	
	@Transactional
	public Integer startWF4Module(String rsWfId, String currUserId){
		RsWorkflow wf = rsWfService.selectById(rsWfId);
		WfDef wfDefParm = new WfDef();
		wfDefParm.setRsWfId(rsWfId);
		List<WfDef> wfDefList = wfDefService.selectList(wfDefParm, "version desc");
		if (wfDefList == null || wfDefList.isEmpty()) {
			return null;
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
		wfInst.setRefMkid(wf.getGnmkId());
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
		return instNumCurr;
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
		awtService.renewAwt(awt, task, nextTask, wfInst, optVO, currUserId);
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
	
	public List<TaskVO> getNextTasksByOptCode(TaskOptVO optVO){
		String optCode = optVO.getOptCode();
		WfAwt awt = awtService.getAwtByParam(optVO.getRsWfId(), optVO.getInstNum(), optVO.getCurrUserId());
		if(awt==null){
			awt = awtService.getAwtByParam(optVO.getRsWfId(), optVO.getInstNum(), null);
		}
		if(awt==null){
			return null;
		}
		List<TaskVO> nextTaskList = null;
		if(WFConstants.OptTypes.COMMIT.equals(optCode)){
			nextTaskList = new ArrayList<TaskVO>(1);
			WfTask task = this.selectById(getNextTaskId(awt.getTaskIdCurr()));
			TaskVO taskVO = new TaskVO();
			taskVO.setTaskType(WFConstants.TaskTypes.valueOf(task.getTaskType()).getTypeDescp());
			taskVO.setTaskDescpDisp(task.getTaskDescpDisp());
			taskVO.setTaskId(task.getTaskId());
			nextTaskList.add(taskVO);
			return nextTaskList;
		}
		else if(WFConstants.OptTypes.REJECT.equals(optCode)){
			nextTaskList = new ArrayList<TaskVO>(1);
			WfTask task = this.selectById(getPrevTaskId(awt.getTaskIdCurr()));
			TaskVO taskVO = new TaskVO();
			taskVO.setTaskType(WFConstants.TaskTypes.valueOf(task.getTaskType()).getTypeDescp());
			taskVO.setTaskDescpDisp(task.getTaskDescpDisp());
			taskVO.setTaskId(task.getTaskId());
			nextTaskList.add(taskVO);
			//check if allow to goBack to first node
			WfTask awtTask = this.selectById(awt.getTaskIdCurr());
			JSONObject choices = JSONObject.parseObject(awtTask.getTxBkChoices());
			Boolean goBackToFirst = choices.getBoolean("GoBackToFirst");
			if(goBackToFirst!=null && goBackToFirst){
				if(WFConstants.TaskTypes.S.getTypeCode().equals(task.getTaskType())){
					//already first node, no need add to list
				}else{
					WfTask parmTask = new WfTask();
					parmTask.setWfId(task.getWfId());
					parmTask.setTaskType(WFConstants.TaskTypes.S.getTypeCode());
					WfTask firstNode = this.selectOne(parmTask);
					if(firstNode!=null){
						taskVO = new TaskVO();
						taskVO.setTaskType(WFConstants.TaskTypes.valueOf(firstNode.getTaskType()).getTypeDescp());
						taskVO.setTaskDescpDisp(firstNode.getTaskDescpDisp());
						taskVO.setTaskId(firstNode.getTaskId());
						nextTaskList.add(0, taskVO);
					}
				}
			}
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
		String optCode = optVO.getOptCode();
		String currTaskId = awt.getTaskIdCurr();
		String nextTaskId = null;
		if(WFConstants.OptTypes.REJECT.equals(optCode)){
			nextTaskId = getPrevTaskId(currTaskId);
		}else if(WFConstants.OptTypes.COMMIT.equals(optCode)){
			nextTaskId = getNextTaskId(currTaskId);
		}else{
			System.out.println("Currently not support other option code:"+optCode);//TODO: other OptCode , get assigners
			return null;
		}
		JSONObject result = taskAssignerService.getUsersGroupsByTaskId(nextTaskId);
		if(WFConstants.OptTypes.REJECT.equals(optCode)){
			//get prev node execution members
			WfInstHist parmHist = new WfInstHist();
			parmHist.setInstId(awt.getInstId());
			List<WfInstHist> histList = histService.selectList(parmHist, "OPT_SEQ desc");
			if(histList!=null){
				JSONArray prevProcessers = new JSONArray();
				boolean startPrevStepHist = false;
				Set<String> idSet = new HashSet<String>();
				for(WfInstHist hist:histList){
					if(!startPrevStepHist){
						if(nextTaskId.equals(hist.getTaskId())){
							startPrevStepHist=true;
							JSONObject processer = new JSONObject();
							processer.put("id", hist.getOptUser());
							processer.put("name", hist.getOptUser());//TODO: get username
							processer.put("checkFlag", true);
							prevProcessers.add(processer);
						}else{
							continue;
						}
					}else{
						if(nextTaskId.equals(hist.getTaskId())){
							String userId = hist.getOptUser();
							if(idSet.contains(userId)){
								continue;
							}
							idSet.add(userId);
							JSONObject processer = new JSONObject();
							processer.put("id", userId);
							processer.put("name", hist.getOptUser());//TODO: get username
							processer.put("checkFlag", true);
							prevProcessers.add(processer);
						}else{
							break;
						}
					}
				}
				result.put("prevProcessers", prevProcessers);
			}
		}
		return result;
	}
	
	public WfTask getCurrentTaskByRefNum(TaskOptVO optVO){
		WfAwt awt = awtService.getAwtByParam(optVO.getRsWfId(), optVO.getInstNum(), optVO.getCurrUserId());
		if(awt==null){
			awt = awtService.getAwtByParam(optVO.getRsWfId(), optVO.getInstNum(), null);
		}
		if(awt==null){
			return null;
		}
		return this.selectById(awt.getTaskIdCurr());
	}
	
	
	public JSONArray getTaskOptions(TaskOptVO optVO, boolean needGroup){
		WfTask currTask = getCurrentTaskByRefNum(optVO);
		if(currTask==null){
			return  null;
		}
		
		
		return null;
	}
}