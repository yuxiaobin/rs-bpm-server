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
@TableName("rs_workflow")
public class RsWorkflow extends CUBaseTO implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "RS_WF_ID", type = IdType.UUID)
	private String rsWfId;

	/**  */
	@TableField(value = "RS_WF_NAME")
	private String rsWfName;

	

	public String getRsWfId() {
		return this.rsWfId;
	}

	public void setRsWfId(String rsWfId) {
		this.rsWfId = rsWfId;
	}

	public String getRsWfName() {
		return this.rsWfName;
	}

	public void setRsWfName(String rsWfName) {
		this.rsWfName = rsWfName;
	}

	

}
