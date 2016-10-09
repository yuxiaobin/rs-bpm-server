package com.xb.persistent;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotations.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 *
 * 
 *
 */
@TableName("UG")
public class TblGroup implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "UG_UGID", type = IdType.UUID)
	private String groupId;

	/**  */
	@TableField(value = "UG_UGNAME")
	private String groupName;
	
	@TableField(exist = false)
	private List<TblUser> userlist;
	
	public List<TblUser> getUserlist() {
		return userlist;
	}

	public void setUserlist(List<TblUser> userlist) {
		this.userlist = userlist;
	}
	
	public void addUser(TblUser user){
		this.userlist.add(user);
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
