package com.xb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xb.base.BaseController;
import com.xb.persistent.WfAwt;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfTaskService;

@Controller
@RequestMapping("/inbox")
public class WFInboxController extends BaseController {
	
	@Autowired
	IWfTaskService taskService;
	@Autowired
	IWfInstHistService histService;
	
	@RequestMapping("/")
	public String hello(HttpSession session, HttpServletRequest req){
		@SuppressWarnings("unchecked")
		Map<String,Object> userinfo = (Map<String, Object>) session.getAttribute(SESSION_USERINFO);
		//TODO: for local testing
		if(userinfo==null){
			userinfo = new HashMap<String,Object>();
			userinfo.put("userId", "staff1");
			session.setAttribute(SESSION_USERINFO, userinfo);
		}
		String userId = (String) userinfo.get("userId");
		req.setAttribute("userId", userId);
		return "wf-inbox";
	}
	
	@RequestMapping("")
	public String hello2(HttpSession session, HttpServletRequest req){
		return hello(session, req);
	}
	
	/**
	 * Common API to get task list for Inbox
	 * @param session
	 * @return
	 */
	@RequestMapping("/tasks")
	@ResponseBody
	public Object getWf4Manager(HttpSession session, HttpServletRequest req){
		@SuppressWarnings("unchecked")
		Map<String,Object> userinfo = (Map<String, Object>) session.getAttribute(SESSION_USERINFO);
		String userId = (String) userinfo.get("userId");
		if(userId==null){
			userId = "system";
		}
		String instNumStr = req.getParameter("instNum");
		String refMkid = req.getParameter("refMkid");
		Integer instNum = null;
		if(!StringUtils.isEmpty(instNumStr)){
			try{
				instNum = Integer.parseInt(instNumStr);
			}catch(Exception e){
				//ignore
				System.out.println("getWf4Manager(): invalid instNum="+instNumStr);
				e.printStackTrace();
			}
		}
		JSONObject result = new JSONObject();
		String myTx = req.getParameter("myTx");
		if(StringUtils.isEmpty(myTx) || "PEND".equals(myTx)){
			result.put("records", taskService.getTasksInbox(userId));
		}else if("DONE".equals(myTx)){
			result.put("records", histService.getDoneListByMe(userId, refMkid, instNum));
		}else{//ALL
			List<WfAwt> list = taskService.getTasksInbox(userId);
			if(list==null){
				list = new ArrayList<WfAwt>();
			}
			Set<String> dupCheck = new HashSet<String>();
			for(WfAwt awt:list){
				dupCheck.add(awt.getTaskOwner()+"_"+awt.getRefMkid()+"_"+awt.getInstNum());
			}
			List<WfAwt> doneList = histService.getDoneListByMe(userId, refMkid, instNum);
			if(doneList!=null){
				for(WfAwt awt:doneList){
					String key = awt.getTaskOwner()+"_"+awt.getRefMkid()+"_"+awt.getInstNum();
					if(!dupCheck.contains(key)){
						list.add(awt);
					}
				}
			}
			result.put("records", list);
		}
		return result;
	}
	
	@RequestMapping("/buz")
	public String awtHist4Business(HttpSession session, HttpServletRequest req){
		@SuppressWarnings("unchecked")
		Map<String,Object> userinfo = (Map<String, Object>) session.getAttribute(SESSION_USERINFO);
		//TODO: for local testing
		if(userinfo==null){
			userinfo = new HashMap<String,Object>();
			userinfo.put("userId", "staff1");
			session.setAttribute(SESSION_USERINFO, userinfo);
		}
		req.setAttribute("userId", (String) userinfo.get("userId"));
		return "wf-history";
	}
}
