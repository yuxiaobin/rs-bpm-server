package com.xb.persistent;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.toolkit.CUBaseTO;

/**
 *
 * 
 *
 */
@TableName("wf_task")
public class WfTask extends CUBaseTO implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "TASK_ID", type = IdType.UUID)
	private String taskId;

	/**  */
	@TableField(value = "WF_ID")
	private String wfId;

	/**  */
	@TableField(value = "TASK_PG_ID")
	private String taskPgId;

	/**  */
	@TableField(value = "TASK_TYPE")
	private String taskType;

	/**  */
	@TableField(value = "TASK_DESCP")
	private String taskDescp;

	/**  */
	@TableField(value = "POS_TOP")
	private Double posTop;

	/**  */
	@TableField(value = "POS_LEFT")
	private Double posLeft;

	/**  */
	@TableField(value = "ASSIGN_USERS")
	private String assignUsers;

	/**  */
	@TableField(value = "ASSIGN_GROUPS")
	private String assignGroups;

	/**
	 * 用来标识是否该task被执行过
	 */
	@TableField(exist = false)
	private String processedFlag;
	/**
	 * 用来标识是否当前task是WF正在pending的那个task
	 */
	@TableField(exist = false)
	private String currTaskId;
	
	public String getProcessedFlag() {
		return processedFlag;
	}

	public void setProcessedFlag(String processedFlag) {
		this.processedFlag = processedFlag;
	}

	public String getCurrTaskId() {
		return currTaskId;
	}

	public void setCurrTaskId(String currTaskId) {
		this.currTaskId = currTaskId;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getWfId() {
		return this.wfId;
	}

	public void setWfId(String wfId) {
		this.wfId = wfId;
	}

	public String getTaskPgId() {
		return this.taskPgId;
	}

	public void setTaskPgId(String taskPgId) {
		this.taskPgId = taskPgId;
	}

	public String getTaskType() {
		return this.taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskDescp() {
		return this.taskDescp;
	}

	public void setTaskDescp(String taskDescp) {
		this.taskDescp = taskDescp;
	}

	public Double getPosTop() {
		return this.posTop;
	}

	public void setPosTop(Double posTop) {
		this.posTop = posTop;
	}

	public Double getPosLeft() {
		return this.posLeft;
	}

	public void setPosLeft(Double posLeft) {
		this.posLeft = posLeft;
	}

	public String getAssignUsers() {
		return this.assignUsers;
	}

	public void setAssignUsers(String assignUsers) {
		this.assignUsers = assignUsers;
	}

	public String getAssignGroups() {
		return this.assignGroups;
	}

	public void setAssignGroups(String assignGroups) {
		this.assignGroups = assignGroups;
	}

	@Override
	public String toString() {
		return "WfTask [taskId=" + taskId + ", wfId=" + wfId + ", taskPgId=" + taskPgId + ", taskType=" + taskType
				+ ", taskDescp=" + taskDescp + ", posTop=" + posTop + ", posLeft=" + posLeft + ", assignUsers="
				+ assignUsers + ", assignGroups=" + assignGroups + "]";
	}

	

}
