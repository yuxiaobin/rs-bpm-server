package com.xb.persistent;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xb.base.CUBaseTO;

/**
 *
 * 
 *
 */
@TableName("wf_task_assign")
public class WfTaskAssign extends CUBaseTO implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "TASK_ASSIGN_ID", type = IdType.UUID)
	private String taskAssignId;

	/**  */
	@TableField(value = "TASK_ID")
	private String taskId;

	/**  */
	@TableField(value = "ASSIGN_TYPE")
	private String assignType;

	/**  */
	@TableField(value = "ASSIGN_REL_ID")
	private String assignRelId;

	/**  */
	@TableField(value = "DEF_SEL_FLAG")
	private String defSelFlag;

	/**  */
	@TableField(value = "SEL_ALL_FLAG")
	private String selAllFlag;

	/**  */
	@TableField(value = "EXE_CONDITION")
	private String exeCondition;

	@TableField(exist = false)
	private String userName;
	
	@TableField(exist = false)
	private String varCode;
	
	@TableField(exist = false)
	private String varDescp;
	
	@TableField(exist = false)
	private String groupName;

	public String getTaskAssignId() {
		return this.taskAssignId;
	}

	public void setTaskAssignId(String taskAssignId) {
		this.taskAssignId = taskAssignId;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getAssignType() {
		return this.assignType;
	}

	public void setAssignType(String assignType) {
		this.assignType = assignType;
	}

	public String getAssignRelId() {
		return this.assignRelId;
	}

	public void setAssignRelId(String assignRelId) {
		this.assignRelId = assignRelId;
	}

	public String getDefSelFlag() {
		return this.defSelFlag;
	}

	public void setDefSelFlag(String defSelFlag) {
		this.defSelFlag = defSelFlag;
	}

	public String getSelAllFlag() {
		return this.selAllFlag;
	}

	public void setSelAllFlag(String selAllFlag) {
		this.selAllFlag = selAllFlag;
	}

	public String getExeCondition() {
		return this.exeCondition;
	}

	public void setExeCondition(String exeCondition) {
		this.exeCondition = exeCondition;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getVarCode() {
		return varCode;
	}

	public void setVarCode(String varCode) {
		this.varCode = varCode;
	}

	public String getVarDescp() {
		return varDescp;
	}

	public void setVarDescp(String varDescp) {
		this.varDescp = varDescp;
	}

	@Override
	public String toString() {
		return "WfTaskAssign [taskAssignId=" + taskAssignId + ", taskId=" + taskId + ", assignType=" + assignType
				+ ", assignRelId=" + assignRelId + ", defSelFlag=" + defSelFlag + ", selAllFlag=" + selAllFlag
				+ ", exeCondition=" + exeCondition + ", userName=" + userName + ", varCode=" + varCode + ", varDescp="
				+ varDescp + ", groupName=" + groupName + "]";
	}

}
