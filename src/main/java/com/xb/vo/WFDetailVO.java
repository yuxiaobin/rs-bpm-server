package com.xb.vo;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.xb.persistent.RsWorkflow;
import com.xb.persistent.WfDef;
import com.xb.persistent.WfTask;
import com.xb.persistent.WfTaskConn;

public class WFDetailVO {
	
	private JSONObject wfData;

	private RsWorkflow rsWF;
	private WfDef wfDef;// latest record > max Version

	List<WfTask> tasks;
	List<WfTaskConn> conns;

	public RsWorkflow getRsWF() {
		return rsWF;
	}

	public void setRsWF(RsWorkflow rsWF) {
		this.rsWF = rsWF;
	}

	public WfDef getWfDef() {
		return wfDef;
	}

	public void setWfDef(WfDef wfDef) {
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

	
}
