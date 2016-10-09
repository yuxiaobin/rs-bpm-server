package com.xb.service;

import java.util.List;

import com.baomidou.framework.service.ICommonService;
import com.xb.common.BusinessException;
import com.xb.persistent.WfAwt;
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
	 * 业务表只会存refMkid & instNum(实例号)
	 * 
	 * @param refMkid
	 * @param instNum
	 * @param currUserId
	 * @return
	 */
	public WfAwt getAwtByParam(String refMkid, int instNum, String currRecallUser);
	
	/**
	 * 根据撤回人，找上一步处理人包含该撤回人的所有待办事宜
	 * @param refMkid
	 * @param instNum
	 * @param currOptUser
	 * @return
	 */
	public List<WfAwt> getAwt4Recall(String refMkid, int instNum, String currOptUser);
	
	public List<WfAwt> getAwtListByInstId(String instId);
	
	/**
	 * 更新待办事宜：
	 * 	删除旧的待办事宜（会签和普通的情况不同）
	 *  新增待办事宜（会签的特殊处理：需最后一个会签人员完成后出发新的待办事宜）
	 * @param prev
	 * @param optVO
	 * @param currUserId
	 */
	public void renewAwt(WfAwt prev, WfTask currTask, WfTask nextTask, TaskOptVO optVO, String currUserId) throws BusinessException;
}