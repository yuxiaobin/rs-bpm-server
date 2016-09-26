package com.xb.vo;

import com.xb.persistent.WfAwt;
import com.xb.persistent.WfInstHist;

public class TaskVO {
	private WfAwt awt;//待办事宜
	
	private WfInstHist instHist;//上一条历史记录
	public WfInstHist getInstHist() {
		return instHist;
	}
	public void setInstHist(WfInstHist instHist) {
		this.instHist = instHist;
	}
	public WfAwt getAwt() {
		return awt;
	}
	public void setAwt(WfAwt awt) {
		this.awt = awt;
	}
	
	private String taskId;
	private String taskDescpDisp;
	private String taskType;//start-task, end-task, user-task, rs-cond-task
	private String taskTypeCode;
	
	public String getTaskTypeCode() {
		return taskTypeCode;
	}
	public void setTaskTypeCode(String taskTypeCode) {
		this.taskTypeCode = taskTypeCode;
	}
	public String getTaskDescpDisp() {
		return taskDescpDisp;
	}
	public void setTaskDescpDisp(String taskDescpDisp) {
		this.taskDescpDisp = taskDescpDisp;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
}
