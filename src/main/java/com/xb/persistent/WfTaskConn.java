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
@TableName("wf_task_conn")
public class WfTaskConn extends CUBaseTO implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "CONN_ID", type = IdType.UUID)
	private String connId;

	/**  */
	@TableField(value = "WF_ID")
	private String wfId;

	/**  */
	@TableField(value = "SOURCE_TASK_ID")
	private String sourceTaskId;

	/**  */
	@TableField(value = "TARGET_TASK_ID")
	private String targetTaskId;

	/**  */
	@TableField(value = "CONN_VAL")
	private String connVal;

	/**  */
	@TableField(value = "CONN_DESCP")
	private String connDescp;


	public String getConnId() {
		return this.connId;
	}

	public void setConnId(String connId) {
		this.connId = connId;
	}

	public String getWfId() {
		return this.wfId;
	}

	public void setWfId(String wfId) {
		this.wfId = wfId;
	}

	public String getSourceTaskId() {
		return this.sourceTaskId;
	}

	public void setSourceTaskId(String sourceTaskId) {
		this.sourceTaskId = sourceTaskId;
	}

	public String getTargetTaskId() {
		return this.targetTaskId;
	}

	public void setTargetTaskId(String targetTaskId) {
		this.targetTaskId = targetTaskId;
	}

	public String getConnVal() {
		return this.connVal;
	}

	public void setConnVal(String connVal) {
		this.connVal = connVal;
	}

	public String getConnDescp() {
		return this.connDescp;
	}

	public void setConnDescp(String connDescp) {
		this.connDescp = connDescp;
	}

}
