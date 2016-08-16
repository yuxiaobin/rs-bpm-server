package com.xb.service;

import java.util.List;

import com.baomidou.framework.service.ICommonService;
import com.xb.persistent.WfAwt;

/**
 *
 * WfAwt 表数据服务层接口
 *
 */
public interface IWfAwtService extends ICommonService<WfAwt> {

	public List<WfAwt> getAwfByUserId(String userId);
}