package com.xb.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xb.persistent.mapper.WfInstHistMapper;
import com.xb.persistent.WfInstHist;
import com.xb.service.IWfInstHistService;
import com.xb.vo.TaskOptVO;
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
	
	public String createHistRecord(TaskOptVO optVO, String currUserId){
		WfInstHist parm = new WfInstHist();
		parm.setInstId(optVO.getInstId());
		List<WfInstHist> histList = this.selectList(parm, "OPT_SEQ desc");
		int nextOptSeq = 1;
		WfInstHist prefHist = null;
		if(histList!=null && !histList.isEmpty()){
			prefHist = histList.get(0);
			nextOptSeq = prefHist.getOptSeq()+1;
		}
		
		WfInstHist hist = new WfInstHist();
		hist.setInstId(optVO.getInstId());
		hist.setNextAssigner(optVO.getNextAssigners());
		hist.setOptSeq(nextOptSeq);
		hist.setOptUser(currUserId);
		hist.setOptType(optVO.getOptType());
		hist.setTaskId(optVO.getCurrTaskId());
		this.insert(hist);
		return hist.getHistId();
	}
}