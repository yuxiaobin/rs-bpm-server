package com.xb.service.impl;

import org.springframework.stereotype.Service;

import com.xb.persistent.mapper.WfTaskConnMapper;
import com.xb.persistent.WfTaskConn;
import com.xb.service.IWfTaskConnService;
import com.baomidou.framework.service.impl.SuperServiceImpl;

/**
 *
 * WfTaskConn 表数据服务层接口实现类
 *
 */
@Service
public class WfTaskConnServiceImpl extends SuperServiceImpl<WfTaskConnMapper, WfTaskConn> implements IWfTaskConnService {


}