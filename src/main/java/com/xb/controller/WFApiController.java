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

@RestController
@RequestMapping("/wfapi")
public class WFApiController extends BaseController {
	
	private static final int STATUS_CODE_SUCC = 0;
	private static final int STATUS_CODE_FAIL = 1;
	
	private static final String RETURN_CODE = "return_code";
	private static final String RETURN_MSG = "return_msg";
	
	@Autowired
	IWfTaskService taskService;
	
	@RequestMapping(value="/awt",method=RequestMethod.POST )
	public Object getAwt(@RequestBody JSONObject parm){
		String userId = parm.getString("userId");
		JSONObject result = new JSONObject();
		result.put("records", taskService.getTasksInbox(userId));
		return result;
	}
	
	@RequestMapping(value="/start",method=RequestMethod.POST )
	public Object startWf(@RequestBody JSONObject parm){
		String userId = parm.getString("userId");
		String gnmkId = parm.getString("gnmkId");
		String wfRefNo = parm.getString("wfRefNo");
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(wfRefNo) || StringUtils.isEmpty(userId)){
			result.put(RETURN_CODE, STATUS_CODE_FAIL);
			result.put(RETURN_MSG, "data is empty");
			return result;
		}
		try{
			if(taskService.startWF4Module(gnmkId, wfRefNo, userId)){
				result.put(RETURN_CODE, STATUS_CODE_SUCC);
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

	
}
