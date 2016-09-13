package com.xb.service;

import java.util.List;

import com.baomidou.framework.service.ICommonService;
import com.xb.persistent.WfAwt;
import com.xb.persistent.WfInstHist;
import com.xb.vo.TaskOptVO;

/**
 *
 * WfInstHist 表数据服务层接口
 *
 */
public interface IWfInstHistService extends ICommonService<WfInstHist> {

	public List<WfInstHist> viewWfInstHistory(String instId);
	
	public List<WfInstHist> viewWfInstHistory(String rsWfId, Integer instNum);
	
	public String createHistRecord(TaskOptVO optVO, WfAwt awt , String currUserId);
	
	public List<WfAwt> getDoneListByMe(String userId, String gnmkId, Integer instNum);
	
}