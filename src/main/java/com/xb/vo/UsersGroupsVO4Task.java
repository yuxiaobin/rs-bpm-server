package com.xb.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xb.common.WFConstants;
import com.xb.persistent.TblGroup;
import com.xb.persistent.TblUser;
import com.xb.persistent.WfCustVars;
import com.xb.persistent.WfTaskAssign;

public class UsersGroupsVO4Task {
	
	private List<TblUser> userList;
	private List<TblGroup> groupList;
	private List<WfCustVars> custUserList;
	
	public List<WfCustVars> getCustUserList() {
		return custUserList;
	}
	public void setCustUserList(List<WfCustVars> custUserList) {
		this.custUserList = custUserList;
	}
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
	
	public JSONObject toJSONObject(List<WfTaskAssign> assignerList){
		if(assignerList==null){
			assignerList = new ArrayList<WfTaskAssign>(0);
		}
		Map<String,WfTaskAssign> assignMap = new HashMap<String,WfTaskAssign>(assignerList.size());
		for(WfTaskAssign assign:assignerList){
			assignMap.put(assign.getAssignRelId()+assign.getAssignType(), assign);
		}
		JSONObject result = new JSONObject();
		WfTaskAssign tmpAssign = null;
		if(userList!=null){
			JSONArray userArray = new JSONArray(userList.size());
			JSONObject userJson = null;
			for(TblUser user : userList){
				userJson = new JSONObject();
				userJson.put("id", user.getId());
				userJson.put("name", user.getName());
				tmpAssign = assignMap.get(user.getId()+WFConstants.AssignType.USER);
				if(tmpAssign!=null){
					userJson.put("defSelMod", tmpAssign.getDefSelFlag());
					userJson.put("checkFlag", WFConstants.TaskSelectAllFlag.YES.equals(tmpAssign.getSelAllFlag())?true:false);
				}else{
					userJson.put("defSelMod", "");
					userJson.put("checkFlag", false);
				}
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
				tmpAssign = assignMap.get(group.getGroupId()+WFConstants.AssignType.GROUP);
				if(tmpAssign!=null){
					groupJson.put("defSelMod", tmpAssign.getDefSelFlag());
					groupJson.put("checkFlag", WFConstants.TaskSelectAllFlag.YES.equals(tmpAssign.getSelAllFlag())?true:false);
				}else{
					groupJson.put("defSelMod", "");
					groupJson.put("checkFlag", false);
				}
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
		if(custUserList!=null && !custUserList.isEmpty()){
			JSONArray custArray = new JSONArray(custUserList.size());
			JSONObject custJson = null;
			for(WfCustVars custU : custUserList){
				custJson = new JSONObject();
				custJson.put("id", custU.getCustVarsId());
				custJson.put("name", custU.getVarDescp());
				tmpAssign = assignMap.get(custU.getCustVarsId()+WFConstants.AssignType.CUST);
				if(tmpAssign!=null){
					custJson.put("defSelMod", tmpAssign.getDefSelFlag());
					custJson.put("checkFlag", WFConstants.TaskSelectAllFlag.YES.equals(tmpAssign.getSelAllFlag())?true:false);
				}else{
					custJson.put("defSelMod", "");
					custJson.put("checkFlag", false);
				}
				custArray.add(custJson);
			}
			result.put("custUsers", custArray);
		}
		return result;
	}
}
