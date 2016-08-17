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

	/**  */
	@TableField(value = "HIST_ID_PRE")
	private String histIdPre;

	/**  */
	@TableField(value = "ASSIGNER_ID")
	private String assignerId;

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
	private String nextAssigners;//待处理人
	
	public String getNextAssigners() {
		return nextAssigners;
	}

	public void setNextAssigners(String nextAssigners) {
		this.nextAssigners = nextAssigners;
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

	public String getHistIdPre() {
		return this.histIdPre;
	}

	public void setHistIdPre(String histIdPre) {
		this.histIdPre = histIdPre;
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
