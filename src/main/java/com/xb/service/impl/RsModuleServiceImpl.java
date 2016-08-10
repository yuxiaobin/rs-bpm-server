package com.xb.service.impl;

import org.springframework.stereotype.Service;

import com.xb.persistent.mapper.RsModuleMapper;
import com.xb.persistent.RsModule;
import com.xb.service.IRsModuleService;
import com.baomidou.framework.service.impl.CommonServiceImpl;

/**
 *
 * RsModule 表数据服务层接口实现类
 *
 */
@Service
public class RsModuleServiceImpl extends CommonServiceImpl<RsModuleMapper, RsModule> implements IRsModuleService {

}