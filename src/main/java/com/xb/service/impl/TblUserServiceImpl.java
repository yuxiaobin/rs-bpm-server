package com.xb.service.impl;

import org.springframework.stereotype.Service;

import com.xb.persistent.mapper.TblUserMapper;
import com.xb.persistent.TblUser;
import com.xb.service.ITblUserService;
import com.baomidou.framework.service.impl.CommonServiceImpl;

/**
 *
 * TblUser 表数据服务层接口实现类
 *
 */
@Service
public class TblUserServiceImpl extends CommonServiceImpl<TblUserMapper, TblUser> implements ITblUserService {


}