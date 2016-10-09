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
@TableName("WFDEF")
public class RsWorkflow implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "WFDEF_WFID", type = IdType.UUID)
	private String rsWfId;

	@TableField(value = "WFDEF_MKID")
	private String refMkid;
	
	public String getRefMkid() {
		return refMkid;
	}

	public void setRefMkid(String refMkid) {
		this.refMkid = refMkid;
	}

	public String getRsWfId() {
		return this.rsWfId;
	}

	public void setRsWfId(String rsWfId) {
		this.rsWfId = rsWfId;
	}

}
