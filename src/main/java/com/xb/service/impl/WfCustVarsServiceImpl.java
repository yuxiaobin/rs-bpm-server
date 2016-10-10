package com.xb.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.xb.persistent.WfCustVars;
import com.xb.persistent.WfVersion;
import com.xb.persistent.mapper.WfCustVarsMapper;
import com.xb.service.IWfCustVarsService;
import com.xb.service.IWfVersionService;

/**
 *
 * WfCustVars 表数据服务层接口实现类
 *
 */
@Service
public class WfCustVarsServiceImpl extends CommonServiceImpl<WfCustVarsMapper, WfCustVars> implements IWfCustVarsService {

	@Autowired
	IWfVersionService wfVersionService;
	
	@Override
	public List<WfCustVars> getCustVars(String refMkid, Integer version) {
		WfVersion wfParm = new WfVersion();
		wfParm.setRefMkid(refMkid);
		wfParm.setVERSION(version);
		List<WfVersion> wfList = wfVersionService.selectList(wfParm,"version desc");
		if(wfList==null || wfList.isEmpty()){
			return null;
		}
		WfCustVars custVarParm = new WfCustVars();
		custVarParm.setWfId(wfList.get(0).getWfId());
		return this.selectList(custVarParm, "VAR_TYPE,var_code");
	}

}