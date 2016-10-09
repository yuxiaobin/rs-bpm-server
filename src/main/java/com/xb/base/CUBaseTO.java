package com.xb.base;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;

public class CUBaseTO {

	/**  */
	@TableField(value = "CREATED_BY")
	private String createdBy;

	/**  */
	@TableField(value = "CREATED_DT")
	private Date createdDt;

	/**  */
	@TableField(value = "UPDATED_BY")
	private String updatedBy;

	/**  */
	@TableField(value = "UPDATED_DT")
	private Date updatedDt;
	
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}
}
