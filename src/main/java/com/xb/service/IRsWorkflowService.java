package com.xb.service;

import com.xb.persistent.RsWorkflow;
import com.xb.vo.ModuleVO;
import com.xb.vo.WFDetailVO;
import com.baomidou.framework.service.ISuperService;

/**
 *
 * RsWorkflow 表数据服务层接口
 *
 */
public interface IRsWorkflowService extends ISuperService<RsWorkflow> {

	public void createWF4Module(ModuleVO module, WFDetailVO wfDetail) ;
	public WFDetailVO getWF4Module(String moduleId, String wfId);
	
}