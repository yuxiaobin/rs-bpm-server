package com.xb.service;

import com.xb.persistent.WfTask;

public interface IWfConditionService {
	
	public String getNextTaskIdByCondResult(WfTask condTask, String refMkid, int wfInstNum);
}
