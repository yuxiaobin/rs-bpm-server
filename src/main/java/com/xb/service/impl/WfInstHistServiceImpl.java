package com.xb.service.impl;

import org.springframework.stereotype.Service;

import com.xb.persistent.mapper.WfInstHistMapper;
import com.xb.persistent.WfInstHist;
import com.xb.service.IWfInstHistService;
import com.baomidou.framework.service.impl.SuperServiceImpl;

/**
 *
 * WfInstHist 表数据服务层接口实现类
 *
 */
@Service
public class WfInstHistServiceImpl extends SuperServiceImpl<WfInstHistMapper, WfInstHist> implements IWfInstHistService {


}