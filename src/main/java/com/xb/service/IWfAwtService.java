package com.xb.service;

import java.util.List;

import com.baomidou.framework.service.ICommonService;
import com.xb.persistent.WfAwt;
import com.xb.persistent.WfInstance;
import com.xb.persistent.WfTask;
import com.xb.vo.TaskOptVO;

/**
 *
 * WfAwt 表数据服务层接口
 *
 */
public interface IWfAwtService extends ICommonService<WfAwt> {

	public List<WfAwt> getAwtByUserId(String userId);
	
	/**
	 * 根据参数获取待办事宜
	 * 业务表只会存rsWfId & instNum(实例号)
	 * 
	 * @param rsWfId
	 * @param instNum
	 * @param currUserId
	 * @return
	 */
	public WfAwt getAwtByParam(String rsWfId, int instNum, String currUserId);
	
	/**
	 * 更新待办事宜：
	 * 	删除旧的待办事宜（会签和普通的情况不同）
	 *  新增待办事宜（会签的特殊处理：需最后一个会签人员完成后出发新的待办事宜）
	 * @param prev
	 * @param optVO
	 * @param currUserId
	 */
	public void renewAwt(WfAwt prev, WfTask currTask, WfTask nextTask, WfInstance wfInst, TaskOptVO optVO, String currUserId);
}