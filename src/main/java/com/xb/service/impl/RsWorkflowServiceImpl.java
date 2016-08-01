package com.xb.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.xb.common.WFConstants;
import com.xb.persistent.RsModule;
import com.xb.persistent.RsWorkflow;
import com.xb.persistent.WfDef;
import com.xb.persistent.WfInstHist;
import com.xb.persistent.WfInstance;
import com.xb.persistent.WfTask;
import com.xb.persistent.WfTaskConn;
import com.xb.persistent.mapper.RsWorkflowMapper;
import com.xb.service.IRsModuleService;
import com.xb.service.IRsWorkflowService;
import com.xb.service.IWfDefService;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfInstanceService;
import com.xb.service.IWfTaskConnService;
import com.xb.service.IWfTaskService;
import com.xb.utils.WfDataUtil;
import com.xb.vo.ModuleVO;
import com.xb.vo.WFDetailVO;

/**
 *
 * RsWorkflow 表数据服务层接口实现类
 *
 */
@Service
public class RsWorkflowServiceImpl extends SuperServiceImpl<RsWorkflowMapper, RsWorkflow> implements IRsWorkflowService {

	@Autowired
	IWfDefService wfDefService;
	@Autowired
	IRsModuleService rsModuleService;
	@Autowired
	IWfTaskService wfTaskService;
	@Autowired
	IWfTaskConnService wfTaskConnService;
	@Autowired
	IWfInstanceService wfInstService;
	@Autowired
	IWfInstHistService wfInstHistService;

	/**
	 * Create Workflow related for module.
	 * 
	 * @param module
	 * @param wfDetail
	 */
	@Transactional
	public void createWF4Module(ModuleVO module, WFDetailVO wfDetail) {
		RsModule modParm = new RsModule();
		modParm.setModId(module.getModuleId());
		RsModule rsModule = rsModuleService.selectOne(modParm);
		RsWorkflow rsWf = null;
		if(!StringUtils.isEmpty(rsModule.getRsWfId())){
			RsWorkflow parm = new RsWorkflow();
			parm.setRsWfId(rsModule.getRsWfId());
			rsWf = this.selectOne(parm);
		}else{
			rsWf = new RsWorkflow();
			rsWf.setRsWfName("Workflow for "+module.getModuleName());
			this.insert(rsWf);
			rsModule.setRsWfId(rsWf.getRsWfId());
			rsModule.setWfFlag("T");
			rsModuleService.updateById(rsModule);
		}
		WfDef wfDefParm = new WfDef();
		wfDefParm.setRsWfId(rsWf.getRsWfId());
		List<WfDef> wfDefList = wfDefService.selectList(wfDefParm, "version desc");
		WfDef wfDef = null;
		if(wfDefList==null || wfDefList.isEmpty()){
			wfDef = new WfDef();
			wfDef.setRsWfId(rsWf.getRsWfId());
			wfDef.setVERSION(1);
			wfDefService.insert(wfDef);
		}else{
			WfDef oldWfDef = wfDefList.get(0);
			wfDef = new WfDef();
			wfDef.setRsWfId(rsWf.getRsWfId());
			wfDef.setVERSION(oldWfDef.getVERSION()+1);
			wfDefService.insert(wfDef);
		}
		String wfId = wfDef.getWfId();
		JSONObject wfData = wfDetail.getWfData();
		JSONArray tasksj = wfData.getJSONArray("tasks");
		List<WfTask> taskList = WfDataUtil.generateTaskList(tasksj, wfId);
		/*if(taskList!=null){
			for(WfTask task:taskList){
				wfTaskService.insert(task);
			}
		}*/
		wfTaskService.insertBatch(taskList);
		
		JSONArray connsj = wfData.getJSONArray("conns");
		List<WfTaskConn> connList = WfDataUtil.generateTaskConnList(connsj, wfId, taskList);
		/*if(connList!=null){
			for(WfTaskConn conn:connList){
				wfTaskConnService.insert(conn);
			}
		}*/
		
		wfTaskConnService.insertBatch(connList);
	}

	/**
	 * get workflow for module
	 * 
	 * @param moduleId
	 * @return
	 */
	public WFDetailVO getWF4Module(String moduleId, String wfId) {
		WFDetailVO result = new WFDetailVO();
		RsModule modParm = new RsModule();
		modParm.setModId(moduleId);
		RsModule rsModule = rsModuleService.selectOne(modParm);
		if (rsModule == null) {
			return result;
		}
		if (StringUtils.isEmpty(rsModule.getRsWfId())) {
			return result;
		}
		RsWorkflow rsWf =null;
		RsWorkflow parm = new RsWorkflow();
		parm.setRsWfId(rsModule.getRsWfId());
		rsWf = this.selectOne(parm);
		
		if (rsWf == null) {
			return result;
		}
		result.setRsWF(rsWf);
		
		WfDef wfDef  = null;
		if(wfId!=null){
			WfDef wfDefParm = new WfDef();
			wfDefParm.setWfId(wfId);
			wfDef = wfDefService.selectOne(wfDefParm);
		}
		if(wfDef==null){
			WfDef wfDefParm = new WfDef();
			wfDefParm.setRsWfId(rsWf.getRsWfId());
			List<WfDef> wfDefList = wfDefService.selectList(wfDefParm, "version desc");
			if (wfDefList == null || wfDefList.isEmpty()) {
				return result;
			}
			wfDef = wfDefList.get(0);
		}
		result.setWfDef(wfDef);
		WfTask taskParm = new WfTask();
		taskParm.setWfId(wfDef.getWfId());
		List<WfTask> taskList = wfTaskService.selectList(taskParm);
		WfTaskConn connParm = new WfTaskConn();
		connParm.setWfId(wfDef.getWfId());
		List<WfTaskConn>connList = wfTaskConnService.selectList(connParm);
		result.setTasks(taskList);
		result.setConns(connList);
		return result;
	}
	
	@Transactional
	public void startWF4Module(String moduleId, String currUserId){
		RsModule modParm = new RsModule();
		modParm.setModId(moduleId);
		RsModule rsModule = rsModuleService.selectOne(modParm);
		if (rsModule == null) {
			return;
		}
		if (StringUtils.isEmpty(rsModule.getRsWfId())) {
			return;
		}
		WfDef wfDefParm = new WfDef();
		wfDefParm.setRsWfId(rsModule.getRsWfId());
		List<WfDef> wfDefList = wfDefService.selectList(wfDefParm, "version desc");
		if (wfDefList == null || wfDefList.isEmpty()) {
			return;
		}
		String wfId = wfDefList.get(0).getWfId();
		WfInstance wfInst = new WfInstance();
		wfInst.setWfId(wfId);
		wfInst.setWfStatus("I");
		wfInstService.insert(wfInst);
		
		WfTask taskParm = new WfTask();
		taskParm.setWfId(wfId);
		List<WfTask> taskList = wfTaskService.selectList(taskParm);
		WfTask startTask = null;
		for(WfTask task:taskList){
			if(WFConstants.TaskTypes.S.getTypeCode().equals(task.getTaskType())){
				startTask = task;
				break;
			}
		}
		WfInstHist hist = new WfInstHist();
		hist.setInstId(wfInst.getInstId());
		hist.setWfId(wfId);
		hist.setTaskId(startTask.getTaskId());
		hist.setOptSeq(1);
		hist.setOptUser(currUserId);
		hist.setOptType("R");//R:Request
		hist.setSTATUS("I");
		wfInstHistService.insert(hist);
	}
	
}