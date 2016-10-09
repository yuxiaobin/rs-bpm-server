package com.xb.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xb.base.BaseController;
import com.xb.persistent.RsWorkflow;
import com.xb.service.IRsWorkflowService;
import com.xb.service.IWfAwtService;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfInstanceService;
import com.xb.service.IWfTaskService;
import com.xb.utils.WfDataUtil;
import com.xb.vo.TaskOptVO;

@Controller
@RequestMapping("/wf")
public class WFController extends BaseController {
	
	
	@Autowired
	IRsWorkflowService wfService;
	@Autowired
	IWfTaskService taskService;
	@Autowired
	IWfInstHistService instHistService;
	@Autowired
	IWfInstanceService instService;
	@Autowired
	IWfAwtService awtService;
	
	@RequestMapping("/")
	public String hello(HttpSession session){
		return "hello";
	}
	@RequestMapping("")
	public String hello2(HttpSession session){
		return "hello";
	}
	
	@RequestMapping("/{userId}")
	public String viewWf(@PathVariable String userId, HttpSession session){
		Map<String,Object> userinfo = new HashMap<String,Object>();
		userinfo.put("userId", userId);
		session.setAttribute(SESSION_USERINFO,userinfo);
		String roleName = null;
		if(userId.startsWith("staff")){
			roleName = "staff";
		}else if(userId.startsWith("manager")){
			roleName = "manager";
		}
		else{
			roleName = "admin";
		}
		if("admin".equals(roleName)){
			return "redirect:/wfadmin/modules";
		}
		return "redirect:/inbox";
	}
	
	
	
	@RequestMapping(value="/modules/listall",method=RequestMethod.GET )
	@ResponseBody
	public Object getAllModulesWithWfDefs(){
		JSONObject page = new JSONObject();
		page.put("records", wfService.selectList(new RsWorkflow()));
		return page;
	}
	
	
	@RequestMapping(value="/start", method=RequestMethod.POST)
	@ResponseBody
	public Object startWf4Module(@RequestBody TaskOptVO optVO, HttpSession session){
		taskService.startWF4Module(optVO.getRefMkid(), getCurrUserId(session));
		JSONObject result = new JSONObject();
		result.put("message", "success");
		return result;
	}

	
	@RequestMapping(value="/modules/page" )
	public String modulesPage(HttpSession session, HttpServletRequest req){
		return "wf-modules-view";
	}
	
	/**********************Workflow Track * start ********************/
	@RequestMapping(value="/hist",method=RequestMethod.GET )
	@ResponseBody
	public Object viewInstHistory(HttpSession session,HttpServletRequest req){
		String refMkid = req.getParameter("refMkid");
		String instNumStr = req.getParameter("instNum");
		if(StringUtils.isEmpty(instNumStr) || !NumberUtils.isNumber(instNumStr)){
			System.err.println("getWfStatus(): invalid instNum="+instNumStr);
			return "";
		}
		int instNum = NumberUtils.toInt(instNumStr);
		JSONObject result = new JSONObject();
		result.put("records", instHistService.viewWfInstHistory(refMkid, instNum));
		return result;
	}
	
	@RequestMapping(value="/status",method=RequestMethod.GET )
	@ResponseBody
	public Object getWfStatus(HttpSession session, HttpServletRequest req){
		String instNum = req.getParameter("instNum");
		String refMkid = req.getParameter("refMkid");
		if(StringUtils.isEmpty(instNum) || !NumberUtils.isNumber(instNum)){
			System.err.println("getWfStatus(): invalid instNum="+instNum);
			return "";
		}
		JSONObject result = WfDataUtil.generateWfJson(taskService.getWFStatus(refMkid, NumberUtils.toInt(instNum)));
		return result;
	}
	/**********************Workflow Track * end ********************/

}
