package com.xb.service;

import com.xb.persistent.TblUser;
import com.baomidou.framework.service.ICommonService;

/**
 *
 * TblUser 表数据服务层接口
 *
 */
public interface ITblUserService extends ICommonService<TblUser> {

	public void deleteJunitData(String refMkid);
	
	public String getCurrentAssigner4Instance(String refMkid, int instNum);
	
	public String getUsernameByUserId(String userId);

}