package com.xb.persistent;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xb.base.CUBaseTO;

/**
 *
 * null
 *
 */
@TableName("WF_VERSION")
public class WfVersion extends CUBaseTO implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/** null */
	@TableId(value = "WF_VERSION_ID", type=IdType.UUID)
	private String wfId;

	/** null */
	@TableField(value = "REF_MKID")
	private String refMkid;

	/** null */
	private Integer VERSION;

	public String getWfId() {
		return wfId;
	}

	public void setWfId(String wfId) {
		this.wfId = wfId;
	}

	public String getRefMkid() {
		return refMkid;
	}

	public void setRefMkid(String refMkid) {
		this.refMkid = refMkid;
	}

	public Integer getVERSION() {
		return this.VERSION;
	}

	public void setVERSION(Integer VERSION) {
		this.VERSION = VERSION;
	}

}
