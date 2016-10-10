package com.xb.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.xb.persistent.WfVersion;
import com.xb.persistent.mapper.WfVersionMapper;
import com.xb.service.IWfVersionService;

/**
 *
 * WfVersion 表数据服务层接口实现类
 *
 */
@Service
public class WfVersionServiceImpl extends CommonServiceImpl<WfVersionMapper, WfVersion> implements IWfVersionService {


}