package com.xb.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rshare.service.wf.annotations.WfEntityBeanFactory;
import com.xb.base.BaseController;
import com.xb.common.BusinessException;
import com.xb.common.WFConstants;
import com.xb.persistent.RsWorkflow;
import com.xb.persistent.WfCustVars;
import com.xb.service.IRsWorkflowService;
import com.xb.service.IWfCustVarsService;
import com.xb.utils.WfDataUtil;
import com.xb.vo.ModuleVO;
import com.xb.vo.WFDetailVO;

@Controller
@RequestMapping("/wfadmin")
public class WFAdminController extends BaseController {
	
	private static final String WARN_MSG_BUZ_STATUS = "Invalid buzStatus[%s] found for refMkid=%s, parse buzStatus ignored ";
	
	@Autowired
	IRsWorkflowService wfService;
	@Autowired
	WfEntityBeanFactory entityFactory;
	@Autowired
	IWfCustVarsService custVarsService;
	@Autowired
	IRsWorkflowService rsWfService;
	
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
	public Object createWF(@PathVariable String refMkid, @RequestBody JSONObject wfData) throws BusinessException{
		JSONObject result = new JSONObject();
		String invalidVarCode = validateExpression(refMkid, wfData);
		if(invalidVarCode==null){
			ModuleVO module = new ModuleVO();
			module.setRefMkid(refMkid);
			WFDetailVO wfDetail = new WFDetailVO();
			wfDetail.setWfData(wfData);
			wfService.createWF4Module(module, wfDetail);
			result.put("return_code", 0);
		}else{
			result.put("return_code", -1);
			result.put("invalidVarCode", invalidVarCode);
		}
		return result;
	}
	
	private String validateExpression(String refMkid, JSONObject wfData){
		JSONArray funcVars = entityFactory.getFuncVariables(refMkid);
		List<String> expressionList = new ArrayList<String>();
		Set<String> funcVarNames = new HashSet<String>();
		if(funcVars!=null){
			for(int i=0;i<funcVars.size();++i){
				funcVarNames.add(funcVars.getJSONObject(i).getString(WFConstants.FuncVarsParm.PARM_VAR_CODE));
			}
		}
		JSONArray custFuncVarArray = wfData.getJSONArray("custFuncVarArray");
		if(custFuncVarArray!=null){
			for(int i=0;i<custFuncVarArray.size();++i){
				funcVarNames.add(custFuncVarArray.getJSONObject(i).getString(WFConstants.FuncVarsParm.PARM_VAR_CODE));
			}
		}
		
		JSONArray taskArray = wfData.getJSONArray("tasks");
		for(int i=0;i<taskArray.size();++i){
			JSONObject taskj = taskArray.getJSONObject(i);
			if(WFConstants.TaskTypes.C.getTypeDescp().equals(taskj.getString(WfDataUtil.ATTR_TASK_RS_TYPE))){
				expressionList.add(taskj.getString("condExp"));
			}else{
				if (taskj.containsKey("assigners")) {
					String assigners = taskj.getString("assigners");
					JSONArray assignersArray = JSONObject.parseArray(assigners);
					if(assignersArray==null || assignersArray.isEmpty()){
						continue;
					}
					for(int j=0;j<assignersArray.size();++j){
						JSONObject assigner = assignersArray.getJSONObject(j);
						String exeConn = assigner.getString("exeConn");
						if(!StringUtils.isEmpty(exeConn)){
							expressionList.add(exeConn);
						}
					}
				}
			}
		}
		StandardEvaluationContext context = null;
		ExpressionParser parser = new SpelExpressionParser();
		try{
			for (int i = 0; i < expressionList.size(); ++i) {
				context = new StandardEvaluationContext();
				String expression = expressionList.get(i);
				String[] expArray = expression.split(" and | or ");
				for (String exp : expArray) {
					for (String varCode : funcVarNames) {
						Pattern p = Pattern.compile(varCode + "[=><! ]");
						Matcher m = p.matcher(exp);
						if (m.find()) {
							exp = exp.replace(varCode, "#" + varCode);
							context.setVariable(varCode, 1);
						}
					}
					parser.parseExpression(exp).getValue(context);
				}
			}
		}catch(Exception e){
			String message = e.getMessage();
			String invalidVarCodeName = message.substring(message.indexOf("'")+1,message.lastIndexOf("'"));
			log.error("invalid varCode="+invalidVarCodeName);
			return invalidVarCodeName;
		}
		return null;
	}

	@RequestMapping(value="/task", method=RequestMethod.GET )
	public Object viewTaskDtlPopup(HttpServletRequest req){
		String taskStr = req.getParameter("taskData");
		req.setAttribute("taskData", taskStr);
		JSONObject json = JSONObject.parseObject(taskStr);
		if(WFConstants.TaskTypes.C.getTypeDescp().equals(json.getString("taskType"))){
			return "wf-popup-cond";
		}else{
			return "wf-popup-task";
		}
	}
	
