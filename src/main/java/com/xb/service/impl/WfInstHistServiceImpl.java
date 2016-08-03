package com.xb.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xb.persistent.mapper.WfInstHistMapper;
import com.xb.persistent.WfInstHist;
import com.xb.service.IWfInstHistService;
import com.baomidou.framework.service.impl.CommonServiceImpl;

/**
 *
 * WfInstHist 表数据服务层接口实现类
 *
 */
@Service
public class WfInstHistServiceImpl extends CommonServiceImpl<WfInstHistMapper, WfInstHist> implements IWfInstHistService {

	public List<WfInstHist> viewWfInstHistory(String instId){
		if(instId==null){
			System.out.println("viewWfInstHistory=====instId="+instId);
			return null;
		}
		WfInstHist histParm = new WfInstHist();
		histParm.setInstId(instId);
		return this.selectList(histParm, "OPT_SEQ desc");
	}
}