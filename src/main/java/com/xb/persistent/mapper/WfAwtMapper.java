package com.xb.persistent.mapper;

import com.xb.persistent.WfAwt;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.CommonMapper;

/**
 *
 * WfAwt 表数据库控制层接口
 *
 */
public interface WfAwtMapper extends CommonMapper<WfAwt> {

	public List<WfAwt> getAwfByUserId(@Param("userId") String userId);
}