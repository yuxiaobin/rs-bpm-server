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
	@TableField(value = "CURR_TASK_ID")
	private String currTaskId;

	
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

	public String getCurrTaskId() {
		return currTaskId;
	}

	public void setCurrTaskId(String currTaskId) {
		this.currTaskId = currTaskId;
	}

}
