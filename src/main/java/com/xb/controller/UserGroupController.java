package com.xb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xb.base.BaseController;
import com.xb.persistent.TblGroup;
import com.xb.persistent.TblUser;
import com.xb.service.ITblGroupService;
import com.xb.service.ITblUserService;

@Controller
@RequestMapping("/usergroup")
public class UserGroupController extends BaseController {
	
	private static final String SESSION_USERINFO = "USERINFO";
	
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
		result.put("records", userService.selectList(new TblUser()));
		return result;
	}
	
	@RequestMapping(value="/groups", method=RequestMethod.GET )
	@ResponseBody
	public Object getAllGroups(){
		JSONObject result = new JSONObject();
		result.put("records", groupService.selectList(new TblGroup()));
		return result;
	}
	
}
