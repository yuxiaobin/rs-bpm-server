package com.xb.service;

import java.util.List;

import com.baomidou.framework.service.ICommonService;
import com.xb.persistent.WfTask;
import com.xb.vo.TaskVO;
import com.xb.vo.WFDetailVO;

/**
 *
 * WfTask 表数据服务层接口
 *
 */
public interface IWfTaskService extends ICommonService<WfTask> {
	
	public List<TaskVO> getTasksInbox(String userId);
	
	public void startWF4Module(String moduleId,String userId);
	
	public void processTask(String instId, String userId, String opt);
	
	/**
	 * 获取当前工作流状态
	 * @param histId
	 * @return
	 */
	public WFDetailVO getWFStatus(String instId);
}