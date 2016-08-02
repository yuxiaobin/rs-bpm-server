package com.xb.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.xb.persistent.WfDef;
import com.xb.persistent.WfInstHist;
import com.xb.service.IRsModuleService;
import com.xb.service.IRsWorkflowService;
import com.xb.service.IWfDefService;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfTaskService;
import com.xb.utils.WfDataUtil;
import com.xb.vo.ModuleVO;
import com.xb.vo.TaskVO;
import com.xb.vo.WFDetailVO;

@Controller
@RequestMapping("/wf")
public class WFController extends BaseController {
	
	private static final String SESSION_USERINFO = "USERINFO";
	
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
	
	@RequestMapping("/")
	public String hello(HttpSession session){
		return "hello";
	}
	@RequestMapping("")
	public String hello2(HttpSession session){
		return "hello";
	}
	
	@RequestMapping("/{roleName}")
	public String viewWf(@PathVariable String roleName, HttpSession session){
		session.setAttribute(SESSION_USERINFO,roleName);
		if("admin".equals(roleName)){
			return "redirect:/wf/admin/modules";
		}
		return "wf-"+roleName;
	}
	
	@RequestMapping("/admin/modules")
	public String home(HttpSession session){
		String userId = (String) session.getAttribute(SESSION_USERINFO);
		if(userId==null){
			session.setAttribute(SESSION_USERINFO,"system");
		}
		return "wf-modules";
	}
	
//	@RequestMapping("/index/{userId}")
//	public String userIndex(@PathVariable String userId, HttpSession session){
//		session.setAttribute(SESSION_USERINFO,userId);
//		return "wf-index";
//	}
	
	@RequestMapping(value="/admin/module",method=RequestMethod.POST )
	@ResponseBody
	public Object createModule(@RequestBody ModuleVO moduleVO){
		RsModule module = new RsModule();
		module.setNAME(moduleVO.getModuleName());
		module.setCreatedBy("system");
		module.setCreatedDt(new Date());
		moduleService.insert(module);
		moduleVO.setModuleId(String.valueOf(module.getModId()));
		return moduleVO;
	}
	
	@RequestMapping(value="/modules/list",method=RequestMethod.GET )
	@ResponseBody
	public Object getAllModules(){
		RsModule parm = new RsModule();
//		Page<RsModule> page = new Page<>(0, 100);
		JSONObject page = new JSONObject();
		List<RsModule> list = moduleService.selectList(parm);
		page.put("records", list);
//		page.setRecords(list);
//		page.setTotal(list.size());
		return page;
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
	
	@RequestMapping("/admin/define/{moduleId}")
	public String viewWf4Module(@PathVariable String moduleId, HttpSession session, HttpServletRequest req){
		if(StringUtils.isEmpty(moduleId)){
			System.out.println("viewWf4Module(): moduleId is empty");
			return "";
		}
		RsModule moduleParm = new RsModule();
		moduleParm.setModId(moduleId);
		RsModule module = moduleService.selectOne(moduleParm);
		session.setAttribute("moduleId", moduleId);
		req.setAttribute("moduleName", module.getNAME());
		return "wf-define";
	}
	
	@RequestMapping(value="/module/{moduleId}/wf/{wfId}",method=RequestMethod.GET )
	@ResponseBody
	public Object getWfDtl4Module(@PathVariable String moduleId, @PathVariable String wfId, HttpSession session){
		if(StringUtils.isEmpty(moduleId)){
			System.out.println("viewWf4Module(): moduleId is empty");
			return "";
		}
		System.out.println("=======moduleId="+moduleId+", =========wfId="+wfId);
		if(wfId=="init"){
			wfId = null;
		}
		JSONObject result = WfDataUtil.generateWfJson(wfService.getWF4Module(moduleId,wfId));
		result.put("role", session.getAttribute(SESSION_USERINFO));
		return result;
	}
	
	@RequestMapping(value="/admin/module/{moduleId}/wf",method=RequestMethod.POST )
	@ResponseBody
	public Object createWF(@PathVariable String moduleId, @RequestBody JSONObject wfData){
		System.out.println(wfData);
		ModuleVO module = new ModuleVO();
		module.setModuleId(moduleId);
		module.setModuleName("moduleName:TODO");//TODO:
		WFDetailVO wfDetail = new WFDetailVO();
		wfDetail.setWfData(wfData);
		wfService.createWF4Module(module, wfDetail);
		return wfData;
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
		String userId = (String) session.getAttribute(SESSION_USERINFO);
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
		String userId = (String) session.getAttribute(SESSION_USERINFO);
		if(userId==null){
			userId = "system";
		}
		List<TaskVO> taskvList = taskService.getTasksInbox(userId);
		JSONObject result = new JSONObject();
		result.put("records", taskvList);
		return result;
	}
	
	@RequestMapping(value="/history/{histId}", method=RequestMethod.GET )
	public String viewTaskHist(@PathVariable String histId, HttpServletRequest req){
		WfInstHist histParm = new WfInstHist();
		histParm.setHistId(histId);
		WfInstHist hist = instHistService.selectOne(histParm);
		if(hist==null){
			System.err.println("viewTaksHist(): hist is null for histId="+histId);
			return "";
		}
		String wfId = hist.getWfId();
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
		req.setAttribute("histId", histId);
		return "wf-view";
	}
	
	@RequestMapping(value="/history/{histId}", method=RequestMethod.POST )
	@ResponseBody
	public Object updateTaskHist(@PathVariable String histId,@RequestBody JSONObject obj, HttpSession session){
		String userId = (String) session.getAttribute(SESSION_USERINFO);
		String opt = obj.getString("opt");
		taskService.processTask(histId, userId, opt);
		JSONObject result = new JSONObject();
		result.put("message", "success");
		return result;
	}
	
	@RequestMapping(value="/inbox" )
	public String viewInbox(HttpSession session){
		String userId = (String) session.getAttribute(SESSION_USERINFO);
		if("staff".equals(userId)){//TODO: should use role
			return "wf-staff";
		}else if("manager".equalsIgnoreCase(userId)){
			return "wf-manager";
		}
		return "/";
	}
	
	@RequestMapping(value="/modules/page" )
	public String modulesPage(HttpSession session, HttpServletRequest req){
		String userId = (String) session.getAttribute(SESSION_USERINFO);
//		if("staff".equals(userId)){//TODO: should use role
//			return "wf-staff";
//		}else if("manager".equalsIgnoreCase(userId)){
//			return "wf-manager";
//		}
//		return "/";
		req.setAttribute("role", userId);
		return "wf-modules-view";
	}
	
	@RequestMapping(value="/view/status/{histId}",method=RequestMethod.GET )
	@ResponseBody
	public Object getWfStatus(@PathVariable String histId, HttpSession session){
		if(StringUtils.isEmpty(histId)){
			return "";
		}
		JSONObject result = WfDataUtil.generateWfJson(taskService.getWFStatus(histId));
		result.put("role", session.getAttribute(SESSION_USERINFO));
		return result;
	}
}
