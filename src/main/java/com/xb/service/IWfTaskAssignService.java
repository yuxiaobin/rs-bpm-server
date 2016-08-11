package com.xb.service;

import java.util.List;

import com.baomidou.framework.service.ICommonService;
import com.xb.persistent.WfTaskAssign;

/**
 *
 * WfTaskAssign 表数据服务层接口
 *
 */
public interface IWfTaskAssignService extends ICommonService<WfTaskAssign> {

	public List<WfTaskAssign> selectTaskAssignerListWithName(String taskId);
}