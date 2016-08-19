package com.xb.service;

import java.util.List;

import com.baomidou.framework.service.ICommonService;
import com.xb.persistent.WfTaskAssign;
import com.xb.vo.UsersGroupsVO4Task;

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
	public UsersGroupsVO4Task getUsersGroupsByTaskId(String taskId);
}