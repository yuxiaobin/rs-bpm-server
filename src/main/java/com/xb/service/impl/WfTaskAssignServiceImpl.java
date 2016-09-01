package com.xb.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.mysql.jdbc.StringUtils;
import com.xb.common.WFConstants;
import com.xb.persistent.TblUser2group;
import com.xb.persistent.WfTaskAssign;
import com.xb.persistent.mapper.WfTaskAssignMapper;
import com.xb.service.ITblUser2groupService;
import com.xb.service.IWfTaskAssignService;

/**
 *
 * WfTaskAssign 表数据服务层接口实现类
 *
 */
@Service
public class WfTaskAssignServiceImpl extends CommonServiceImpl<WfTaskAssignMapper, WfTaskAssign> implements IWfTaskAssignService {

	@Autowired
	ITblUser2groupService userGroupService;
	
	public List<WfTaskAssign> selectTaskAssignerListWithName(String taskId){
		if(StringUtils.isNullOrEmpty(taskId)){
			return null;
		}
		return baseMapper.getTaskAssignerListWithName(taskId);
	}

	@Override
	public JSONObject getUsersGroupsByTaskId(String taskId) {
		WfTaskAssign parm = new WfTaskAssign();
		parm.setTaskId(taskId);
		List<WfTaskAssign> assignerList = this.selectList(parm);
		if(assignerList==null || assignerList.isEmpty()){
			return userGroupService.getUsersGroupsDtlList(null, null).toJSONObject(assignerList);
		}else{
			List<String> userIdList = new ArrayList<String>();
			List<String> groupIdList = new ArrayList<String>();
			for(WfTaskAssign assigner: assignerList){
				if(WFConstants.AssignType.USER.equals(assigner.getAssignType())){
					userIdList.add(assigner.getAssignRelId());
				}else{
					groupIdList.add(assigner.getAssignRelId());
				}
			}
			return userGroupService.getUsersGroupsDtlList(userIdList, groupIdList).toJSONObject(assignerList);
		}
	}

	
	@Override
	public List<String> getAssignedUsersByTaskId(String taskId) {
		WfTaskAssign assign = new WfTaskAssign();
		assign.setTaskId(taskId);
		List<WfTaskAssign> assignList = this.selectList(assign);
		if(assignList==null || assignList.isEmpty()){
			return null;
		}
		
		Set<String> userIdSet = new HashSet<String>();
		TblUser2group ug = new TblUser2group();
		for(WfTaskAssign as:assignList){
			if(WFConstants.AssignType.USER.equals(as.getAssignType())){
				userIdSet.add(as.getAssignRelId());
			}else{
				ug.setGroupId(as.getAssignRelId());
				List<TblUser2group> u2gList = userGroupService.selectList(ug);
				if(u2gList!=null){
					for(TblUser2group u2g:u2gList){
						userIdSet.add(u2g.getUserId());
					}
				}
			}
		}
		List<String> userIdList = new ArrayList<String>(userIdSet.size());
		userIdList.addAll(userIdSet);
		return userIdList;
	}
}