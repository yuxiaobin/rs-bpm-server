package com.xb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xb.base.BaseController;
import com.xb.persistent.RsModule;
import com.xb.persistent.WfAwt;
import com.xb.persistent.WfDef;
import com.xb.persistent.WfInstHist;
import com.xb.service.IRsModuleService;
import com.xb.service.IRsWorkflowService;
import com.xb.service.ITblUser2groupService;
import com.xb.service.ITblUserService;
import com.xb.service.IWfAwtService;
import com.xb.service.IWfDefService;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfInstanceService;
import com.xb.service.IWfTaskService;
import com.xb.utils.WfDataUtil;

@Controller
@RequestMapping("/wf")
public class WFController extends BaseController {
	
	
	@Autowired
	IRsModuleService moduleService;
	@Autowired
	IRsWorkflowService wfService;
	@Autowired
	IWfDefService wfDefService;
	@Autowired
	IWfTaskService taskService;
	@Autowired
	IWfInstHistService instHistService;
	@Autowired
	IWfInstanceService instService;
	@Autowired
	ITblUserService userService;
	@Autowired
	ITblUser2groupService user2GroupService;
	@Autowired
	IWfAwtService awtService;;
	
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
//		return "wf-"+roleName;
		return "redirect:/inbox";
	}
	
	
	
	@RequestMapping(value="/modules/listall",method=RequestMethod.GET )
	@ResponseBody
	public Object getAllModulesWithWfDefs(){
		RsModule parm = new RsModule();
//		Page<RsModule> page = new Page<>(0, 100);
		JSONObject page = new JSONObject();
		List<RsModule> list = moduleService.selectList(parm);
		if(list!=null){
			WfDef wfDefParm = new WfDef();
			for(RsModule module:list){
				String rsWfId = module.getRsWfId();
				if(StringUtils.isEmpty(rsWfId)){
					continue;
				}
				wfDefParm.setRsWfId(rsWfId);
				module.setWfList(wfDefService.selectList(wfDefParm, "version"));
			}
		}
		page.put("records", list);
//		page.setRecords(list);
//		page.setTotal(list.size());
		return page;
	}
	
	
	@RequestMapping(value="/module/{moduleId}/wf/{wfId}",method=RequestMethod.GET )
	@ResponseBody
	public Object getWfDtl4Module(@PathVariable String moduleId, @PathVariable String wfId, HttpSession session){
		if(StringUtils.isEmpty(moduleId)){
			System.out.println("viewWf4Module(): moduleId is empty");
			return "";
		}
		if(wfId=="init"){
			wfId = null;
		}
		JSONObject result = WfDataUtil.generateWfJson(wfService.getWF4Module(moduleId,wfId));
		result.put("role", session.getAttribute(SESSION_USERINFO));
		return result;
	}
	
	
	/************staff view only********************/
	@RequestMapping("/view/{wfId}")
	public String viewWf4Staff(@PathVariable String wfId, HttpSession session, HttpServletRequest req){
		if(StringUtils.isEmpty(wfId)){
			System.out.println("viewWf4Staff(): wfId is empty");
			return "";
		}
		WfDef parm = new WfDef();
		parm.setWfId(wfId);
		WfDef wfDef = wfDefService.selectOne(parm);
		if(wfDef==null){
			System.out.println("viewWf4Staff(): wfDef is null");
			return "";
		}
		String rsWfId = wfDef.getRsWfId();
		RsModule moduleParm = new RsModule();
		moduleParm.setRsWfId(rsWfId);
		RsModule module = moduleService.selectOne(moduleParm);
		req.setAttribute("moduleId", module.getModId());
		req.setAttribute("moduleName", module.getNAME());
		req.setAttribute("wfId", wfId);
		parm.setWfId(null);
		parm.setRsWfId(rsWfId);
		List<WfDef> wfList = wfDefService.selectList(parm, "version desc");
		if(wfList.get(0).getVERSION()>wfDef.getVERSION()){
			req.setAttribute("latestFlag", String.valueOf(false));
		}else{
			req.setAttribute("latestFlag", String.valueOf(true));
		}
		
		return "wf-view";
	}
	
	@RequestMapping("/start/{moduleId}")
	@ResponseBody
	public Object startWf4Module(@PathVariable String moduleId,HttpSession session){
		if(StringUtils.isEmpty(moduleId)){
			System.out.println("viewWf4Module(): moduleId is empty");
			return "";
		}
		
		Map<String,Object> userinfo = getUserInfo(session);
		String userId = (String) userinfo.get("userId");
		taskService.startWF4Module(moduleId, userId);
		JSONObject result = new JSONObject();
		result.put("message", "success");
		return result;
	}

	/**
	 * Common API to get task list for Inbox
	 * @param session
	 * @return
	 */
	@RequestMapping("/inbox/tasks")
	@ResponseBody
	public Object getWf4Manager(HttpSession session){
		Map<String,Object> userinfo = getUserInfo(session);
		String userId = (String) userinfo.get("userId");
		if(userId==null){
			userId = "system";
		}
		JSONObject result = new JSONObject();
		result.put("records", taskService.getTasksInbox(userId));
		return result;
	}
	
	/*@RequestMapping(value="/history/{instId}", method=RequestMethod.GET )//TODO:
	public String viewTaskHist(@PathVariable String instId, HttpServletRequest req){
		WfInstance instance = instService.selectById(instId);
		if(instance==null){
			System.err.println("viewTaksHist(): instance is null for instId="+instId);
			return "";
		}
		String wfId = instance.getWfId();
		WfDef wfDefParm = new WfDef();
		wfDefParm.setWfId(wfId);
		WfDef wfDef = wfDefService.selectOne(wfDefParm);
		RsModule moduleParm = new RsModule();
		moduleParm.setRsWfId(wfDef.getRsWfId());
		RsModule module = moduleService.selectOne(moduleParm);
		req.setAttribute("moduleId", module.getModId());
		req.setAttribute("moduleName", module.getNAME());
		req.setAttribute("wfId", wfId);
		req.setAttribute("latestFlag", String.valueOf(false));
		req.setAttribute("instId", instId);
		return "wf-view";
	}*/
	
	@RequestMapping(value="/inbox" )
	public String viewInbox(HttpSession session, HttpServletRequest req){
		Map<String,Object> userinfo = getUserInfo(session);
		String userId = (String) userinfo.get("userId");
		req.setAttribute("userId", userId);
		return "wf-inbox";
	}
	
	@RequestMapping(value="/modules/page" )
	public String modulesPage(HttpSession session, HttpServletRequest req){
		Map<String,Object> userinfo =getUserInfo(session);
		String userId = (String) userinfo.get("userId");
		req.setAttribute("role", userId);
		return "wf-modules-view";
	}
	
	
	@RequestMapping(value="/hist",method=RequestMethod.GET )
	@ResponseBody
	public Object viewInstHistory(HttpSession session,HttpServletRequest req){
		String rsWfId = req.getParameter("rsWfId");
		String instNum = req.getParameter("instNum");
		if(StringUtils.isEmpty(instNum) || !NumberUtils.isNumber(instNum)){
			System.err.println("getWfStatus(): invalid instNum="+instNum);
			return "";
		}
		int instNumber = NumberUtils.toInt(instNum);
		JSONObject result = new JSONObject();
		List<WfInstHist> list = instHistService.viewWfInstHistory(rsWfId, instNumber);
		
		Map<String,Object> userinfo = getUserInfo(session);
		String userId = (String) userinfo.get("userId");
		WfAwt awt = awtService.getAwtByParam(rsWfId, instNumber, userId);
		if(awt!=null){
			WfInstHist awtHist = new WfInstHist();
			awtHist.setTaskDescpDisp(awt.getTaskDescpDisp());
			awtHist.setInstId(awt.getInstId());
			
			awtHist.setTaskBegin(awt.getAwtBegin());
			awtHist.setTaskEnd(awt.getAwtEnd());
			awtHist.setTaskOwner(awt.getTaskOwner());
			if(list==null){
				list = new ArrayList<WfInstHist>(1);
			}else{
				if(!list.isEmpty()){
					awtHist.setNextAssigner(list.get(list.size()-1).getNextAssigner());
				}else{
					awtHist.setNextAssigner(userId);
				}
			}
			list.add(awtHist);
		}
		result.put("records", list);
		return result;
	}
	
	@RequestMapping(value="/status",method=RequestMethod.GET )
	@ResponseBody
	public Object getWfStatus(HttpSession session, HttpServletRequest req){
		String rsWfId = req.getParameter("rsWfId");
		String instNum = req.getParameter("instNum");
//		String refMkid = req.getParameter("refMkid");
		if(StringUtils.isEmpty(instNum) || !NumberUtils.isNumber(instNum)){
			System.err.println("getWfStatus(): invalid instNum="+instNum);
			return "";
		}
		JSONObject result = WfDataUtil.generateWfJson(taskService.getWFStatus(rsWfId, NumberUtils.toInt(instNum)));
		return result;
	}
	
}
