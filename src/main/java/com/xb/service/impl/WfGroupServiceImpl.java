package com.xb.service.impl;

import org.springframework.stereotype.Service;

import com.xb.persistent.mapper.WfGroupMapper;
import com.xb.persistent.WfGroup;
import com.xb.service.IWfGroupService;
import com.baomidou.framework.service.impl.SuperServiceImpl;

/**
 *
 * WfGroup 表数据服务层接口实现类
 *
 */
@Service
public class WfGroupServiceImpl extends SuperServiceImpl<WfGroupMapper, WfGroup> implements IWfGroupService {


}