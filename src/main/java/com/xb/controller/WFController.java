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
import org.springframework.web.bind.annotation.RequestBody;
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
import com.xb.vo.TaskOptVO;

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
	
	
	@RequestMapping(value="/start", method=RequestMethod.POST)
	@ResponseBody
	public Object startWf4Module(@RequestBody TaskOptVO optVO, HttpSession session){
		taskService.startWF4Module(optVO.getModuleId(),optVO.getRsWfId(), getCurrUserId(session));
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
		String rsWfId = req.getParameter("rsWfId");
		String instNum = req.getParameter("instNum");
		if(StringUtils.isEmpty(instNum) || !NumberUtils.isNumber(instNum)){
			System.err.println("getWfStatus(): invalid instNum="+instNum);
			return "";
		}
		int instNumber = NumberUtils.toInt(instNum);
		JSONObject result = new JSONObject();
		List<WfInstHist> list = instHistService.viewWfInstHistory(rsWfId, instNumber);
		
		String userId = getCurrUserId(session);
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
	/**********************Workflow Track * end ********************/

}
