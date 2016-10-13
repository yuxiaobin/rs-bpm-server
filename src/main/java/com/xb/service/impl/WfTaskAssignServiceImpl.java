package com.xb.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.mysql.jdbc.StringUtils;
import com.rshare.service.wf.annotations.WfEntityBeanFactory;
import com.xb.common.WFConstants;
import com.xb.persistent.TblUser2group;
import com.xb.persistent.WfCustVars;
import com.xb.persistent.WfTaskAssign;
import com.xb.persistent.mapper.WfTaskAssignMapper;
import com.xb.service.ITblUser2groupService;
import com.xb.service.IWfConditionService;
import com.xb.service.IWfCustVarsService;
import com.xb.service.IWfTaskAssignService;
import com.xb.service.IWfTaskService;

/**
 *
 * WfTaskAssign 表数据服务层接口实现类
 *
 */
@Service
public class WfTaskAssignServiceImpl extends CommonServiceImpl<WfTaskAssignMapper, WfTaskAssign> implements IWfTaskAssignService {

	@Autowired
	ITblUser2groupService userGroupService;
	@Autowired
	IWfTaskService taskService;
	@Autowired
	IWfCustVarsService custVarsService;
	@Autowired
	WfEntityBeanFactory entityFactory;
	@Autowired
	IWfConditionService condService;
	
	public List<WfTaskAssign> selectTaskAssignerListWithName(String taskId){
		if(StringUtils.isNullOrEmpty(taskId)){
			return null;
		}
		return baseMapper.getTaskAssignerListWithName(taskId);
	}

	@Override
	public JSONObject getUsersGroupsByTaskId(String refMkid, Integer wfInstNum, String wfId, String taskId) {
		WfTaskAssign parm = new WfTaskAssign();
		parm.setTaskId(taskId);
		List<WfTaskAssign> assignerList = this.selectList(parm);
		WfCustVars custUserParm = new WfCustVars();
		custUserParm.setWfId(wfId);
		custUserParm.setVarType(WFConstants.CustVarTypes.VAR_TYPE_USER);
		List<WfCustVars> custUserList = custVarsService.selectList(custUserParm);
		
		if(assignerList==null || assignerList.isEmpty()){
			return userGroupService.getUsersGroupsDtlList(null, null, custUserList).toJSONObject(assignerList);
		}else{
			List<String> userIdList = new ArrayList<String>();
			List<String> groupIdList = new ArrayList<String>();
			List<WfCustVars> custUserInUseList = new ArrayList<WfCustVars>();
			Map<String,WfCustVars> custUserMap = new HashMap<String,WfCustVars>(custUserList.size());
			for(WfCustVars custU:custUserList){
				custUserMap.put(custU.getCustVarsId(), custU);
			}
			assignerList = evaluateAssigners(refMkid, wfInstNum, wfId, assignerList);
			for(WfTaskAssign assigner: assignerList){
				if(WFConstants.AssignType.USER.equals(assigner.getAssignType())){
					userIdList.add(assigner.getAssignRelId());
				}else if(WFConstants.AssignType.GROUP.equals(assigner.getAssignType())){
					groupIdList.add(assigner.getAssignRelId());
				}else{
					custUserInUseList.add(custUserMap.get(assigner.getAssignRelId()));
				}
			}
			return userGroupService.getUsersGroupsDtlList(userIdList, groupIdList, custUserInUseList).toJSONObject(assignerList);
		}
	}

	private List<WfTaskAssign> evaluateAssigners(String refMkid, Integer wfInstNum, String wfId, List<WfTaskAssign> assigner){
		List<WfTaskAssign> evalList = new ArrayList<WfTaskAssign>();
		for(WfTaskAssign ta:assigner){
			if(condService.evaluateExpression(ta.getExeCondition(), refMkid, wfInstNum, wfId)){
				evalList.add(ta);
			}
		}
		return evalList;
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