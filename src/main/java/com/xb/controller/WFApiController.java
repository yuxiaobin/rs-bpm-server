package com.xb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xb.base.BaseController;
import com.xb.common.BusinessException;
import com.xb.common.WFConstants;
import com.xb.persistent.WfAwt;
import com.xb.persistent.WfInstHist;
import com.xb.persistent.WfInstance;
import com.xb.service.IRsWorkflowService;
import com.xb.service.IWfApiService;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfInstTrackService;
import com.xb.service.IWfInstanceService;
import com.xb.service.IWfTaskService;
import com.xb.vo.TaskOptVO;
import com.xb.vo.TaskVO;

@Controller
@RequestMapping("/wfapi")
public class WFApiController extends BaseController {
	
	private static final int STATUS_CODE_SUCC = WFConstants.ApiParams.STATUS_CODE_SUCC;
	private static final int STATUS_CODE_FAIL = WFConstants.ApiParams.STATUS_CODE_FAIL;
	private static final int STATUS_CODE_INVALID = WFConstants.ApiParams.STATUS_CODE_INVALID;
	private static final int STATUS_CODE_WF_NOT_DEFINED = WFConstants.ApiParams.STATUS_CODE_WF_NOT_DEFINED;
	private static final int STATUS_CODE_OPT_NOT_ALLOW = WFConstants.ApiParams.STATUS_CODE_OPT_NOT_ALLOW;
	private static final int STATUS_CODE_NO_RECORD = WFConstants.ApiParams.STATUS_CODE_NO_RECORD;
	
	private static final String STATUS_MSG_OPT_NOT_ALLOW = WFConstants.ApiParams.STATUS_MSG_OPT_NOT_ALLOW;
	
	private static final String RETURN_CODE = WFConstants.ApiParams.RETURN_CODE;
	private static final String RETURN_MSG = WFConstants.ApiParams.RETURN_MSG;
	private static final String RETURN_WF_INST_NUM = WFConstants.ApiParams.RETURN_WF_INST_NUM;
	private static final String RETURN_CURR_TASK_ID = WFConstants.ApiParams.RETURN_CURR_TASK_ID;
	private static final String RETURN_RECORDS = WFConstants.ApiParams.RETURN_RECORDS;
	private static final String RETURN_RECORDS_COUNT = WFConstants.ApiParams.RETURN_RECORDS_COUNT;

	private static final String RETURN_TASK_DESCP = "taskDescp";
	private static final String RETURN_TASK_ID = "taskId";
	private static final String RETURN_TASK_TYPE = "taskType";
	private static final String RETURN_TASK_OWNER = "taskOwner";
	
	private static final String PARM_WF_INST_NUM = WFConstants.ApiParams.RETURN_WF_INST_NUM;
	private static final String PARM_USER_ID = WFConstants.ApiParams.PARM_USER_ID;
	private static final String PARM_REFMK_ID = WFConstants.ApiParams.PARM_REFMK_ID;
	private static final String PARM_OPT_CODE = WFConstants.ApiParams.PARM_OPT_CODE;
	private static final String PARM_CALLBACK_URL = WFConstants.ApiParams.PARM_CALLBACK_URL;
	private static final String PARM_TRACK_ID = WFConstants.API_PARM_TRACK_ID;
	
	
	
	@Autowired
	IWfTaskService taskService;
	@Autowired
	IRsWorkflowService rsWfService;
	@Autowired
	IWfApiService wfApiService;
	@Autowired
	IWfInstHistService histService;
	@Autowired
	IWfInstanceService instService;
	@Autowired
	IWfInstTrackService trackService;
	
