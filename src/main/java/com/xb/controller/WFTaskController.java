package com.xb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xb.base.BaseController;
import com.xb.common.WFConstants;
import com.xb.persistent.WfTask;
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
			TaskOptVO optVO = new TaskOptVO();
			optVO.setRsWfId(rsWfId);
			optVO.setInstNum(Integer.parseInt(instNum));
			optVO.setRefMkid(refMkid);
			optVO.setOptCode(optCode);
			req.setAttribute("TX_PR_CHOICES",taskService.getCurrentTaskByRefNum(optVO).getTxPrChoices());
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
		WfTask task = taskService.getCurrentTaskByRefNum(optVO);
		JSONArray result = new JSONArray();
		if(task!=null){
			//第1组菜单
			JSONArray group1 = new JSONArray();
			JSONObject option = new JSONObject();
			option.put("value", "commit");
			option.put("disflag", false);//A:active, I:inactive
			group1.add(option);
			JSONObject choices = JSONObject.parseObject(task.getTxChoices());
			if(choices!=null){
				Boolean allowGoBack = choices.getBoolean("AllowGoBack");
				option = new JSONObject();
				option.put("value", "back");
				if(allowGoBack==null || !allowGoBack){
//					option.put("disflag", true);//not returned
				}else{
					option.put("disflag", false);
					group1.add(option);
				}
				
				Boolean allowVeto = choices.getBoolean("AllowVeto");
				option = new JSONObject();
				option.put("value", "veto");
				if(allowVeto==null || !allowVeto){
//					option.put("disflag", true);
				}else{
					option.put("disflag", false);
					group1.add(option);
				}
				result.add(group1);
				//第二组菜单
				group1 = new JSONArray();
				option = new JSONObject();
				option.put("value", "forward");
				option.put("disflag", false);
				group1.add(option);
				
				Boolean allowReCall = choices.getBoolean("AllowReCall");
				option = new JSONObject();
				option.put("value", "recall");
				if(allowReCall==null || !allowReCall){
//					option.put("disflag", true);
				}else{
					option.put("disflag", false);
					group1.add(option);
				}
				
				option = new JSONObject();
				option.put("value", "letmedo");
				option.put("disflag", false);
				group1.add(option);
				result.add(group1);
				//第三组菜单
				group1 = new JSONArray();
				option = new JSONObject();
				option.put("value", "dispatch");
				option.put("disflag", true);
				group1.add(option);
				option = new JSONObject();
				option.put("value", "track");
				option.put("disflag", false);
				group1.add(option);
				result.add(group1);
			}
		}
		return result;
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
		WfTask task = taskService.getCurrentTaskByRefNum(optVO);
		JSONArray result = new JSONArray();
		if(task!=null){
			JSONObject option = new JSONObject();
			option.put("value", WFConstants.OptTypes.COMMIT);
			option.put("disflag", false);//A:active, I:inactive
			result.add(option);
			JSONObject choices = JSONObject.parseObject(task.getTxChoices());
			if(choices!=null){
				Boolean allowGoBack = choices.getBoolean("AllowGoBack");
				option = new JSONObject();
				option.put("value", WFConstants.OptTypes.REJECT);
				if(allowGoBack==null || !allowGoBack){
//					option.put("disflag", true);//not returned
				}else{
					option.put("disflag", false);
					result.add(option);
				}
				Boolean allowVeto = choices.getBoolean("AllowVeto");
				option = new JSONObject();
				option.put("value", WFConstants.OptTypes.VETO);
				if(allowVeto==null || !allowVeto){
//					option.put("disflag", true);
				}else{
					option.put("disflag", false);
					result.add(option);
				}
				option = new JSONObject();
				option.put("value", WFConstants.OptTypes.FORWARD);
				option.put("disflag", false);
				result.add(option);
				
				Boolean allowReCall = choices.getBoolean("AllowReCall");
				option = new JSONObject();
				option.put("value", WFConstants.OptTypes.RECALL);
				if(allowReCall==null || !allowReCall){
//					option.put("disflag", true);
				}else{
					option.put("disflag", false);
					result.add(option);
				}
				option = new JSONObject();
				option.put("value", WFConstants.OptTypes.LET_ME_DO);
				option.put("disflag", false);
				result.add(option);
				option = new JSONObject();
				option.put("value", WFConstants.OptTypes.DISPATCH);
				option.put("disflag", true);
				result.add(option);
				option = new JSONObject();
				option.put("value", WFConstants.OptTypes.TRACK);
				option.put("disflag", false);
				result.add(option);
			}
		}
		return result;
	}
	
}
