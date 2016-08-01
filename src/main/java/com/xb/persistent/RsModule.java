package com.xb.persistent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
@TableName("rs_module")
public class RsModule extends CUBaseTO implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "MOD_ID", type = IdType.UUID)
	private String modId;

	/**  */
	private String NAME;

	/**  */
	@TableField(value = "OUTPUT_CLASS")
	private String outputClass;

	/**  */
	@TableField(value = "OUTPUT_OPT")
	private String outputOpt;

	/**  */
	@TableField(value = "WF_FLAG")
	private String wfFlag;

	/**  */
	@TableField(value = "RS_WF_ID")
	private String rsWfId;

	
	/**
	 * Store the 
	 */
	@TableField(exist = false)
	private List<WfDef> wfList = null;
	
	public List<WfDef> getWfList() {
		return wfList == null?new ArrayList<WfDef>(0):wfList;
	}

	public void setWfList(List<WfDef> wfList) {
		this.wfList = wfList;
	}

	public String getModId() {
		return this.modId;
	}

	public void setModId(String modId) {
		this.modId = modId;
	}

	public String getNAME() {
		return this.NAME;
	}

	public void setNAME(String NAME) {
		this.NAME = NAME;
	}

	public String getOutputClass() {
		return this.outputClass;
	}

	public void setOutputClass(String outputClass) {
		this.outputClass = outputClass;
	}

	public String getOutputOpt() {
		return this.outputOpt;
	}

	public void setOutputOpt(String outputOpt) {
		this.outputOpt = outputOpt;
	}

	public String getWfFlag() {
		return this.wfFlag;
	}

	public void setWfFlag(String wfFlag) {
		this.wfFlag = wfFlag;
	}

	public String getRsWfId() {
		return this.rsWfId;
	}

	public void setRsWfId(String rsWfId) {
		this.rsWfId = rsWfId;
	}

}
