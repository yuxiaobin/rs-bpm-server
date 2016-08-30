package com.xb.service;

import java.util.List;

import com.baomidou.framework.service.ICommonService;
import com.xb.persistent.TblGroup;
import com.xb.persistent.TblUser;

/**
 *
 * TblGroup 表数据服务层接口
 *
 */
public interface ITblGroupService extends ICommonService<TblGroup> {
	
	public List<TblGroup> getGroupsWithUsers();

	public List<TblUser> getUserInSpecGroup(String groupId);
	
	public List<TblUser> getAddableUsers(String groupId);
	
	public void addUsers2Group(String groupId, String[] userIds);
	
	public void deleteUsers2Group(String groupId, String[] userIds);

}