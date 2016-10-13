package com.xb.service;

import java.util.List;

import com.baomidou.framework.service.ICommonService;
import com.xb.persistent.TblGroup;
import com.xb.persistent.TblUser;
import com.xb.persistent.TblUser2group;
import com.xb.persistent.WfCustVars;
import com.xb.vo.UsersGroupsVO4Task;

/**
 *
 * TblUser2group 表数据服务层接口
 *
 */
public interface ITblUser2groupService extends ICommonService<TblUser2group> {

	/**
	 * 根据userIdList获取user详细信息list
	 * @param userIdList
	 * @return
	 */
	public UsersGroupsVO4Task getUsersGroupsDtlList(List<String> userIdList, List<String> groupIdList, List<WfCustVars> custUserList);
	
	public List<TblGroup> getAllGroupsWithUsers();
	
	public List<TblGroup> getAllGroupsWithWithoutUsers();
	
	public List<TblUser> getUserInSpecGroup(String groupId);
	
	public List<TblUser> getAddableUsers(String groupId);
	
}