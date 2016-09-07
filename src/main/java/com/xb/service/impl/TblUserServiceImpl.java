package com.xb.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.xb.persistent.TblUser;
import com.xb.persistent.mapper.TblUserMapper;
import com.xb.service.ITblUserService;

/**
 *
 * TblUser 表数据服务层接口实现类
 *
 */
@Service
public class TblUserServiceImpl extends CommonServiceImpl<TblUserMapper, TblUser> implements ITblUserService {

	@Override
	public void deleteJunitData(String refMkid) {
		baseMapper.deleteUnitResult1(refMkid);
		baseMapper.deleteUnitResult2(refMkid);
		baseMapper.deleteUnitResult3(refMkid);
		
	}

	

}