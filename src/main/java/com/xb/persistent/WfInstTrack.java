package com.xb.persistent;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 *
 * 
 *
 */
@TableName("wf_inst_track")
public class WfInstTrack implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "TRACK_ID", type = IdType.INPUT)
	private String trackId;

	/**  */
	@TableField(value = "ROLLBACK_FLAG")
	private String rollbackFlag;

	/**  */
	@TableField(value = "CREATED_BY")
	private String createdBy;

	/**  */
	@TableField(value = "UPDATED_BY")
	private String updatedBy;

	/**  */
	@TableField(value = "CREATED_DT")
	private Date createdDt;

	/**  */
	@TableField(value = "UPDATED_DT")
	private Date updatedDt;

	public String getTrackId() {
		return this.trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public String getRollbackFlag() {
		return this.rollbackFlag;
	}

	public void setRollbackFlag(String rollbackFlag) {
		this.rollbackFlag = rollbackFlag;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

}
