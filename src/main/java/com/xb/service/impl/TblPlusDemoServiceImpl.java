package com.xb.service.impl;

import org.springframework.stereotype.Service;

import com.xb.persistent.mapper.TblPlusDemoMapper;
import com.xb.persistent.TblPlusDemo;
import com.xb.service.ITblPlusDemoService;
import com.baomidou.framework.service.impl.SuperServiceImpl;

/**
 *
 * TblPlusDemo 表数据服务层接口实现类
 *
 */
@Service
public class TblPlusDemoServiceImpl extends SuperServiceImpl<TblPlusDemoMapper, TblPlusDemo> implements ITblPlusDemoService {


}