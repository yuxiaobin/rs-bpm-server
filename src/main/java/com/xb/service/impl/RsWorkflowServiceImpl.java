package com.xb.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.xb.common.WFConstants;
import com.xb.persistent.RsWorkflow;
import com.xb.persistent.WfCustVars;
import com.xb.persistent.WfTask;
import com.xb.persistent.WfTaskConn;
import com.xb.persistent.WfVersion;
import com.xb.persistent.mapper.RsWorkflowMapper;
import com.xb.service.IRsWorkflowService;
import com.xb.service.IWfCustVarsService;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfInstanceService;
import com.xb.service.IWfTaskConnService;
import com.xb.service.IWfTaskService;
import com.xb.service.IWfVersionService;
import com.xb.utils.WfDataUtil;
import com.xb.vo.ModuleVO;
import com.xb.vo.WFDetailVO;

/**
 *
 * RsWorkflow 表数据服务层接口实现类
 *
 */
@Service
public class RsWorkflowServiceImpl extends CommonServiceImpl<RsWorkflowMapper, RsWorkflow> implements IRsWorkflowService {

	@Autowired
	IWfVersionService wfDefService;
	@Autowired
	IWfTaskService wfTaskService;
	@Autowired
	IWfTaskConnService wfTaskConnService;
	@Autowired
	IWfInstanceService wfInstService;
	@Autowired
	IWfInstHistService wfInstHistService;
	@Autowired
	IWfCustVarsService custVarsService;

	/**
	 * Create Workflow related for module.
	 * 
	 * @param module
	 * @param wfDetail
	 */
	@Transactional
	public void createWF4Module(ModuleVO module, WFDetailVO wfDetail) {
		String refMkid = module.getRefMkid();
		WfVersion wfDefParm = new WfVersion();
		wfDefParm.setRefMkid(refMkid);
		List<WfVersion> wfDefList = wfDefService.selectList(wfDefParm, "version desc");
		WfVersion wfDef = null;
		if(wfDefList==null || wfDefList.isEmpty()){
			wfDef = new WfVersion();
			wfDef.setRefMkid(refMkid);
			wfDef.setVERSION(1);
			wfDefService.insert(wfDef);
		}else{
			WfVersion oldWfDef = wfDefList.get(0);
			wfDef = new WfVersion();
			wfDef.setRefMkid(refMkid);
			wfDef.setVERSION(oldWfDef.getVERSION()+1);
			wfDefService.insert(wfDef);
		}
		String wfId = wfDef.getWfId();
		JSONObject wfData = wfDetail.getWfData();
		JSONArray tasksj = wfData.getJSONArray("tasks");
		List<WfTask> taskList = WfDataUtil.generateTaskList(tasksj, wfId);
		wfTaskService.batchCreateTasksWithAssigners(taskList);
		
		JSONArray connsj = wfData.getJSONArray("conns");
		List<WfTaskConn> connList = WfDataUtil.generateTaskConnList(connsj, wfId, taskList);
		
//		wfTaskConnService.insertBatch(connList);//oracle not support: insert into tableA(a) values(1),(2),(3);
		for(WfTaskConn con:connList){
			wfTaskConnService.insert(con);
		}
		
		JSONArray custFuncVarArray = wfData.getJSONArray("custFuncVarArray");
		if(custFuncVarArray==null || custFuncVarArray.isEmpty()){
			return;
		}
		JSONObject jsn = null;
		WfCustVars custVar = null;
		for(int i=0;i<custFuncVarArray.size();++i){
			jsn = custFuncVarArray.getJSONObject(i);
			custVar = new WfCustVars();
			custVar.setWfId(wfId);
			custVar.setVarCode(jsn.getString(WFConstants.FuncVarsParm.PARM_VAR_CODE));
			custVar.setVarType(jsn.getString(WFConstants.FuncVarsParm.PARM_VAR_TYPE));
			custVar.setVarDescp(jsn.getString(WFConstants.FuncVarsParm.PARM_VAR_DESCP));
			custVar.setVarExpression(jsn.getString(WFConstants.FuncVarsParm.PARAM_VAR_EXPRESSION));
			custVarsService.insert(custVar);
		}
	}

	/**
	 * get workflow for module
	 * 
	 * @param moduleId
	 * @return
	 */
	public WFDetailVO getWF4Module(String refMkid, String wfId) {
		WFDetailVO result = new WFDetailVO();
		WfVersion wfDef  = null;
		if(wfId!=null){
			wfDef = wfDefService.selectById(wfId);
		}
		if(wfDef==null){
			WfVersion wfDefParm = new WfVersion();
			wfDefParm.setRefMkid(refMkid);
			List<WfVersion> wfDefList = wfDefService.selectList(wfDefParm, "version desc");
			if (wfDefList == null || wfDefList.isEmpty()) {
				return result;
			}
			wfDef = wfDefList.get(0);
		}
		if(wfDef==null){//first time define workflow
			return result;
		}
		result.setWfDef(wfDef);
		wfId = wfDef.getWfId();
		WfTaskConn connParm = new WfTaskConn();
		connParm.setWfId(wfId);
		result.setConns(wfTaskConnService.selectList(connParm));
		result.setTasks(wfTaskService.selectTasksWithAssigners(wfId));
		return result;
	}
	
}