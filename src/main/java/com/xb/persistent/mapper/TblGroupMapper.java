package com.xb.persistent.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.CommonMapper;
import com.xb.persistent.TblGroup;
import com.xb.persistent.TblUser;

/**
 *
 * TblGroup 表数据库控制层接口
 *
 */
public interface TblGroupMapper extends CommonMapper<TblGroup> {

	public List<TblUser> getAllUsersInGroup();
}