	@RequestMapping(value="/awt",method=RequestMethod.POST )
	@ResponseBody
	public Object getAwt(@RequestBody JSONObject parm){
		String userId = parm.getString(PARM_USER_ID);
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(userId)){
			result.put(RETURN_CODE, STATUS_CODE_INVALID);
			result.put(RETURN_MSG, "passed in data is empty");
			return result;
		}
		List<WfAwt> list = null;
		try{
			list = taskService.getTasksInbox(userId);
		}
		catch(Exception e){
			log.error("getAwt, parm="+parm.toJSONString(), e);
			result.put(RETURN_CODE, STATUS_CODE_FAIL);
			return result;
		}
		if(list==null || list.isEmpty()){
			result.put(RETURN_CODE, STATUS_CODE_NO_RECORD);
			return result;
		}
		int awtSize = list.size();
		JSONArray records = new JSONArray(awtSize);
		JSONObject record = null;
		for(WfAwt awt : list){
			record = new JSONObject();
			record.put(RETURN_TASK_DESCP, awt.getTaskDescpDisp());				//			record.put("title", awt.getAwtTitle());
			record.put("awtBegin", awt.getAwtBegin());
			record.put("awtEnd", awt.getAwtEnd());
			record.put("preOperator", awt.getOptUsersPre());//上一步处理人
			record.put(RETURN_TASK_OWNER, awt.getTaskOwner());//待处理人 	//			record.put("txProcesser", awt.getOptUsersPre());//已处理人
			record.put("taskCreater", awt.getInstCreater());//创建人
			record.put(PARM_REFMK_ID, awt.getRefMkid());//工作流ID
			record.put(RETURN_WF_INST_NUM, awt.getInstNum());//实例号
			records.add(record);
		}
		result.put(RETURN_CODE, STATUS_CODE_SUCC);
		result.put(RETURN_RECORDS, records);
		result.put(RETURN_RECORDS_COUNT, awtSize);
		return result;
	}
	
	@RequestMapping(value="/start",method=RequestMethod.GET )
	@ResponseBody
	public Object startWf(){
		JSONObject result = new JSONObject();
		result.put(RETURN_CODE, STATUS_CODE_INVALID);
		result.put(RETURN_MSG, "GET method is not allowed");
		return result;
	}
	
	@RequestMapping(value="/start",method=RequestMethod.POST )
	@ResponseBody
	public Object startWf(@RequestBody JSONObject parm, HttpServletRequest request){
		setCurrentUser(parm);
		if(request.getHeader(PARM_TRACK_ID)==null){
			log.warn("no track ID in request header for parm="+parm.toString());
		}
		String userId = parm.getString(PARM_USER_ID);
		String refMkid = parm.getString(PARM_REFMK_ID);
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(refMkid) || StringUtils.isEmpty(userId)){
			result.put(RETURN_CODE, STATUS_CODE_INVALID);
			result.put(RETURN_MSG, "passed in data is empty");
			return result;
		}
		try{
			JSONObject startResult = taskService.startWFByRefMkid(refMkid, userId);
			if(startResult!=null){
				result.put(RETURN_CODE, STATUS_CODE_SUCC);
				result.put(RETURN_WF_INST_NUM, startResult.getInteger(RETURN_WF_INST_NUM));
				result.put(RETURN_CURR_TASK_ID, startResult.getString(RETURN_CURR_TASK_ID));
				result.put(RETURN_MSG, "succ");
			}else{
				result.put(RETURN_CODE, STATUS_CODE_WF_NOT_DEFINED);
				result.put(RETURN_MSG, "WF not defined");
			}
		}catch(Exception e){
			log.error("startWf,parm="+parm.toJSONString(), e);
			result.put(RETURN_CODE, STATUS_CODE_FAIL);
			result.put(RETURN_MSG, "failed to start workflow");
		}
		return result;
	}
	
	@RequestMapping(value="/options",method=RequestMethod.POST )
	@ResponseBody
	public Object getTaskOptions(@RequestBody JSONObject parm){
		String userId = parm.getString(PARM_USER_ID);
		String refMkid = parm.getString(PARM_REFMK_ID);
		String wfInstNumStr = parm.getString(PARM_WF_INST_NUM);
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(refMkid) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(wfInstNumStr)){
			result.put(RETURN_CODE, STATUS_CODE_INVALID);
			result.put(RETURN_MSG, "passed in data is empty");
			return result;
		}
		Integer instNum = null;
		try{
			instNum = Integer.parseInt(wfInstNumStr);
		}
		catch(Exception e){
			result.put(RETURN_CODE, STATUS_CODE_INVALID);
			result.put(RETURN_MSG, "wfInstNum is not a number");
			return result;
		}
		try{
			TaskOptVO optVO = new TaskOptVO();
			optVO.setCurrUserId(userId);
			optVO.setInstNum(instNum);
			optVO.setRefMkid(refMkid);
			result.put(RETURN_RECORDS, taskService.getTaskOptions(optVO, false));
			result.put(RETURN_CODE, STATUS_CODE_SUCC);
		}catch(Exception e){
			log.error("getTaskOptions, parm="+parm.toJSONString(), e);
			result.put(RETURN_CODE, STATUS_CODE_FAIL);
			result.put(RETURN_MSG, "error when get options");
		}
		return result;
	}
	
	@RequestMapping(value="/operate",method=RequestMethod.GET )
	public Object loadProcessTask(HttpServletRequest req){
		String instNumStr = req.getParameter(PARM_WF_INST_NUM);
		String refMkid = req.getParameter(PARM_REFMK_ID);
		String optCode = req.getParameter(PARM_OPT_CODE);
		String callbackUrl = req.getParameter(PARM_CALLBACK_URL);
		if(StringUtils.isEmpty(instNumStr) || StringUtils.isEmpty(refMkid) || StringUtils.isEmpty(optCode)){
			req.setAttribute(RETURN_CODE, STATUS_CODE_INVALID);
			req.setAttribute(RETURN_MSG, "passed in data is empty");
			return "error";
		}
		Integer instNum = null;
		try{
			instNum = Integer.parseInt(instNumStr);
		}catch(Exception e){
			req.setAttribute(RETURN_CODE, STATUS_CODE_INVALID);
			req.setAttribute(RETURN_MSG, "wfInstNum is not a number");
			return "error";
		}
		if(!isValidOptCode(optCode)){
			req.setAttribute(RETURN_CODE, STATUS_CODE_INVALID);
			req.setAttribute(RETURN_MSG, "optCode is invalid");
			return "error";
		}
		
		try{
			req.setAttribute("instNum", instNum);
			req.setAttribute(PARM_REF_MKID, refMkid);
			if(WFConstants.OptTypes.TRACK.equals(optCode)){
				return "wf-popup-track";
			}else{
				req.setAttribute("callbackUrl", callbackUrl);
				req.setAttribute("userId", req.getParameter("userId"));
				req.setAttribute(PARM_OPT_CODE, optCode);
				//提交，退回，否决等操作事务页面
				TaskOptVO optVO = new TaskOptVO();
				optVO.setRefMkid(refMkid);
				optVO.setInstNum(instNum);
				optVO.setOptCode(optCode);
				req.setAttribute("TX_PR_CHOICES",taskService.getCurrentTaskByRefNum(optVO).getTxPrChoices());
				return "wf-popup-opt";
			}
		}catch(Exception e){
			log.error("loadProcessTask",e);
			req.setAttribute(RETURN_CODE, STATUS_CODE_FAIL);
			req.setAttribute(RETURN_MSG, "系统调用出错");
			return "error";
		}
	}
	
	//get assigner
	/*@RequestMapping(value="/assigners",method=RequestMethod.POST )
	@ResponseBody
	public Object getAssigners(@RequestBody JSONObject parm){
		
	}*/
	
	//get next task list
	@RequestMapping(value="/tasks",method=RequestMethod.POST )
	@ResponseBody
	public Object getTasks(@RequestBody JSONObject parm){
		String refMkid = parm.getString(PARM_REFMK_ID);
		String wfInstNumStr = parm.getString(PARM_WF_INST_NUM);
		String optCode = parm.getString(PARM_OPT_CODE);
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(refMkid) || StringUtils.isEmpty(wfInstNumStr) || StringUtils.isEmpty(optCode) ){
			result.put(RETURN_CODE, STATUS_CODE_INVALID);
			result.put(RETURN_MSG, "passed in data is empty");
			return result;
		}
		if(!validateInstNumAndOptCode(result, wfInstNumStr,optCode)){
			return result;
		}
		Integer instNum = Integer.parseInt(wfInstNumStr);;
		try{
			TaskOptVO optVO = new TaskOptVO();
			optVO.setRefMkid(refMkid);
			optVO.setInstNum(instNum);
			optVO.setOptCode(optCode);
			List<TaskVO> taskList = taskService.getNextTasksByOptCode(optVO);
			JSONArray taskArray = new JSONArray(taskList.size());
			if(taskList!=null && !taskList.isEmpty()){
				JSONObject task = null;
				for(TaskVO tv:taskList){
					task = new JSONObject();
					task.put(RETURN_TASK_ID, tv.getTaskId());
					task.put(RETURN_TASK_TYPE, tv.getTaskType());
					task.put(RETURN_TASK_DESCP, tv.getTaskDescpDisp());
					taskArray.add(task);
				}
			}
			result.put(RETURN_RECORDS, taskArray);
		}catch(Exception e){
			log.error("getTasks, parm="+parm.toJSONString(), e);
			result.put(RETURN_CODE, STATUS_CODE_FAIL);
			result.put(RETURN_MSG, "系统调用出错");
		}
		return result;
	}
	
	//get history
	@RequestMapping(value="/history",method=RequestMethod.POST )
	@ResponseBody
	public Object getWFHistory(@RequestBody JSONObject parm){
		String refMkid = parm.getString(PARM_REFMK_ID);
		String wfInstNumStr = parm.getString(PARM_WF_INST_NUM);
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(refMkid) || StringUtils.isEmpty(wfInstNumStr) ){
			result.put(RETURN_CODE, STATUS_CODE_INVALID);
			result.put(RETURN_MSG, "passed in data is empty");
			return result;
		}
		Integer instNum = null;
		try{
			instNum = Integer.parseInt(wfInstNumStr);
		}
		catch(Exception e){
			result.put(RETURN_CODE, STATUS_CODE_INVALID);
			result.put(RETURN_MSG, "wfInstNum is not a number");
			return result;
		}
		try{
			WfInstance instParm = new WfInstance();
			instParm.setInstNum(instNum);
			instParm.setRefMkid(refMkid);
			WfInstance inst = instService.selectOne(instParm);
			if(inst==null){
				result.put(RETURN_CODE, STATUS_CODE_INVALID);
				result.put(RETURN_MSG, "no record found for refMkid="+refMkid);
				return result;
			}
			List<WfInstHist> histList = histService.viewWfInstHistory(inst.getInstId());
			if(histList==null||histList.isEmpty()){
				result.put(RETURN_CODE, STATUS_CODE_NO_RECORD);
				result.put(RETURN_MSG, "no record found");
			}else{
				JSONArray histArray = new JSONArray();
				JSONObject histJson = null;
				for(WfInstHist hist:histList){
					histJson = new JSONObject();
					histJson.put(RETURN_TASK_DESCP, hist.getTaskDescpDisp());
					histJson.put("optUser", hist.getOptUser());
					histJson.put(PARM_OPT_CODE, hist.getOptType());
					histJson.put("comments", hist.getOptComm());
					histJson.put("taskBegin", hist.getTaskBegin());
					histJson.put("taskEnd", hist.getTaskEnd());
					histJson.put("taskRend", hist.getTaskRend());
					histJson.put(RETURN_TASK_OWNER, hist.getTaskOwner());
					histArray.add(histJson);
				}
				result.put(RETURN_CODE, STATUS_CODE_SUCC);
				result.put(RETURN_RECORDS, histArray);
			}
		}catch(Exception e){
			log.error("getWFHistory: parm="+parm, e);
			result.put(RETURN_CODE, STATUS_CODE_FAIL);
			result.put(RETURN_MSG, "系统调用出错");
		}
		return result;
	}
	
	
	/**
	 * API for Operate Task
	 * @param parm
	 * @return
	 */
	@RequestMapping(value="/operate",method=RequestMethod.POST )
	@ResponseBody
	public Object doOperate(@RequestBody JSONObject parm, HttpServletRequest request){
		setCurrentUser(parm);
		if(request.getHeader(PARM_TRACK_ID)==null){
			log.warn("no track ID in request header for parm="+parm.toString());
		}
		String userId = parm.getString(PARM_USER_ID);
		String refMkid = parm.getString(PARM_REFMK_ID);
		String wfInstNumStr = parm.getString(PARM_WF_INST_NUM);
		String optCode = parm.getString(PARM_OPT_CODE);
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(refMkid)
				|| StringUtils.isEmpty(wfInstNumStr) || StringUtils.isEmpty(optCode)){
			log.warn("================================= invalid parameter= for refMkid= "+ refMkid+"===================");
			result.put(RETURN_CODE, STATUS_CODE_INVALID);
			result.put(RETURN_MSG, "passed in data is empty");
			return result;
		}
		if(WFConstants.OptTypes.TRACK.equals(optCode)){
			return getWFHistory(parm);
		}
		if(!validateInstNumAndOptCode(result, wfInstNumStr,optCode)){
			return result;
		}
		TaskOptVO optVO = new TaskOptVO();
		optVO.setCurrUserId(userId);
		optVO.setRefMkid(refMkid);
		optVO.setInstNum(Integer.parseInt(wfInstNumStr));
		optVO.setOptCode(optCode);
		optVO.setNextAssigners(parm.getString("nextUserIds"));
		optVO.setNextTaskId(parm.getString("nextTaskId"));
		optVO.setComments(parm.getString("comments"));
		
		try{
			if(!wfApiService.validateOperate(optVO, result)){
				return result;
			}
			result = taskService.processTask(optVO, userId);
			result.put(RETURN_CODE, STATUS_CODE_SUCC);
			result.put(RETURN_MSG, "Succ");
			return result;
		}
		catch(BusinessException e){
			log.error("doOperate : parm="+parm, e);
			result.put(RETURN_CODE, STATUS_CODE_OPT_NOT_ALLOW);
			result.put(RETURN_MSG, STATUS_MSG_OPT_NOT_ALLOW);
		}
		catch(Exception e){
			log.error("doOperate : parm="+parm, e);
			result.put(RETURN_CODE, STATUS_CODE_FAIL);
			result.put(RETURN_MSG, "系统调用出错");
		}
		return result;
	}
	
	/**
	 * API for Operate Task
	 * @param parm
	 * @return
	 */
	@RequestMapping(value="/rollback",method=RequestMethod.POST )
	@ResponseBody
	public Object doRollback(@RequestBody JSONObject parm,HttpServletRequest request){
		setCurrentUser(parm);
		JSONObject result = new JSONObject();
		String trackId = parm.getString(PARM_TRACK_ID);
		if(trackId==null){
			log.warn("no track ID in request header for parm="+parm.toString());
			result.put(RETURN_CODE, STATUS_CODE_INVALID);
			result.put(RETURN_MSG, "no trackID passed in request header");
			return result;
		}
		try{
			if(trackService.doRollback(trackId)){
				result.put(RETURN_CODE, STATUS_CODE_SUCC);
				result.put(RETURN_MSG, "rollback success");
			}else{
				result.put(RETURN_CODE, STATUS_CODE_OPT_NOT_ALLOW);
				result.put(RETURN_MSG, "already rollbacked before");
			}
		}catch(Exception e){
			log.error("rollback failed for trackId="+trackId, e);
			result.put(RETURN_CODE, STATUS_CODE_FAIL);
			result.put(RETURN_MSG, "rollback failed");
		}
		return result;
	}
	
	private boolean validateInstNumAndOptCode(JSONObject result, String instNumStr, String optCode){
		try{
			Integer.parseInt(instNumStr);
		}
		catch(Exception e){
			result.put(RETURN_CODE, STATUS_CODE_INVALID);
			result.put(RETURN_MSG, "wfInstNum is not a number");
			return false;
		}
		if(!isValidOptCode(optCode)){
			result.put(RETURN_CODE, STATUS_CODE_INVALID);
			result.put(RETURN_MSG, "optCode is invalid");
			return false;
		}
		result.put(RETURN_CODE, STATUS_CODE_SUCC);
		return true;
	}
	
}
