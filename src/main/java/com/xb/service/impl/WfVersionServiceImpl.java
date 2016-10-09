package com.xb.service.impl;

import org.springframework.stereotype.Service;

import com.xb.persistent.mapper.WfVersionMapper;
import com.xb.persistent.WfVersion;
import com.xb.service.IWfVersionService;
import com.baomidou.framework.service.impl.CommonServiceImpl;

/**
 *
 * WfVersion 表数据服务层接口实现类
 *
 */
@Service
public class WfVersionServiceImpl extends CommonServiceImpl<WfVersionMapper, WfVersion> implements IWfVersionService {


}