package com.xb.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.xb.persistent.TblGroup;
import com.xb.persistent.TblUser;
import com.xb.persistent.TblUser2group;
import com.xb.persistent.mapper.TblUser2groupMapper;
import com.xb.service.ITblUser2groupService;
import com.xb.service.ITblUserService;
import com.xb.vo.UsersGroupsVO4Task;

/**
 *
 * TblUser2group 表数据服务层接口实现类
 *
 */
@Service
public class TblUser2groupServiceImpl extends CommonServiceImpl<TblUser2groupMapper, TblUser2group> implements ITblUser2groupService {

	@Autowired
	ITblUserService userService;
	
	public UsersGroupsVO4Task getUsersGroupsDtlList(List<String> userIdList, List<String> groupIdList){
		UsersGroupsVO4Task result = new UsersGroupsVO4Task();
		List<TblUser> userList = null;
		List<TblUser> groupUserList = null;
		if((userIdList==null || userIdList.isEmpty()) && (groupIdList==null || groupIdList.isEmpty())){//select all users
			userList = userService.selectList(new TblUser());
			groupUserList = baseMapper.getGroupListWithUsersAll();
		}
		else{
			if(userIdList!=null && !userIdList.isEmpty()){
				userList = baseMapper.getUserListByIdList(userIdList);
			}
			if(groupIdList!=null && !groupIdList.isEmpty()){
				groupUserList = baseMapper.getGroupListWithUsersByIdList(groupIdList);
			}
		}
		result.setUserList(userList);
		result.setGroupList(processUsers2Group(groupUserList));
		return result;
	}
	
	
	public List<TblGroup> getAllGroupsWithUsers(){
		return processUsers2Group(baseMapper.getGroupListWithUsersAll());
	}
	
	public List<TblGroup> getAllGroupsWithWithoutUsers(){
		return processUsers2Group(baseMapper.getGroupListWithWithoutUsersAll());
	}
	
	private List<TblGroup> processUsers2Group(List<TblUser> groupUserList){
		Map<String,TblGroup> groupIdMap = new HashMap<String,TblGroup>();
		if(groupUserList!=null && !groupUserList.isEmpty()){
			for(TblUser user:groupUserList){
				String groupId = user.getGroupId();
				if(groupIdMap.containsKey(groupId)){
					if(user.getId()!=null){
						groupIdMap.get(groupId).addUser(user);
					}
				}
				else{
					TblGroup group = new TblGroup();
					group.setGroupId(user.getGroupId());
					group.setGroupName(user.getGroupName());
					if(user.getId()!=null){
						List<TblUser> usersInGp = new ArrayList<TblUser>();
						usersInGp.add(user);
						group.setUserlist(usersInGp);
					}
					groupIdMap.put(groupId, group);
				}
			}
		}
		Collection<TblGroup> cln = groupIdMap.values();
		List<TblGroup> groupList = new ArrayList<TblGroup>(cln.size());
		groupList.addAll(cln);
		return groupList;
	}
	
	public List<TblUser> getUserInSpecGroup(String groupId){
		return baseMapper.getUsersInSpecGroup(groupId);
	}

	
	public List<TblUser> getAddableUsers(String groupId){
		return baseMapper.getAddableUsers(groupId);
	}

}