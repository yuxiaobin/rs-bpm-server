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
@TableName("wf_def")
public class WfDef extends CUBaseTO implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "WF_ID", type = IdType.UUID)
	private String wfId;

	/**  */
	@TableField(value = "RS_WF_ID")
	private String rsWfId;

	/**  */
	private Integer VERSION;

	
	public String getWfId() {
		return this.wfId;
	}

	public void setWfId(String wfId) {
		this.wfId = wfId;
	}

	public String getRsWfId() {
		return this.rsWfId;
	}

	public void setRsWfId(String rsWfId) {
		this.rsWfId = rsWfId;
	}

	public Integer getVERSION() {
		return this.VERSION;
	}

	public void setVERSION(Integer VERSION) {
		this.VERSION = VERSION;
	}

	

}
