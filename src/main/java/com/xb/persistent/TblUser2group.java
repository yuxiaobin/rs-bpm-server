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
@TableName("tbl_user2group")
public class TblUser2group implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "user2group_id", type = IdType.UUID)
	private String user2groupId;

	/**  */
	@TableField(value = "user_id")
	private String userId;

	/**  */
	@TableField(value = "group_id")
	private String groupId;

	public String getUser2groupId() {
		return this.user2groupId;
	}

	public void setUser2groupId(String user2groupId) {
		this.user2groupId = user2groupId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}
