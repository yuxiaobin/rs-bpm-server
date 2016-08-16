package com.xb.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xb.persistent.mapper.WfAwtMapper;
import com.xb.persistent.WfAwt;
import com.xb.service.IWfAwtService;
import com.baomidou.framework.service.impl.CommonServiceImpl;

/**
 *
 * WfAwt 表数据服务层接口实现类
 *
 */
@Service
public class WfAwtServiceImpl extends CommonServiceImpl<WfAwtMapper, WfAwt> implements IWfAwtService {

	/**
	 * 根据登录用户，获取待办事宜。
	 */
	public List<WfAwt> getAwfByUserId(String userId){
		return baseMapper.getAwfByUserId(userId);
	}
}