package com.xb.service;

import com.alibaba.fastjson.JSONObject;
import com.xb.vo.TaskOptVO;

public interface IWfApiService {
	
	/**
	 * Validate operation.
	 * 
	 * @param optVO
	 * @param result
	 * @return
	 */
	public boolean validateOperate(TaskOptVO optVO, JSONObject result);

}
