package com.xb.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.framework.service.ICommonService;
import com.xb.persistent.WfTaskAssign;

/**
 *
 * WfTaskAssign 表数据服务层接口
 *
 */
public interface IWfTaskAssignService extends ICommonService<WfTaskAssign> {

	public List<WfTaskAssign> selectTaskAssignerListWithName(String taskId);
	
	
	/**
	 * 根据taskId获取该它配置的用户和用户组.
	 * @param taskId
	 * @return
	 */
	public JSONObject getUsersGroupsByTaskId(String taskId);
	
	/**
	 * return a list of userId
	 * 
	 * @param taskId
	 * @return
	 */
	public List<String> getAssignedUsersByTaskId(String taskId);
	
	
}