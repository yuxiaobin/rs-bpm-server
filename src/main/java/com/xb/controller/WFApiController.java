package com.xb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.xb.base.BaseController;
import com.xb.service.IWfTaskService;
import com.xb.vo.TaskOptVO;

@RestController
@RequestMapping("/wfapi")
public class WFApiController extends BaseController {
	
	private static final int STATUS_CODE_SUCC = 0;
	private static final int STATUS_CODE_FAIL = 1;
	
	private static final String RETURN_CODE = "return_code";
	private static final String RETURN_MSG = "return_msg";
	private static final String RETURN_WF_INST_NUM = "wf_inst_num";
	private static final String RETURN_TASK_OPTIONS = "task_options";
	
	@Autowired
	IWfTaskService taskService;
	
	@RequestMapping(value="/awt",method=RequestMethod.POST )
	public Object getAwt(@RequestBody JSONObject parm){
		String userId = parm.getString("userId");
		JSONObject result = new JSONObject();
		result.put("records", taskService.getTasksInbox(userId));
		return result;
	}
	
	@RequestMapping(value="/start",method=RequestMethod.GET )
	public Object startWf(){
		JSONObject result = new JSONObject();
		result.put(RETURN_CODE, STATUS_CODE_FAIL);
		result.put(RETURN_MSG, "GET method is not allowed");
		return result;
	}
	
	@RequestMapping(value="/start",method=RequestMethod.POST )
	public Object startWf(@RequestBody JSONObject parm){
		String userId = parm.getString("userId");
		String gnmkId = parm.getString("gnmkId");
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(gnmkId) || StringUtils.isEmpty(userId)){
			result.put(RETURN_CODE, STATUS_CODE_FAIL);
			result.put(RETURN_MSG, "passed in data is empty");
			return result;
		}
		try{
			Integer instNum = taskService.startWFByGnmkId(gnmkId, userId);
			if(instNum!=null){
				result.put(RETURN_CODE, STATUS_CODE_SUCC);
				result.put(RETURN_WF_INST_NUM, instNum);
				result.put(RETURN_MSG, "succ");
			}else{
				result.put(RETURN_CODE, STATUS_CODE_FAIL);
				result.put(RETURN_MSG, "no wf record found");
			}
		}catch(Exception e){
			result.put(RETURN_CODE, STATUS_CODE_FAIL);
			result.put(RETURN_MSG, "failed to start workflow");
		}
		return result;
	}
	
	@RequestMapping(value="/options",method=RequestMethod.POST )
	public Object getTaskOptions(@RequestBody JSONObject parm){
		String userId = parm.getString("userId");
		String gnmkId = parm.getString("gnmkId");
		String wfInstNumStr = parm.getString("wfInstNum");
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(gnmkId) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(wfInstNumStr)){
			result.put(RETURN_CODE, STATUS_CODE_FAIL);
			result.put(RETURN_MSG, "passed in data is empty");
			return result;
		}
		Integer instNum = null;
		try{
			instNum = Integer.parseInt(wfInstNumStr);
		}
		catch(Exception e){
			result.put(RETURN_CODE, STATUS_CODE_FAIL);
			result.put(RETURN_MSG, "wfInstNum is not a number");
			return result;
		}
		try{
			TaskOptVO optVO = new TaskOptVO();
			optVO.setCurrUserId(userId);
			optVO.setInstNum(instNum);
			optVO.setGnmkId(gnmkId);
			result.put(RETURN_CODE, STATUS_CODE_SUCC);
			result.put(RETURN_TASK_OPTIONS, taskService.getTaskOptions(optVO, false));
		}catch(Exception e){
			result.put(RETURN_CODE, STATUS_CODE_FAIL);
			result.put(RETURN_MSG, "error when get options");
		}
		return result;
	}
	
}
