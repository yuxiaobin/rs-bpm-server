package com.xb.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.xb.common.BusinessException;
import com.xb.common.WFConstants;
import com.xb.persistent.RsWorkflow;
import com.xb.persistent.WfAwt;
import com.xb.persistent.WfInstHist;
import com.xb.persistent.WfInstance;
import com.xb.persistent.WfTask;
import com.xb.persistent.WfTaskAssign;
import com.xb.persistent.WfTaskConn;
import com.xb.persistent.WfVersion;
import com.xb.persistent.mapper.WfTaskMapper;
import com.xb.service.IRsWorkflowService;
import com.xb.service.IWfAwtService;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfInstanceService;
import com.xb.service.IWfTaskAssignService;
import com.xb.service.IWfTaskConnService;
import com.xb.service.IWfTaskService;
import com.xb.service.IWfVersionService;
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
	
//	private static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
	
	private static Logger log = LogManager.getLogger(WfTaskServiceImpl.class);

	@Autowired
	IWfInstHistService histService;
	@Autowired
	IWfTaskConnService taskConnService;
	@Autowired
	IWfInstanceService instService;
	@Autowired
	IWfVersionService wfDefService;
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
	
	public JSONObject startWFByRefMkid(String refMkid, String userId){
		return startWF4Module(refMkid,userId);
	}
	
	@Transactional
	public JSONObject startWF4Module(String refMkid, String currUserId){
		WfVersion wfDefParm = new WfVersion();
		wfDefParm.setRefMkid(refMkid);
		List<WfVersion> wfDefList = wfDefService.selectList(wfDefParm, "version desc");
		if (wfDefList == null || wfDefList.isEmpty()) {
			return null;
		}
		String wfId = wfDefList.get(0).getWfId();
		synchronized (wfId+"_st") {
			WfInstance instParm = new WfInstance();
			instParm.setRefMkid(refMkid);
			List<WfInstance> instList4RefMkid = instService.selectList(instParm, "INST_NUM desc");
			int instNumCurr = 1;
			if(instList4RefMkid!=null && !instList4RefMkid.isEmpty()){
				instNumCurr = instList4RefMkid.get(0).getInstNum()+1;
			}
			
			WfTask taskParm = new WfTask();
			taskParm.setWfId(wfId);
			taskParm.setTaskType(WFConstants.TaskTypes.S.getTypeCode());
			WfTask startTask = this.selectOne(taskParm);
			String taskId = startTask.getTaskId();
			
			WfInstance wfInst = new WfInstance();
			wfInst.setWfId(wfId);
			wfInst.setWfStatus(WFConstants.WFStatus.IN_PROCESS);
			wfInst.setInstNum(instNumCurr);
			wfInst.setRefMkid(refMkid);
			wfInst.setCurrAssigners(currUserId);
			wfInst.setTaskIdCurr(taskId);
			instService.insert(wfInst);
			
			//待办事宜
			WfAwt awt = new WfAwt();
			awt.setAssignerId(currUserId);
			awt.setInstId(wfInst.getInstId());
			awt.setTaskIdCurr(taskId);
			awt.setAwtBegin(new Date());
			
			awtService.insert(awt);
			
			JSONObject result = new JSONObject();
			result.put(WFConstants.ApiParams.RETURN_WF_INST_NUM, instNumCurr);
			result.put(WFConstants.ApiParams.RETURN_CURR_TASK_ID, taskId);
			return result;
		}
	}

	@Transactional
	public JSONObject processTask(TaskOptVO optVO, String currUserId) throws BusinessException{
		optVO.setCurrUserId(currUserId);
		WfAwt awt = getAwtByParm(optVO);
		if(awt==null || !awt.getAssignerId().equals(currUserId)){
			//for recall closed workflow, there is no awt.
			if(WFConstants.OptTypes.RECALL.equals(optVO.getOptCode())){
				WfInstance instParm = new WfInstance();
				instParm.setRefMkid(optVO.getRefMkid());
				instParm.setInstNum(optVO.getInstNum());
				WfInstance inst = instService.selectOne(instParm);
				if(inst==null){
					log.error("processTask(): cannot find instance for optVO="+optVO);
					throw new BusinessException("OPERATE-ERROR","cannot find instance for optVO="+optVO);
				}
				awt = new WfAwt();
				awt.setInstId(inst.getInstId());
				awt.setTaskIdCurr(inst.getTaskIdCurr());
				awt.setTaskIdPre(inst.getTaskIdPre());
				awt.setTaskOwner(currUserId);
			}else{
				if(awt==null){
					log.error("processTask(): cannot get awt record for optVO="+optVO);
					throw new BusinessException("OPERATE-ERROR","cannot find awt for optVO="+optVO);
				}
			}
		}
		WfTask currtask = this.selectById(awt.getTaskIdCurr());
		WfTask nextTask = null;
		String optCode = optVO.getOptCode();
		switch (optCode) {
		case WFConstants.OptTypes.COMMIT:
			if(optVO.getNextTaskId()==null){
				optVO.setNextTaskId(getNextTaskIdByOptCodeAsDefault(optVO));
			}
			nextTask = this.selectById(optVO.getNextTaskId());
			break;
		case WFConstants.OptTypes.REJECT:
			if(optVO.getNextTaskId()==null){
				optVO.setNextTaskId(getNextTaskIdByOptCodeAsDefault(optVO));
			}
			nextTask = this.selectById(optVO.getNextTaskId());
			break;
		case WFConstants.OptTypes.FORWARD:
			nextTask = currtask;
			optVO.setNextTaskId(nextTask.getTaskId());
			break;
		case WFConstants.OptTypes.LET_ME_DO:
			nextTask = currtask;
			optVO.setNextTaskId(nextTask.getTaskId());
			optVO.setNextAssigners(currUserId);
			break;
		case WFConstants.OptTypes.RECALL:
			String nextTaskId = awt.getTaskIdPre();
			optVO.setNextTaskId(nextTaskId);
			nextTask = this.selectById(nextTaskId);
			break;
		default:
			break;
		}
		optVO.setWfId(currtask.getWfId());
		optVO.setPrevInstHistId(histService.createHistRecord(optVO, awt ,currUserId));
		awtService.renewAwt(awt, currtask, nextTask, optVO, currUserId);
		JSONObject result = new JSONObject();
		result.put("buzStatusBefore", currtask.getBuzStatus());
		result.put("buzStatusAfter", nextTask.getBuzStatus());
		return result;
	}
	
	private String getPrevTaskId(String currTaskId){
		WfTaskConn connParm = new WfTaskConn();
		connParm.setTargetTaskId(currTaskId);
		WfTaskConn conn = taskConnService.selectOne(connParm);
		if(conn!=null){
			return conn.getSourceTaskId();//TODO: for condition cases, targetTaskId may have multiple connections@0901
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
		
	public WFDetailVO getWFStatus(String refMkid, Integer instNum){
		WfInstance parmInst = new WfInstance();
		parmInst.setRefMkid(refMkid);
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
		WfVersion wfDef = wfDefService.selectById(instance.getWfId());
		result.setWfDef(wfDef);
		
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
//		this.insertBatch(taskList);
		for(WfTask task:taskList){
			this.insert(task);
		}
		if(assignerAllList!=null && !assignerAllList.isEmpty()){
			for(WfTaskAssign ta:assignerAllList){
				taskAssignerService.insert(ta);
			}
//			taskAssignerService.insertBatch(assignerAllList);
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
		WfAwt awt = getAwtByParm(optVO);
		if(WFConstants.OptTypes.RECALL.equals(optCode)){
			WfInstance instParm = new WfInstance();
			instParm.setInstNum(optVO.getInstNum());
			instParm.setRefMkid(optVO.getRefMkid());
			WfInstance inst = instService.selectOne(instParm);
			WfTask task = this.selectById(inst.getTaskIdPre());
			if(!StringUtils.isEmpty(optVO.getCurrUserId())){
				WfInstHist parm = new WfInstHist();
				parm.setOptUser(optVO.getCurrUserId());
				parm.setInstId(inst.getInstId());
				List<WfInstHist> hist4CurrUser = histService.selectList(parm, "OPT_SEQ DESC");
				if(hist4CurrUser==null || hist4CurrUser.isEmpty()){
					log.warn("======recall warning=====");
					log.warn(optVO+", hist4currUser is null");
					return null;
				}
				WfInstHist hist = hist4CurrUser.get(0);
				if(!hist.getTaskId().equals(task.getTaskId())){//recall forward, taskid will be different
					if(!WFConstants.OptTypes.FORWARD.equals(hist.getOptType())){
						return null;
					}
				}
			}
			List<TaskVO> nextTaskList = new ArrayList<TaskVO>(1);
			TaskVO taskVO = new TaskVO();
			taskVO.setTaskTypeCode(task.getTaskType());
			taskVO.setTaskType(WFConstants.TaskTypes.valueOf(task.getTaskType()).getTypeDescp());
			taskVO.setTaskDescpDisp(task.getTaskDescpDisp());
			taskVO.setTaskId(task.getTaskId());
			nextTaskList.add(taskVO);
			return nextTaskList;
		}
		if(awt==null){
			return null;
		}
		List<TaskVO> nextTaskList = null;
		if(WFConstants.OptTypes.COMMIT.equals(optCode)){
			nextTaskList = new ArrayList<TaskVO>(1);
			String nextTaskId = getNextTaskId(awt.getTaskIdCurr());
			if(nextTaskId==null){
				return null;
			}
			WfTask task = this.selectById(nextTaskId);
			TaskVO taskVO = new TaskVO();
			taskVO.setTaskType(WFConstants.TaskTypes.valueOf(task.getTaskType()).getTypeDescp());
			taskVO.setTaskDescpDisp(task.getTaskDescpDisp());
			taskVO.setTaskId(task.getTaskId());
			nextTaskList.add(taskVO);
			return nextTaskList;
		}
		else if(WFConstants.OptTypes.REJECT.equals(optCode)){
			WfTask currTask = this.selectById(awt.getTaskIdCurr());
			JSONObject txChoices = currTask.getTxChoicesJson();
			Boolean allowGoBack = null;
			if(txChoices!=null){
				allowGoBack = txChoices.getBoolean("AllowGoBack");
			}
			if(allowGoBack==null || !allowGoBack){
				return null;
			}
			JSONObject txBkChoices = currTask.getTxBkChoicesJson();
			Boolean allowBackToPrevious = null;
			Boolean allowBackToFirst = null;
			if(txBkChoices!=null){
				allowBackToPrevious= txBkChoices.getBoolean("GoBackToPrevious");
				allowBackToFirst= txBkChoices.getBoolean("GoBackToFirst");
			}
			boolean prevTaskIsFirstTask = false;
			if(allowBackToPrevious!=null && allowBackToPrevious){
				nextTaskList = new ArrayList<TaskVO>(1);
				WfTask task = this.selectById(getPrevTaskId(awt.getTaskIdCurr()));
				TaskVO taskVO = new TaskVO();
				taskVO.setTaskType(WFConstants.TaskTypes.valueOf(task.getTaskType()).getTypeDescp());
				taskVO.setTaskDescpDisp(task.getTaskDescpDisp());
				taskVO.setTaskId(task.getTaskId());
				nextTaskList.add(taskVO);
				if(WFConstants.TaskTypes.S.getTypeCode().equals(task.getTaskType())){
					prevTaskIsFirstTask = true;
				}
			}
			if(allowBackToFirst!=null && allowBackToFirst && !prevTaskIsFirstTask){
				WfTask parmTask = new WfTask();
				parmTask.setWfId(currTask.getWfId());
				parmTask.setTaskType(WFConstants.TaskTypes.S.getTypeCode());
				WfTask firstNode = this.selectOne(parmTask);
				if(firstNode!=null){
					TaskVO taskVO = new TaskVO();
					taskVO = new TaskVO();
					taskVO.setTaskType(WFConstants.TaskTypes.valueOf(firstNode.getTaskType()).getTypeDescp());
					taskVO.setTaskDescpDisp(firstNode.getTaskDescpDisp());
					taskVO.setTaskId(firstNode.getTaskId());
					nextTaskList.add(0, taskVO);
				}
			}
			return nextTaskList;
		}
		if(WFConstants.OptTypes.FORWARD.equals(optCode)){
			WfTask task = this.selectById(awt.getTaskIdCurr());
			TaskVO taskVO = new TaskVO();
			taskVO.setTaskType(WFConstants.TaskTypes.valueOf(task.getTaskType()).getTypeDescp());
			taskVO.setTaskDescpDisp(task.getTaskDescpDisp());
			taskVO.setTaskId(task.getTaskId());
			nextTaskList = new ArrayList<TaskVO>(1);
			nextTaskList.add(taskVO);
			return nextTaskList;
		}
		//other cases, if need to get next task list.
		return null;
	}
	
	private String getNextTaskIdByOptCodeAsDefault(TaskOptVO optVO){
		List<TaskVO> nextTasks = getNextTasksByOptCode(optVO);
		if(nextTasks==null || nextTasks.isEmpty()){
			return null;
		}
		return nextTasks.get(0).getTaskId();
	}
	
	
	public JSONObject getNextAssignersByOptCode(TaskOptVO optVO){
		String optCode = optVO.getOptCode();
		WfAwt awt = getAwtByParm(optVO);
		if(awt==null){
			if(WFConstants.OptTypes.RECALL.equals(optCode)){
				return getNextAssigner4Recall(optVO.getCurrUserId());
			}
			return null;
		}
		String currTaskId = awt.getTaskIdCurr();
		String nextTaskId = null;
		switch (optCode) {
		case WFConstants.OptTypes.REJECT:
			nextTaskId = getPrevTaskId(currTaskId);
			break;
		case WFConstants.OptTypes.COMMIT:
			nextTaskId = getNextTaskId(currTaskId);
			break;
		case WFConstants.OptTypes.FORWARD:
			nextTaskId = currTaskId;
			break;
		case WFConstants.OptTypes.RECALL:
			return getNextAssigner4Recall(optVO.getCurrUserId());
		default:
			System.out.println("Currently not support other option code:"+optCode);//TODO: other OptCode , get assigners
			log.debug("getNextAssignersByOptCode(): Currently not support other option code:"+optCode);
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
	
	private JSONObject getNextAssigner4Recall(String userId){
		JSONObject result = new JSONObject();
		JSONObject userJson = new JSONObject();
		userJson.put("id", userId);
		userJson.put("name", userId);//TODO: get username
		userJson.put("defSelMod", 1);
		userJson.put("checkFlag",false);
		JSONArray userArray = new JSONArray();
		userArray.add(userJson);
		result.put("users", userArray);
		return result;
	}
	
	private WfAwt getAwtByParm(TaskOptVO optVO){
		WfAwt awt =  null;
		String refMkid = optVO.getRefMkid();
		if(WFConstants.OptTypes.RECALL.equals(optVO.getOptCode())){
			List<WfAwt> awtList = awtService.getAwt4Recall(refMkid, optVO.getInstNum(), optVO.getCurrUserId());
			if(awtList==null || awtList.isEmpty()){
				return null;
			}
			return awtList.get(0);
		}else{
			awt = awtService.getAwtByParam(refMkid, optVO.getInstNum(), optVO.getCurrUserId());
			if(awt==null && optVO.getCurrUserId()!=null){
				awt = awtService.getAwtByParam(refMkid, optVO.getInstNum(), null);
			}
			return awt;
		}
	}
	
	public WfTask getCurrentTaskByRefNum(TaskOptVO optVO){
		WfAwt awt = getAwtByParm(optVO);
		if(awt==null){
			/**
			 * Recall: 流程已经提交完毕，没有awt
			 * 但是END-TASK如果是允许撤回，那就可以撤回，且撤回到上一个
			 * 所以这里要返回END-TASK
			 */
			String refMkid = optVO.getRefMkid();
			if(WFConstants.OptTypes.RECALL.equals(optVO.getOptCode())){
				awt = awtService.getAwtByParam(refMkid, optVO.getInstNum(), null);
				if(awt==null){
					WfInstance instParm = new WfInstance();
					instParm.setRefMkid(refMkid);
					instParm.setInstNum(optVO.getInstNum());
					WfInstance inst = instService.selectOne(instParm);
					if(inst!=null){
						return this.selectById(inst.getTaskIdCurr());
					}else{
						return null;
					}
				}else{
					//存在不符合撤回条件的awt
					return null;
				}
			}else{
				return null;
			}
		}
		return this.selectById(awt.getTaskIdCurr());
	}
	
	
	public JSONArray getTaskOptions(TaskOptVO optVO, boolean needGroup){
		if("admin".equals(optVO.getCurrUserId())){
			//TODO: need to check if any special cases
		}
		WfInstance parm = new WfInstance();
		parm.setInstNum(optVO.getInstNum());
		parm.setRefMkid(optVO.getRefMkid());
		WfInstance instance = instService.selectOne(parm);
		return generateOptions(instance, needGroup);
	}
	
	private JSONArray generateOptions( WfInstance instance, boolean needGroup){
		WfTask task = this.selectById(instance.getTaskIdCurr());
		JSONArray result = genOptionArray();//[C, RJ, RC, V, F, LMD, D, TK]
		if(task==null){
			return result;
		}
		JSONObject choices = JSONObject.parseObject(task.getTxChoices());
		if(WFConstants.TaskTypes.E.getTypeCode().equals(task.getTaskType())){//current is End task, means wf is closed.
			Boolean allowReCall = choices.getBoolean("AllowReCall");
			if(allowReCall!=null &&allowReCall){
				result.getJSONObject(2).put("disflag", false);
			}
			result.getJSONObject(0).put("disflag", true);
			return filterDisabledOptions(result);
		}
		result.getJSONObject(0).put("disflag", false);
		//Forward:
		WfTaskAssign assignParm = new WfTaskAssign();
		assignParm.setTaskId(task.getTaskId());
		int assignerCount = taskAssignerService.selectCount(assignParm);
		if(assignerCount>1){
			result.getJSONObject(4).put("disflag", false);
		}
		//Let Me Do
		if(WFConstants.TxTypes.NORMAL.equals(task.getTxType())){
			WfAwt awtParm = new WfAwt();
			awtParm.setInstId(instance.getInstId());
			awtParm.setTaskIdCurr(task.getTaskId());
			int awtCount = awtService.selectCount(awtParm);
			if(awtCount>1){//普通事务&当前待处理人超过1个人
				result.getJSONObject(5).put("disflag", false);
			}
		}
		if(choices!=null){
			Boolean allowGoBack = choices.getBoolean("AllowGoBack");
			if(allowGoBack!=null && allowGoBack){
				result.getJSONObject(1).put("disflag", false);
			}
		}
		if(!StringUtils.isEmpty(instance.getTaskIdPre())){
			WfTask prevTask = this.selectById(instance.getTaskIdPre());
			JSONObject choicesPrev = JSONObject.parseObject(prevTask.getTxChoices());
			Boolean allowReCall = choicesPrev.getBoolean("AllowReCall");
			if(allowReCall!=null && allowReCall){
				result.getJSONObject(2).put("disflag", false);
			}
		}
		return filterDisabledOptions(result);
	}
	
	private JSONArray filterDisabledOptions(JSONArray result){
		JSONArray filteredResult = new JSONArray();
		for(int i=0;i<result.size();++i){
			JSONObject jsn = result.getJSONObject(i);
			if(!jsn.getBooleanValue("disflag")){
				filteredResult.add(jsn);
			}
		}
		return filteredResult;
	}
	
	private JSONArray genOptionArray(){
		JSONArray result = new JSONArray();
		JSONObject option = null;
		option = new JSONObject();
		option.put("value", WFConstants.OptTypes.COMMIT);
		option.put("descp", WFConstants.OptTypesDescp.COMMIT);
		option.put("disflag", false);//A:active, I:inactive
		result.add(option);
		
		option = new JSONObject();
		option.put("value", WFConstants.OptTypes.REJECT);
		option.put("descp", WFConstants.OptTypesDescp.REJECT);
		option.put("disflag", true);
		result.add(option);
		
		option = new JSONObject();
		option.put("value", WFConstants.OptTypes.RECALL);
		option.put("descp", WFConstants.OptTypesDescp.RECALL);
		option.put("disflag", true);
		result.add(option);
		
		option = new JSONObject();
		option.put("value", WFConstants.OptTypes.VETO);
		option.put("descp", WFConstants.OptTypesDescp.VETO);
		option.put("disflag", true);
		result.add(option);
		
		option = new JSONObject();
		option.put("value", WFConstants.OptTypes.FORWARD);
		option.put("descp", WFConstants.OptTypesDescp.FORWARD);
		option.put("disflag", true);
		result.add(option);
		
		option = new JSONObject();
		option.put("value", WFConstants.OptTypes.LET_ME_DO);
		option.put("descp", WFConstants.OptTypesDescp.LET_ME_DO);
		option.put("disflag", true);
		result.add(option);
		
		option = new JSONObject();
		option.put("value", WFConstants.OptTypes.DISPATCH);
		option.put("descp", WFConstants.OptTypesDescp.DISPATCH);
		option.put("disflag", true);
		result.add(option);
		
		option = new JSONObject();
		option.put("value", WFConstants.OptTypes.TRACK);
		option.put("descp", WFConstants.OptTypesDescp.TRACK);
		option.put("disflag", false);
		result.add(option);
		return result;
	}
	
}