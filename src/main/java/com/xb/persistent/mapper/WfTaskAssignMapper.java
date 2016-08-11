package com.xb.persistent.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.CommonMapper;
import com.xb.persistent.WfTaskAssign;

/**
 *
 * WfTaskAssign 表数据库控制层接口
 *
 */
public interface WfTaskAssignMapper extends CommonMapper<WfTaskAssign> {

	public List<WfTaskAssign> getTaskAssignerListWithName(@Param("taskId") String taskId);
}