package com.xb.service.impl;

import org.springframework.stereotype.Service;

import com.xb.persistent.mapper.WfDefMapper;
import com.xb.persistent.WfDef;
import com.xb.service.IWfDefService;
import com.baomidou.framework.service.impl.SuperServiceImpl;

/**
 *
 * WfDef 表数据服务层接口实现类
 *
 */
@Service
public class WfDefServiceImpl extends SuperServiceImpl<WfDefMapper, WfDef> implements IWfDefService {


}