package com.xb.vo;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.xb.persistent.RsWorkflow;
import com.xb.persistent.WfTask;
import com.xb.persistent.WfTaskConn;
import com.xb.persistent.WfVersion;

public class WFDetailVO {
	
	private JSONObject wfData;

	private RsWorkflow rsWF;
	private WfVersion wfDef;// latest record > max Version
//	private RsModule module;

	List<WfTask> tasks;
	List<WfTaskConn> conns;

	public RsWorkflow getRsWF() {
		return rsWF;
	}

	public void setRsWF(RsWorkflow rsWF) {
		this.rsWF = rsWF;
	}

	
	public WfVersion getWfDef() {
		return wfDef;
	}

	public void setWfDef(WfVersion wfDef) {
		this.wfDef = wfDef;
	}

	public List<WfTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<WfTask> tasks) {
		this.tasks = tasks;
	}

	public List<WfTaskConn> getConns() {
		return conns;
	}

	public void setConns(List<WfTaskConn> conns) {
		this.conns = conns;
	}

	public JSONObject getWfData() {
		return wfData;
	}

	public void setWfData(JSONObject wfData) {
		this.wfData = wfData;
	}

//	public RsModule getModule() {
//		return module;
//	}
//
//	public void setModule(RsModule module) {
//		this.module = module;
//	}

	
}
