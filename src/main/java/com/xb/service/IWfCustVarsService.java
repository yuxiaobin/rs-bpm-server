package com.xb.service;

import java.util.List;

import com.baomidou.framework.service.ICommonService;
import com.xb.persistent.WfCustVars;

/**
 *
 * WfCustVars 表数据服务层接口
 *
 */
public interface IWfCustVarsService extends ICommonService<WfCustVars> {

	public List<WfCustVars> getCustVars(String refMkid, Integer version);
}