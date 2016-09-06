package com.xb.persistent;

import java.io.Serializable;
import java.util.Date;

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
@TableName("wf_awt")
public class WfAwt extends CUBaseTO implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "wf_awt_id", type = IdType.UUID)
	private String wfAwtId;

	/**  */
	@TableField(value = "INST_ID")
	private String instId;

	/**  */
	@TableField(value = "TASK_ID_CURR")
	private String taskIdCurr;

	/** 
	 * 0905 updated 参考原有设计
	 * 记录上一步操作的taskId和userId
	 */
	@TableField(value = "TASK_ID_PRE")
	private String taskIdPre;
	@TableField(value = "OPT_USERS_PRE")
	private String optUsersPre;//上一步处理人：上一步，指的是上一个事务节点，如果事务只是同一节点流转（转交，我来处理），则不变

	/**  */
	@TableField(value = "ASSIGNER_ID")
	private String assignerId;//对应的人有待办事宜

	/**  */
	@TableField(value = "awt_begin")
	private Date awtBegin;

	/**  */
	@TableField(value = "awt_end")
	private Date awtEnd;

	/**  */
	@TableField(value = "awt_alarm")
	private Date awtAlarm;

	/**  */
	@TableField(value = "awt_title")
	private String awtTitle;

	/**  */
	@TableField(value = "awt_summary")
	private String awtSummary;
	
	@TableField(value = "complete_flag")
	private String completeFlag = "N";

	
	@TableField(exist = false)
	private String taskOwner;//待处理人，该task可以处理的人
	
	@TableField(exist = false)
	private String taskProcesser;//该流程处理过的人，已处理人
	
	@TableField(exist = false)
	private String instCreater;//创建人
	
	@TableField(exist = false)
	private String taskDescpDisp;
	
	@TableField(exist = false)
	private String rsWfId;
	
	@TableField(exist = false)
	private Integer instNum;
	
	@TableField(exist = false)
	private String refMkid;
	
	
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

	public String getInstCreater() {
		return instCreater;
	}

	public void setInstCreater(String instCreater) {
		this.instCreater = instCreater;
	}

	public String getRefMkid() {
		return refMkid;
	}

	public String getTaskProcesser() {
		return taskProcesser;
	}

	public void setTaskProcesser(String taskProcesser) {
		this.taskProcesser = taskProcesser;
	}

	public void setRefMkid(String refMkid) {
		this.refMkid = refMkid;
	}

	public String getRsWfId() {
		return rsWfId;
	}

	public void setRsWfId(String rsWfId) {
		this.rsWfId = rsWfId;
	}

	public Integer getInstNum() {
		return instNum;
	}

	public void setInstNum(Integer instNum) {
		this.instNum = instNum;
	}

	public String getTaskDescpDisp() {
		return taskDescpDisp;
	}

	public void setTaskDescpDisp(String taskDescpDisp) {
		this.taskDescpDisp = taskDescpDisp;
	}

	public String getTaskOwner() {
		return taskOwner;
	}

	public void setTaskOwner(String taskOwner) {
		this.taskOwner = taskOwner;
	}

	public String getWfAwtId() {
		return this.wfAwtId;
	}

	public void setWfAwtId(String wfAwtId) {
		this.wfAwtId = wfAwtId;
	}

	public String getInstId() {
		return this.instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getTaskIdCurr() {
		return this.taskIdCurr;
	}

	public void setTaskIdCurr(String taskIdCurr) {
		this.taskIdCurr = taskIdCurr;
	}

	public String getAssignerId() {
		return this.assignerId;
	}

	public void setAssignerId(String assignerId) {
		this.assignerId = assignerId;
	}

	public Date getAwtBegin() {
		return this.awtBegin;
	}

	public void setAwtBegin(Date awtBegin) {
		this.awtBegin = awtBegin;
	}

	public Date getAwtEnd() {
		return this.awtEnd;
	}

	public void setAwtEnd(Date awtEnd) {
		this.awtEnd = awtEnd;
	}

	public Date getAwtAlarm() {
		return this.awtAlarm;
	}

	public void setAwtAlarm(Date awtAlarm) {
		this.awtAlarm = awtAlarm;
	}

	public String getAwtTitle() {
		return this.awtTitle;
	}

	public void setAwtTitle(String awtTitle) {
		this.awtTitle = awtTitle;
	}

	public String getAwtSummary() {
		return this.awtSummary;
	}

	public void setAwtSummary(String awtSummary) {
		this.awtSummary = awtSummary;
	}

	public String getCompleteFlag() {
		return completeFlag;
	}

	public void setCompleteFlag(String completeFlag) {
		this.completeFlag = completeFlag;
	}

}
