package com.xb.controller;

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
import com.xb.persistent.RsWorkflow;
import com.xb.service.IRsWorkflowService;
import com.xb.utils.WfDataUtil;
import com.xb.vo.ModuleVO;
import com.xb.vo.WFDetailVO;

@Controller
@RequestMapping("/wfadmin")
public class WFAdminController extends BaseController {
	
	@Autowired
	IRsWorkflowService wfService;
	
	@RequestMapping("/")
	public String hello(HttpSession session){
		return home(session);
	}
	@RequestMapping("")
	public String hello2(HttpSession session){
		return home(session);
	}
	
	@RequestMapping("/modules")
	public String home(HttpSession session){
		return "wf-modules";
	}
	
	@RequestMapping(value="/module",method=RequestMethod.POST )
	@ResponseBody
	public Object createModule(@RequestBody ModuleVO moduleVO){
		RsWorkflow wf = new RsWorkflow();
		wf.setRefMkid(moduleVO.getRefMkid());
		List<RsWorkflow> list = wfService.selectList(wf);
		if(list!=null && !list.isEmpty()){
			moduleVO.setStatusCode("dup");
			return moduleVO;
		}
		wfService.insert(wf);
		return moduleVO;
	}
	
	@RequestMapping(value="/modules/list",method=RequestMethod.GET )
	@ResponseBody
	public Object getAllModules(){
		JSONObject page = new JSONObject();
		page.put("records", wfService.selectList(new RsWorkflow()));
		return page;
	}
	
	@RequestMapping("/define/{refMkid}")
	public String viewWf4Module(@PathVariable String refMkid, HttpSession session, HttpServletRequest req) throws Exception{
		if(StringUtils.isEmpty(refMkid)){
			System.out.println("viewWf4Module(): refMkid is empty");
			return "";
		}
		req.setAttribute(PARM_REF_MKID, refMkid);
		return "wf-define";
	}
	
	@RequestMapping(value="/module/{refMkid}/wf",method=RequestMethod.GET )
	@ResponseBody
	public Object getWfDtl4Module(@PathVariable String refMkid, HttpSession session){
		if(StringUtils.isEmpty(refMkid)){
			System.out.println("viewWf4Module(): refMkid is empty");
			return "";
		}
		return WfDataUtil.generateWfJson(wfService.getWF4Module(refMkid,null));
	}
	
	@RequestMapping(value="/module/{refMkid}/wf",method=RequestMethod.POST )
	@ResponseBody
	public Object createWF(@PathVariable String refMkid, @RequestBody JSONObject wfData){
		ModuleVO module = new ModuleVO();
		module.setRefMkid(refMkid);
		WFDetailVO wfDetail = new WFDetailVO();
		wfDetail.setWfData(wfData);
		wfService.createWF4Module(module, wfDetail);
		JSONObject result = new JSONObject();
		result.put("return_code", 0);
		return result;
	}
	

	@RequestMapping(value="/task", method=RequestMethod.GET )
	public Object viewTaskDtlPopup(HttpServletRequest req){
		String taskStr = req.getParameter("taskData");
		req.setAttribute("taskData", taskStr);
		return "wf-popup-task";
	}
}
