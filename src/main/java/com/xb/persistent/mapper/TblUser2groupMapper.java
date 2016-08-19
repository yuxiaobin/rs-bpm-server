package com.xb.persistent.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.CommonMapper;
import com.xb.persistent.TblUser;
import com.xb.persistent.TblUser2group;

/**
 *
 * TblUser2group 表数据库控制层接口
 *
 */
public interface TblUser2groupMapper extends CommonMapper<TblUser2group> {

	public List<TblUser> getUserListByIdList(List<String> userIdList);
	
	public List<TblUser> getGroupListWithUsersByIdList(List<String> groupIdList);
	
	public List<TblUser> getGroupListWithUsersAll();
}