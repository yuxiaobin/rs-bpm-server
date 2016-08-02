package com.xb.service.impl;

import org.springframework.stereotype.Service;

import com.xb.persistent.mapper.WfInstanceMapper;
import com.xb.persistent.WfInstance;
import com.xb.service.IWfInstanceService;
import com.baomidou.framework.service.impl.CommonServiceImpl;

/**
 *
 * WfInstance 表数据服务层接口实现类
 *
 */
@Service
public class WfInstanceServiceImpl extends CommonServiceImpl<WfInstanceMapper, WfInstance> implements IWfInstanceService {


}