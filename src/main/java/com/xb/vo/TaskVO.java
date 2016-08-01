package com.xb.vo;

import com.xb.persistent.WfInstHist;
import com.xb.persistent.WfTask;

public class TaskVO {
	
	private WfTask task;
	private WfInstHist instHist;
	public WfTask getTask() {
		return task;
	}
	public void setTask(WfTask task) {
		this.task = task;
	}
	public WfInstHist getInstHist() {
		return instHist;
	}
	public void setInstHist(WfInstHist instHist) {
		this.instHist = instHist;
	}
	
}
