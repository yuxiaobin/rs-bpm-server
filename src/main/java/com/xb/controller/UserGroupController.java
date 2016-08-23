package com.xb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
}
