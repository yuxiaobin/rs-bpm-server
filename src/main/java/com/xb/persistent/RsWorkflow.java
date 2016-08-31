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
@TableName("rs_workflow")
public class RsWorkflow extends CUBaseTO implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "RS_WF_ID", type = IdType.UUID)
	private String rsWfId;

	@TableField(value = "GNMK_ID")
	private String gnmkId;
	

	public String getGnmkId() {
		return gnmkId;
	}

	public void setGnmkId(String gnmkId) {
		this.gnmkId = gnmkId;
	}

	public String getRsWfId() {
		return this.rsWfId;
	}

	public void setRsWfId(String rsWfId) {
		this.rsWfId = rsWfId;
	}

}
