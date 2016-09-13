package com.xb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xb.base.BaseController;
import com.xb.common.BusinessException;
import com.xb.common.WFConstants;
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
		JSONObject result = new JSONObject();
		try {
			taskService.processTask(optVO, getCurrUserId(session));
			result.put("message", "success");
		} catch (BusinessException e) {
			result.put("message", e.getErrorMsg());
		}
		return result;
	}
	
	
	@RequestMapping("/loadprocess")
	public Object loadProcessTask(HttpSession session, HttpServletRequest req){
		
		String rsWfId = req.getParameter("rsWfId");
		String instNum = req.getParameter("instNum");
		String refMkid = req.getParameter("refMkid");
		req.setAttribute("rsWfId", rsWfId);
		req.setAttribute("instNum", instNum);
		req.setAttribute("refMkid", refMkid);
		String optCode = req.getParameter("optCode");
		if(WFConstants.OptTypes.TRACK.equals(optCode)){
			return "wf-popup-track";
		}else{
			req.setAttribute("optCode", optCode);
			//提交，退回，否决等操作事务页面
			TaskOptVO optVO = new TaskOptVO();
			optVO.setRsWfId(rsWfId);
			optVO.setInstNum(Integer.parseInt(instNum));
			optVO.setOptCode(optCode);
			req.setAttribute("TX_PR_CHOICES",taskService.getCurrentTaskByRefNum(optVO).getTxPrChoices());
			return "wf-popup-opt";
		}
	}
	
	@RequestMapping("/next/tasks")
	@ResponseBody
	public Object getNextTasks(@RequestBody TaskOptVO optVO, HttpSession session){
		String currUserId = getCurrUserId(session);
		if(!StringUtils.isEmpty(currUserId)){
			optVO.setCurrUserId(currUserId);
		}
		JSONObject result = new JSONObject();
		result.put("records", taskService.getNextTasksByOptCode(optVO));
		return result;
	}
	
	@RequestMapping("/next/usergroups")
	@ResponseBody
	public Object getNextAssigners(@RequestBody TaskOptVO optVO, HttpSession session){
		String currUserId = getCurrUserId(session);
		if(!StringUtils.isEmpty(currUserId)){
			optVO.setCurrUserId(currUserId);
		}
		JSONObject result = new JSONObject();
		result.put("result", taskService.getNextAssignersByOptCode(optVO));
		return result;
	}
	
	@RequestMapping(value="/options",method=RequestMethod.GET )
	@ResponseBody
	public Object getTaskOptions(HttpServletRequest req	){
		TaskOptVO optVO = new TaskOptVO();
		String rsWfId = req.getParameter("rsWfId");
		String instNum = req.getParameter("instNum");
		if(NumberUtils.isNumber(instNum)){
			optVO.setInstNum(NumberUtils.toInt(instNum));
		}
		else{
			System.err.println("getTaskOptions(): invalid instNum="+instNum);
			return new JSONArray();
		}
		optVO.setRsWfId(rsWfId);
		return taskService.getTaskOptions(optVO, true);
	}
	
	@RequestMapping(value="/options/nogroup",method=RequestMethod.GET )
	@ResponseBody
	public Object getTaskOptionsNoGroup(HttpServletRequest req	){
		TaskOptVO optVO = new TaskOptVO();
		String rsWfId = req.getParameter("rsWfId");
		String instNum = req.getParameter("instNum");
		if(NumberUtils.isNumber(instNum)){
			optVO.setInstNum(NumberUtils.toInt(instNum));
		}
		else{
			System.err.println("getTaskOptions(): invalid instNum="+instNum);
			return new JSONArray();
		}
		optVO.setRsWfId(rsWfId);
		return taskService.getTaskOptions(optVO, false);
	}
	
}
