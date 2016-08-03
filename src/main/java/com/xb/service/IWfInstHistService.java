package com.xb.service;

import java.util.List;

import com.baomidou.framework.service.ICommonService;
import com.xb.persistent.WfInstHist;

/**
 *
 * WfInstHist 表数据服务层接口
 *
 */
public interface IWfInstHistService extends ICommonService<WfInstHist> {

	public List<WfInstHist> viewWfInstHistory(String instId);
}