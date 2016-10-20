package com.xb.persistent;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 *
 * 
 *
 */
@TableName("wf_inst_tracklog")
public class WfInstTracklog implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "TRACKLOG_ID", type = IdType.UUID)
	private String tracklogId;

	/**  */
	@TableField(value = "TRACK_ID")
	private String trackId;

	/**  */
	@TableField(value = "DATA_OPTION")
	private String dataOption;

	/**  */
	@TableField(value = "ENTITY_CLASS_TYPE")
	private String entityClassType;

	/**  */
	@TableField(value = "DATA_BEFORE")
	private String dataBefore;

	/**  */
	@TableField(value = "DATA_AFTER")
	private String dataAfter;

	public String getTracklogId() {
		return this.tracklogId;
	}

	public void setTracklogId(String tracklogId) {
		this.tracklogId = tracklogId;
	}

	public String getTrackId() {
		return this.trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public String getDataOption() {
		return this.dataOption;
	}

	public void setDataOption(String dataOption) {
		this.dataOption = dataOption;
	}

	public String getEntityClassType() {
		return this.entityClassType;
	}

	public void setEntityClassType(String entityClassType) {
		this.entityClassType = entityClassType;
	}

	public String getDataBefore() {
		return this.dataBefore;
	}

	public void setDataBefore(String dataBefore) {
		this.dataBefore = dataBefore;
	}

	public String getDataAfter() {
		return this.dataAfter;
	}

	public void setDataAfter(String dataAfter) {
		this.dataAfter = dataAfter;
	}

}
