package com.xb.vo;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xb.persistent.TblGroup;
import com.xb.persistent.TblUser;

public class UsersGroupsVO4Task {
	
	private List<TblUser> userList;
	private List<TblGroup> groupList;
	public List<TblUser> getUserList() {
		return userList;
	}
	public void setUserList(List<TblUser> userList) {
		this.userList = userList;
	}
	public List<TblGroup> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<TblGroup> groupList) {
		this.groupList = groupList;
	}
	public UsersGroupsVO4Task() {
		super();
	}
	
	public UsersGroupsVO4Task(int userSize, int groupSize) {
		userList = new ArrayList<TblUser>(userSize);
		groupList = new ArrayList<TblGroup>(groupSize);
	}
	
	public void addUser(TblUser user){
		if(user!=null){
			userList.add(user);
		}
	}

	public void addGroup(TblGroup group){
		if(group!=null)
			groupList.add(group);
	}
	
	public JSONObject toJSONObject(){
		JSONObject result = new JSONObject();
		if(userList!=null){
			JSONArray userArray = new JSONArray(userList.size());
			JSONObject userJson = null;
			for(TblUser user : userList){
				userJson = new JSONObject();
				userJson.put("id", user.getId());
				userJson.put("name", user.getName());
				userArray.add(userJson);
			}
			result.put("users", userArray);
		}
		if(groupList!=null){
			JSONArray groupArray = new JSONArray(groupList.size());
			JSONObject groupJson = null;
			for(TblGroup group : groupList){
				groupJson = new JSONObject();
				groupJson.put("id", group.getGroupId());
				groupJson.put("name", group.getGroupName());
				List<TblUser> userlist = group.getUserlist();
				if(userlist!=null){
					JSONArray usersInGroup = new JSONArray();
					JSONObject usrJs = null;
					for(TblUser usr : userlist){
						usrJs = new JSONObject();
						usrJs.put("id", usr.getId());
						usrJs.put("name", usr.getName());
						usersInGroup.add(usrJs);
					}
					groupJson.put("usersInGroup", usersInGroup);
				}
				groupArray.add(groupJson);
			}
			result.put("groups", groupArray);
		}
		return result;
	}
	public String toString(){
		return toJSONObject().toJSONString();
	}
}
