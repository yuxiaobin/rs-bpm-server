package com.xb.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xb.persistent.mapper.WfTaskAssignMapper;
import com.xb.persistent.WfTaskAssign;
import com.xb.service.IWfTaskAssignService;
import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.mysql.jdbc.StringUtils;

/**
 *
 * WfTaskAssign 表数据服务层接口实现类
 *
 */
@Service
public class WfTaskAssignServiceImpl extends CommonServiceImpl<WfTaskAssignMapper, WfTaskAssign> implements IWfTaskAssignService {

	public List<WfTaskAssign> selectTaskAssignerListWithName(String taskId){
		if(StringUtils.isNullOrEmpty(taskId)){
			return null;
		}
		return baseMapper.getTaskAssignerListWithName(taskId);
	}
}