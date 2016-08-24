package com.xb.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.mysql.jdbc.StringUtils;
import com.xb.common.WFConstants;
import com.xb.persistent.WfTaskAssign;
import com.xb.persistent.mapper.WfTaskAssignMapper;
import com.xb.service.ITblUser2groupService;
import com.xb.service.IWfTaskAssignService;
import com.xb.vo.UsersGroupsVO4Task;

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
}