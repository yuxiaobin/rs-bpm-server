package com.xb.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.xb.persistent.WfTask;

public interface IWfConditionService {
	
	public String getNextTaskIdByCondResult(WfTask condTask, String refMkid, int wfInstNum);
	
	public boolean evaluateExpression(String expression, String refMkid, int wfInstNum, String wfId);
	
	public JSONObject getFuncVarValues(String refMkid, Integer wfInstNum);
	
	public List<JSONObject> getEntityDataListBySql(String sql, boolean withOrder);
}
