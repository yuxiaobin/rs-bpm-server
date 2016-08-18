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
@TableName("wf_inst_hist")
public class WfInstHist extends CUBaseTO implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "HIST_ID", type = IdType.UUID)
	private String histId;

	/**  */
	@TableField(value = "INST_ID")
	private String instId;

	/**  */
	@TableField(value = "WF_ID")
	private String wfId;

	/**  */
	@TableField(value = "TASK_ID")
	private String taskId;

	/**  */
	@TableField(value = "OPT_USER")
	private String optUser;

	/**  */
	@TableField(value = "OPT_TYPE")
	private String optType;

	/**  */
	@TableField(value = "OPT_SEQ")
	private Integer optSeq;

	/**  */
	@TableField(value = "NEXT_ASSIGNER")
	private String nextAssigner;//待处理人

	/**  */
	@TableField(value = "TASK_OWNER")
	private String taskOwner;//当前处理人

	/**  */
	@TableField(value = "TASK_BEGIN")
	private Date taskBegin;

	/**  */
	@TableField(value = "TASK_END")
	private Date taskEnd;

	/**  */
	@TableField(value = "TASK_REND")
	private Date taskRend;
	
	@TableField(exist = false)
	private String taskDescpDisp;
	
	public String getTaskDescpDisp() {
		return taskDescpDisp;
	}

	public void setTaskDescpDisp(String taskDescpDisp) {
		this.taskDescpDisp = taskDescpDisp;
	}

	public String getHistId() {
		return this.histId;
	}

	public void setHistId(String histId) {
		this.histId = histId;
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

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getOptUser() {
		return this.optUser;
	}

	public void setOptUser(String optUser) {
		this.optUser = optUser;
	}

	public String getOptType() {
		return this.optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}

	public Integer getOptSeq() {
		return this.optSeq;
	}

	public void setOptSeq(Integer optSeq) {
		this.optSeq = optSeq;
	}

	public String getNextAssigner() {
		return this.nextAssigner;
	}

	public void setNextAssigner(String nextAssigner) {
		this.nextAssigner = nextAssigner;
	}

	public String getTaskOwner() {
		return this.taskOwner;
	}

	public void setTaskOwner(String taskOwner) {
		this.taskOwner = taskOwner;
	}

	public Date getTaskBegin() {
		return this.taskBegin;
	}

	public void setTaskBegin(Date taskBegin) {
		this.taskBegin = taskBegin;
	}

	public Date getTaskEnd() {
		return this.taskEnd;
	}

	public void setTaskEnd(Date taskEnd) {
		this.taskEnd = taskEnd;
	}

	public Date getTaskRend() {
		return this.taskRend;
	}

	public void setTaskRend(Date taskRend) {
		this.taskRend = taskRend;
	}
}
