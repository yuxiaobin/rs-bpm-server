package com.xb.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.xb.persistent.RsWorkflow;
import com.xb.persistent.WfDef;
import com.xb.persistent.WfTask;
import com.xb.persistent.WfTaskConn;
import com.xb.persistent.mapper.RsWorkflowMapper;
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
public class RsWorkflowServiceImpl extends CommonServiceImpl<RsWorkflowMapper, RsWorkflow> implements IRsWorkflowService {

	@Autowired
	IWfDefService wfDefService;
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
		RsWorkflow rsWf = this.selectById(module.getRsWfId());
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
		wfTaskService.batchCreateTasksWithAssigners(taskList);
		
		JSONArray connsj = wfData.getJSONArray("conns");
		List<WfTaskConn> connList = WfDataUtil.generateTaskConnList(connsj, wfId, taskList);
		
		wfTaskConnService.insertBatch(connList);
	}

	/**
	 * get workflow for module
	 * 
	 * @param moduleId
	 * @return
	 */
	public WFDetailVO getWF4Module(String rsWfId, String wfId) {
		WFDetailVO result = new WFDetailVO();
		RsWorkflow rsWf = this.selectById(rsWfId);
		if (rsWf == null) {
			return result;
		}
		result.setRsWF(rsWf);
		
		WfDef wfDef  = null;
		if(wfId!=null){
			wfDef = wfDefService.selectById(wfId);
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