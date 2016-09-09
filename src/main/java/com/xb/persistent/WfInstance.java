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
@TableName("wf_instance")
public class WfInstance extends CUBaseTO implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "INST_ID", type = IdType.UUID)
	private String instId;

	/**  */
	@TableField(value = "WF_ID")
	private String wfId;

	/**  */
	@TableField(value = "WF_STATUS")
	private String wfStatus;

	/**  */
	@TableField(value = "RS_WF_ID")
	private String rsWfId;

	/**  */
	@TableField(value = "INST_NUM")
	private Integer instNum;
	
	/**  */
	@TableField(value = "REF_MKID")
	private String refMkid;
	
	@TableField(value = "CURR_ASSIGNERS")
	private String currAssigners;
	
	/**
	 * 该字段始终保持当前pending的taskID
	 */
	@TableField(value = "TASK_ID_CURR")
	private String taskIdCurr;
	
	/**
	 * 该字段recall时不更新，为了会签事物其他人还能找到撤回的task
	 */
	@TableField(value = "TASK_ID_PRE")
	private String taskIdPre;
	
	@TableField(value = "OPT_USERS_PRE")
	private String optUsersPre;
	
	public String getTaskIdCurr() {
		return taskIdCurr;
	}

	public void setTaskIdCurr(String taskIdCurr) {
		this.taskIdCurr = taskIdCurr;
	}

	public String getRefMkid() {
		return refMkid;
	}

	public void setRefMkid(String refMkid) {
		this.refMkid = refMkid;
	}

	public String getCurrAssigners() {
		return currAssigners;
	}

	public void setCurrAssigners(String currAssigners) {
		this.currAssigners = currAssigners;
	}

	public String getInstId() {
		return this.instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getWfId() {
		return this.wfId;
	}

	public void setWfId(String wfId) {
		this.wfId = wfId;
	}

	public String getWfStatus() {
		return this.wfStatus;
	}

	public void setWfStatus(String wfStatus) {
		this.wfStatus = wfStatus;
	}

	public String getRsWfId() {
		return this.rsWfId;
	}

	public void setRsWfId(String rsWfId) {
		this.rsWfId = rsWfId;
	}

	public Integer getInstNum() {
		return this.instNum;
	}

	public void setInstNum(Integer instNum) {
		this.instNum = instNum;
	}

	public String getTaskIdPre() {
		return taskIdPre;
	}

	public void setTaskIdPre(String taskIdPre) {
		this.taskIdPre = taskIdPre;
	}

	public String getOptUsersPre() {
		return optUsersPre;
	}

	public void setOptUsersPre(String optUsersPre) {
		this.optUsersPre = optUsersPre;
	}

	@Override
	public String toString() {
		return "WfInstance [instId=" + instId + ", wfId=" + wfId + ", wfStatus=" + wfStatus + ", rsWfId=" + rsWfId
				+ ", instNum=" + instNum + ", refMkid=" + refMkid + ", currAssigners=" + currAssigners + ", taskIdCurr="
				+ taskIdCurr + ", taskIdPre=" + taskIdPre + ", optUsersPre=" + optUsersPre + "]";
	}

}
