package com.xb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xb.base.BaseController;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfInstanceService;
import com.xb.service.IWfTaskService;
import com.xb.vo.TaskOptVO;

/**
 * 跟task相关的都在此controller
 * 跟wf相关放在WFController
 * @author yuxiaobin
 *
 */
@Controller
@RequestMapping("/task")
public class WFTaskController extends BaseController {
	
	@Autowired
	IWfTaskService taskService;
	@Autowired
	IWfInstHistService instHistService;
	@Autowired
	IWfInstanceService instService;
	
	@RequestMapping("/process")
	@ResponseBody
	public Object processTask(@RequestBody TaskOptVO optVO, HttpSession session){
		taskService.processTask(optVO, getCurrUserId(session));
		JSONObject result = new JSONObject();
		result.put("message", "success");
		return result;
	}
	
	
	@RequestMapping("/loadprocess")
	public Object loadProcessTask(HttpSession session, HttpServletRequest req){
//		Map<String,Object> userInfo = getUserInfo(session);
//		String userId = (String) userInfo.get("userId");
		
		String rsWfId = req.getParameter("rsWfId");
		String instNum = req.getParameter("instNum");
		String refMkid = req.getParameter("refMkid");
		req.setAttribute("rsWfId", rsWfId);
		req.setAttribute("instNum", instNum);
		req.setAttribute("refMkid", refMkid);
		String optCode = req.getParameter("optCode");
		if("TRACK".equals(optCode)){
			return "wf-popup-track";
		}else{
			req.setAttribute("optCode", optCode);
			//提交，退回，否决等操作事务页面
			return "wf-popup-opt";
		}
	}
	
	@RequestMapping("/next/tasks")
	@ResponseBody
	public Object getNextTasks(@RequestBody TaskOptVO optVO, HttpSession session){
		optVO.setCurrUserId(getCurrUserId(session));
		JSONObject result = new JSONObject();
		result.put("records", taskService.getNextTasksByOptCode(optVO));
		return result;
	}
	
	@RequestMapping("/next/usergroups")
	@ResponseBody
	public Object getNextAssigners(@RequestBody TaskOptVO optVO, HttpSession session){
		optVO.setCurrUserId(getCurrUserId(session));
		JSONObject result = new JSONObject();
		result.put("result", taskService.getNextAssignersByOptCode(optVO));
		return result;
	}
	
	
	
}