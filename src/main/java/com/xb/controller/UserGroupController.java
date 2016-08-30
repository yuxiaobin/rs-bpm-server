package com.xb.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xb.base.BaseController;
import com.xb.persistent.TblGroup;
import com.xb.persistent.TblUser;
import com.xb.service.ITblGroupService;
import com.xb.service.ITblUserService;

@Controller
@RequestMapping("/usergroup")
public class UserGroupController extends BaseController {
	
	@Autowired
	ITblUserService userService;
	@Autowired
	ITblGroupService groupService;
	
	@RequestMapping(value="/" )
	public String manageUserOnload(){
		return "wf-user-group";
	}
	@RequestMapping(value="" )
	public String manageUserOnload2(){
		return "wf-user-group";
	}
	
	
	
	@RequestMapping(value="/users", method=RequestMethod.GET )
	@ResponseBody
	public Object getAllUsers(){
		JSONObject result = new JSONObject();
		List<TblUser> userList = userService.selectList(new TblUser());
		if(userList!=null){
			JSONArray userArray = new JSONArray();
			JSONObject ob = null;
			for(TblUser usr:userList){
				ob = new JSONObject();
				ob.put("id", usr.getId());
				ob.put("name", usr.getName());
				userArray.add(ob);
			}
			result.put("records", userArray);
		}
		else{
			result.put("records", "[]");
		}
		return result;
	}
	
	@RequestMapping(value="/groups", method=RequestMethod.GET )
	@ResponseBody
	public Object getAllGroups(){
		JSONObject result = new JSONObject();
		List<TblGroup> groupList = groupService.selectList(new TblGroup());
		if(groupList!=null){
			JSONArray groupArray = new JSONArray();
			JSONObject ob = null;
			for(TblGroup gp:groupList){
				ob = new JSONObject();
				ob.put("id", gp.getGroupId());
				ob.put("name", gp.getGroupName());
				groupArray.add(ob);
			}
			result.put("records", groupArray);
		}
		else{
			result.put("records", "[]");
		}
		return result;
	}
	
	@RequestMapping(value="/groupsWithUsers", method=RequestMethod.GET )
	@ResponseBody
	public Object getGroupsWithUsers(){
		JSONObject result = new JSONObject();
		List<TblGroup> groupList = groupService.getGroupsWithUsers();
		if(groupList!=null){
			JSONArray groupArray = new JSONArray();
			JSONObject ob = null;
			List<TblUser> userList = null;
			JSONArray userInGroupArray = null;
			JSONObject userInGroup = null;
			for(TblGroup gp:groupList){
				ob = new JSONObject();
				ob.put("id", gp.getGroupId());
				ob.put("name", gp.getGroupName());
				userList = gp.getUserlist();
				if(userList!=null){
					userInGroupArray = new JSONArray();
					for(TblUser user:userList){
						userInGroup = new JSONObject();
						userInGroup.put("name", user.getName());
						userInGroupArray.add(userInGroup);
					}
					ob.put("userInGroup", userInGroupArray);
				}
				groupArray.add(ob);
			}
			result.put("records", groupArray);
		}
		else{
			result.put("records", "[]");
		}
		return result;
	}
	
	@RequestMapping(value="/group/edit", method=RequestMethod.GET )
	public String viewEditGroup(HttpServletRequest req){
		req.setAttribute("groupId", req.getParameter("groupId"));
		req.setAttribute("groupName", req.getParameter("groupName"));
		return "wf-group";
	}
	
	@RequestMapping(value="/group/users", method=RequestMethod.GET )
	@ResponseBody
	public Object getUsersInGroup(HttpServletRequest req){
		JSONObject result = new JSONObject();
		String groupId = req.getParameter("groupId");
		if(StringUtils.isEmpty(groupId)){
			return result;
		}
		result.put("records", groupService.getUserInSpecGroup(groupId));
		return result;
	}
	
	@RequestMapping(value="/group/users/add", method=RequestMethod.GET )
	@ResponseBody
	public Object getAddableUsers(HttpServletRequest req){
		JSONObject result = new JSONObject();
		String groupId = req.getParameter("groupId");
		if(StringUtils.isEmpty(groupId)){
			return result;
		}
		List<TblUser> userList = groupService.getAddableUsers(groupId);
		if(userList!=null){
			Map<String,TblGroup> groupMap = new HashMap<String,TblGroup>();
			String groupIdTmp = null;
			List<TblUser> usersInGp = null;
			JSONArray userArray = new JSONArray();
			JSONObject userJson = null;
			for(TblUser user : userList){
				groupIdTmp = user.getGroupId();
				if(!StringUtils.isEmpty(groupIdTmp)){
					if(groupMap.containsKey(groupIdTmp)){
						groupMap.get(groupIdTmp).addUser(user);
					}else{
						TblGroup group = new TblGroup();
						group.setGroupId(groupIdTmp);
						group.setGroupName(user.getGroupName());
						usersInGp = new ArrayList<TblUser>();
						usersInGp.add(user);
						group.setUserlist(usersInGp);
						groupMap.put(groupIdTmp, group);
					}
				}
				else{
					userJson = new JSONObject();
					userJson.put("id", user.getId());
					userJson.put("name", user.getName());
					userArray.add(userJson);
				}
			}
			Collection<TblGroup> groups = groupMap.values();
			JSONArray groupArray = new JSONArray();
			JSONObject gpJson = null;
			JSONArray usersInGroup = null;
			JSONObject userInGpJson = null;
			for(TblGroup gp:groups){
				gpJson = new JSONObject();
				gpJson.put("id", gp.getGroupId());
				gpJson.put("name", gp.getGroupName());
				List<TblUser> ulist = gp.getUserlist();
				usersInGroup = new JSONArray(ulist.size());
				for(TblUser u:ulist){
					userInGpJson = new JSONObject();
					userInGpJson.put("id", u.getId());
					userInGpJson.put("name", u.getName());
					usersInGroup.add(userInGpJson);
				}
				gpJson.put("usersInGroup", usersInGroup);
				groupArray.add(gpJson);
			}
			result.put("groups", groupArray);
			result.put("users", userArray);
		}
		return result;
	}
	
	
	@RequestMapping(value="/group/users/add", method=RequestMethod.POST )
	@ResponseBody
	public Object getAddableUsers(@RequestBody JSONObject parm){
		String groupId = parm.getString("groupId");
		String userIds = parm.getString("userIds");
		String[] userIdArray = userIds.split(",");
		JSONObject result = new JSONObject();
		if(userIdArray.length==0){
			result.put("result", "no record added");
			return result;
		}
		groupService.addUsers2Group(groupId, userIdArray);
		result.put("result", "succ");
		return result;
	}
	
	@RequestMapping(value="/group/users/delete", method=RequestMethod.POST )
	@ResponseBody
	public Object deleteUserInGroup(@RequestBody JSONObject parm){
		String groupId = parm.getString("groupId");
		String userIds = parm.getString("userIds");
		String[] userIdArray = userIds.split(",");
		JSONObject result = new JSONObject();
		if(userIdArray.length==0){
			result.put("result", "no record added");
			return result;
		}
		groupService.deleteUsers2Group(groupId, userIdArray);
		result.put("result", "succ");
		return result;
	}
	
	@RequestMapping(value="/group", method=RequestMethod.DELETE )
	@ResponseBody
	public Object deleteGroup(HttpServletRequest req){
		String groupId = req.getParameter("groupId");
		JSONObject result = new JSONObject();
		groupService.deleteById(groupId);
		result.put("result", "succ");
		return result;
	}
	
}
