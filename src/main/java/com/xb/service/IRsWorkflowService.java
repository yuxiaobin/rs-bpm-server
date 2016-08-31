package com.xb.service;

import com.xb.persistent.RsWorkflow;
import com.xb.vo.ModuleVO;
import com.xb.vo.WFDetailVO;
import com.baomidou.framework.service.ICommonService;

/**
 *
 * RsWorkflow 表数据服务层接口
 *
 */
public interface IRsWorkflowService extends ICommonService<RsWorkflow> {

	public void createWF4Module(ModuleVO module, WFDetailVO wfDetail) ;
	public WFDetailVO getWF4Module(String rsWfId, String wfId);
	
}