	@RequestMapping(value="/buzStatus",method=RequestMethod.GET )
	@ResponseBody
	public Object getBuzStatus(HttpServletRequest req	){
		String refMkid = req.getParameter(PARM_REF_MKID);
		if(StringUtils.isEmpty(refMkid)){
			return new JSONArray(0);
		}
		RsWorkflow res = rsWfService.selectById(refMkid);
		String buzStatusSet = res.getBuzStatusSet();
		if(StringUtils.isEmpty(buzStatusSet)){
			log.warn(String.format(WARN_MSG_BUZ_STATUS, "buzStatusSet is empty",refMkid));
			return new JSONArray(0);
		}
		String[] array = buzStatusSet.split(";");
		JSONArray result = new JSONArray(array.length);
		JSONObject json = null;
		for(String str:array){
			String[] arr = str.split(":");
			if(arr.length!=2){
				log.warn(String.format(WARN_MSG_BUZ_STATUS, str,refMkid));
				continue;
			}
			json = new JSONObject();
			json.put("value", arr[0]);
			json.put("descp", arr[1]);
			result.add(json);
		}
		return result;
	}
	
	/**
	 * 获取某一个工作流的功能变量(实体映射的功能变量 & 自定义变量)
	 * @param parm
	 * @return
	 */
	@RequestMapping(value="/funcvars/all", method=RequestMethod.POST)
	@ResponseBody
	public Object getAllFuncVars(@RequestBody JSONObject parm){
		String refMkid = parm.getString(PARM_REF_MKID);
		String versionStr = parm.getString(PARM_WF_VERSION);
		if(StringUtils.isEmpty(refMkid)){
			return null;
		}
		Integer version = null;
		if(!StringUtils.isEmpty(versionStr)){
			try{
				version = Integer.parseInt(versionStr);
			}catch(NumberFormatException e){
				log.warn(String.format(ERROR_MSG_INVALID_VERSION, versionStr,refMkid));
			}
		}
		JSONArray funcVarsArray = getFuncVarsFromEntityAnnotation(refMkid);
		List<WfCustVars> custVarList = custVarsService.getCustVars(refMkid, version);
		if(custVarList!=null && !custVarList.isEmpty()){
			JSONObject record = null;
			for(WfCustVars var:custVarList){
				record = new JSONObject();
				record.put(PARM_VAR_CODE, var.getVarCode());
				record.put(PARM_VAR_DESCP, var.getVarDescp());
				funcVarsArray.add(record);
			}
		}
		return funcVarsArray;
	}
	/**
	 * 获取某一个工作流的功能变量(实体映射的功能变量)
	 * 
	 * 采取自定义变量从页面上获取。预定义变量从这个action获取
	 * @param parm
	 * @return
	 */
	@RequestMapping(value="/funcvars", method=RequestMethod.POST)
	@ResponseBody
	public Object getFuncVars(@RequestBody JSONObject parm){
		String refMkid = parm.getString(PARM_REF_MKID);
		if(StringUtils.isEmpty(refMkid)){
			return null;
		}
		return getFuncVarsFromEntityAnnotation(refMkid);
	}
	
	private JSONArray getFuncVarsFromEntityAnnotation(String refMkid){
		return entityFactory.getFuncVariables(refMkid);
	}
	
	private static final String ERROR_MSG_INVALID_VERSION = "getCustVars(): invalid version[%s] with refMikd=%s";
	
	private static final String PARM_VAR_TYPE = WFConstants.FuncVarsParm.PARM_VAR_TYPE;
	private static final String PARM_VAR_CODE = WFConstants.FuncVarsParm.PARM_VAR_CODE;
	private static final String PARM_VAR_DESCP = WFConstants.FuncVarsParm.PARM_VAR_DESCP;
	private static final String PARAM_VAR_EXPRESSION = WFConstants.FuncVarsParm.PARAM_VAR_EXPRESSION;
	
	/**
	 * 获取用户自定义功能变量
	 * @param parm
	 * @return
	 */
	@RequestMapping(value="/custvars", method=RequestMethod.POST)
	@ResponseBody
	public Object getCustVars(@RequestBody JSONObject parm){
		String refMkid = parm.getString(PARM_REF_MKID);
		String versionStr = parm.getString(PARM_WF_VERSION);
		if(StringUtils.isEmpty(refMkid)){
			return null;
		}
		Integer version = null;
		if(!StringUtils.isEmpty(versionStr)){
			try{
				version = Integer.parseInt(versionStr);
			}catch(NumberFormatException e){
				log.warn(String.format(ERROR_MSG_INVALID_VERSION, versionStr,refMkid));
			}
		}
		List<WfCustVars> custVarList = custVarsService.getCustVars(refMkid, version);
		if(custVarList==null || custVarList.isEmpty()){
			return "[]";
		}
		JSONArray result = new JSONArray(custVarList.size());
		JSONObject record = null;
		for(WfCustVars var:custVarList){
			record = new JSONObject();
			record.put(PARM_VAR_TYPE, var.getVarType());
			record.put(PARM_VAR_CODE, var.getVarCode());
			record.put(PARM_VAR_DESCP, var.getVarDescp());
			record.put(PARAM_VAR_EXPRESSION, var.getVarExpression());
			result.add(record);
		}
		return result;
	}
	
}
