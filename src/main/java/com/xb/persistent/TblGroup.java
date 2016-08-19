package com.xb.persistent;

import java.io.Serializable;
import java.util.Date;
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
@TableName("tbl_group")
public class TblGroup implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(value = "GROUP_ID", type = IdType.UUID)
	private String groupId;

	/**  */
	@TableField(value = "GROUP_NAME")
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
