package com.xb.service;

import java.util.List;

import com.baomidou.framework.service.ISuperService;
import com.xb.persistent.WfTask;
import com.xb.vo.TaskVO;

/**
 *
 * WfTask 表数据服务层接口
 *
 */
public interface IWfTaskService extends ISuperService<WfTask> {
	
	public List<TaskVO> getTasksInbox(String userId);
	
	public void processTask(String histId, String userId, String opt);

}