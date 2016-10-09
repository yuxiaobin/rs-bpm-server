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
@TableName("wf_group")
public class WfGroup extends CUBaseTO implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "WF_GROUP_ID", type = IdType.UUID)
	private Integer wfGroupId;

	/**  */
	@TableField(value = "GROUP_NAME")
	private String groupName;

	/**  */
	private String USERS;

	

	public Integer getWfGroupId() {
		return this.wfGroupId;
	}

	public void setWfGroupId(Integer wfGroupId) {
		this.wfGroupId = wfGroupId;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getUSERS() {
		return this.USERS;
	}

	public void setUSERS(String USERS) {
		this.USERS = USERS;
	}

	

}
