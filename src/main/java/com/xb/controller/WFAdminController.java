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
import com.xb.service.IWfDefService;
import com.xb.utils.WfDataUtil;
import com.xb.vo.ModuleVO;
import com.xb.vo.WFDetailVO;

@Controller
@RequestMapping("/wfadmin")
public class WFAdminController extends BaseController {
	
	@Autowired
	IRsWorkflowService wfService;
	@Autowired
	IWfDefService wfDefService;
	
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
		wf.setGnmkId(moduleVO.getGnmkId());
		List<RsWorkflow> list = wfService.selectList(wf);
		if(list!=null && !list.isEmpty()){
			moduleVO.setStatusCode("dup");
			return moduleVO;
		}
		wfService.insert(wf);
		moduleVO.setRsWfId(wf.getRsWfId());
		return moduleVO;
	}
	
	@RequestMapping(value="/modules/list",method=RequestMethod.GET )
	@ResponseBody
	public Object getAllModules(){
		JSONObject page = new JSONObject();
		page.put("records", wfService.selectList(new RsWorkflow()));
		return page;
	}
	
	@RequestMapping("/define/{rsWfId}")
	public String viewWf4Module(@PathVariable String rsWfId, HttpSession session, HttpServletRequest req) throws Exception{
		if(StringUtils.isEmpty(rsWfId)){
			System.out.println("viewWf4Module(): rsWfId is empty");
			return "";
		}
		RsWorkflow wf = wfService.selectById(rsWfId);
		if(wf==null){
			throw new Exception("No record found");
		}
		req.setAttribute("rsWfId", rsWfId);
		req.setAttribute("gnmkId", wf.getGnmkId());
		return "wf-define";
	}
	
	@RequestMapping(value="/module/{rsWfId}/wf",method=RequestMethod.GET )
	@ResponseBody
	public Object getWfDtl4Module(@PathVariable String rsWfId, HttpSession session){
		if(StringUtils.isEmpty(rsWfId)){
			System.out.println("viewWf4Module(): rsWfId is empty");
			return "";
		}
		return WfDataUtil.generateWfJson(wfService.getWF4Module(rsWfId,null));
	}
	
	@RequestMapping(value="/module/{rsWfId}/wf",method=RequestMethod.POST )
	@ResponseBody
	public Object createWF(@PathVariable String rsWfId, @RequestBody JSONObject wfData){
		ModuleVO module = new ModuleVO();
		module.setRsWfId(rsWfId);
		WFDetailVO wfDetail = new WFDetailVO();
		wfDetail.setWfData(wfData);
		wfService.createWF4Module(module, wfDetail);
		return wfData;
	}
	

	@RequestMapping(value="/task", method=RequestMethod.GET )
	public Object viewTaskDtlPopup(HttpServletRequest req){
		String taskStr = req.getParameter("taskData");
		req.setAttribute("taskData", taskStr);
		return "wf-popup-task";
	}
}
