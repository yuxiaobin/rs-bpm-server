package com.xb.persistent.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.CommonMapper;
import com.xb.persistent.WfCustVars;

/**
 *
 * WfCustVars 表数据库控制层接口
 *
 */
public interface WfCustVarsMapper extends CommonMapper<WfCustVars> {

	public List<JSONObject> getBuzDataByEntity(Map<String,Object> parm);
//	public List<JSONObject> getBuzDataByEntityWithCond(Map<String,Object> parm);
	
	@SuppressWarnings("rawtypes")
	public List<LinkedHashMap> getBuzDataByEntityWithOrder(Map<String, Object> parm);
}