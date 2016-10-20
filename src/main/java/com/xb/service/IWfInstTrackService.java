package com.xb.service;

import com.xb.persistent.WfInstTrack;
import com.baomidou.framework.service.ICommonService;

/**
 *
 * WfInstTrack 表数据服务层接口
 *
 */
public interface IWfInstTrackService extends ICommonService<WfInstTrack> {

	public boolean doRollback(String trackID) throws Exception;

}