package com.xb.persistent.mapper;

import com.xb.persistent.WfTask;
import com.xb.vo.TaskVO;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.CommonMapper;

/**
 *
 * WfTask 表数据库控制层接口
 *
 */
public interface WfTaskMapper extends CommonMapper<WfTask> {
	
	public List<TaskVO> getTasksInbox(@Param("userId") String userId, @Param("likeUserId") String likeUserId);

	public List<WfTask> getTaskListWithStatus(@Param("instId") String instId);

}