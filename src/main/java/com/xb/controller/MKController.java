package com.xb.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xb.base.BaseController;
import com.xb.service.IWfTaskService;

@Controller
@RequestMapping("/mk")
public class MKController extends BaseController {
	
	@Autowired
	IWfTaskService taskService;
	
	@RequestMapping("/task")
	public String hello(HttpSession session, HttpServletRequest req){
		@SuppressWarnings("unchecked")
		Map<String,Object> userinfo = (Map<String, Object>) session.getAttribute(SESSION_USERINFO);
		if(userinfo==null){
			userinfo = new HashMap<String,Object>();
			userinfo.put("userId", "staff1");
			session.setAttribute(SESSION_USERINFO, userinfo);
		}
		String userId = (String) userinfo.get("userId");
		req.setAttribute("userId", userId);
		String rsWfId = req.getParameter("rsWfId");
		String instNum = req.getParameter("instNum");
		String refMkid = req.getParameter("refMkid");
		System.out.println("rsWfId="+rsWfId+",instNum="+instNum+",refMkid="+refMkid);
		return "wf-mk";
	}
	
	@RequestMapping("")
	public String hello2(HttpSession session, HttpServletRequest req){
		return hello(session, req);
	}
	
	/**
	 * 模拟业务中取出相关业务的记录
	 * @param session
	 * @return
	 */
	@RequestMapping("/tasks")
	@ResponseBody
	public Object getWf4Manager(HttpSession session){
		@SuppressWarnings("unchecked")
		Map<String,Object> userinfo = (Map<String, Object>) session.getAttribute(SESSION_USERINFO);
		String userId = (String) userinfo.get("userId");
		if(userId==null){
			userId = "system";
		}
		JSONObject result = new JSONObject();
		result.put("records", taskService.getTasksInbox(userId));
		return result;
	}
	
}
