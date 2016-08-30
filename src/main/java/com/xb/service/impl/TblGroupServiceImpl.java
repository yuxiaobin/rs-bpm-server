package com.xb.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.xb.persistent.TblGroup;
import com.xb.persistent.TblUser;
import com.xb.persistent.TblUser2group;
import com.xb.persistent.mapper.TblGroupMapper;
import com.xb.service.ITblGroupService;
import com.xb.service.ITblUser2groupService;

/**
 *
 * TblGroup 表数据服务层接口实现类
 *
 */
@Service
public class TblGroupServiceImpl extends CommonServiceImpl<TblGroupMapper, TblGroup> implements ITblGroupService {

	@Autowired
	ITblUser2groupService user2GroupService;
	
	public List<TblGroup> getGroupsWithUsers(){
		return user2GroupService.getAllGroupsWithWithoutUsers();
	}
	
	public List<TblUser> getUserInSpecGroup(String groupId){
		return user2GroupService.getUserInSpecGroup(groupId);
	}
	
	public List<TblUser> getAddableUsers(String groupId){
		return user2GroupService.getAddableUsers(groupId);
	}
	
	@Transactional
	public void addUsers2Group(String groupId, String[] userIds){
		List<TblUser2group> records = new ArrayList<TblUser2group>(userIds.length);
		TblUser2group u2g = null;
		for(String userId:userIds){
			u2g = new TblUser2group();
			u2g.setGroupId(groupId);
			u2g.setUserId(userId);
			records.add(u2g);
		}
		user2GroupService.insertBatch(records);
	}
	
	@Transactional
	public void deleteUsers2Group(String groupId, String[] userIds){
		TblUser2group u2g = null;
		for(String userId:userIds){
			u2g = new TblUser2group();
			u2g.setGroupId(groupId);
			u2g.setUserId(userId);
			user2GroupService.deleteSelective(u2g);
		}
	}
	
}