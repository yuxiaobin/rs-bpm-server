package com.xb.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.xb.common.WFConstants;
import com.xb.persistent.WfInstHist;
import com.xb.persistent.WfInstance;
import com.xb.persistent.WfTask;
import com.xb.persistent.WfTaskConn;
import com.xb.persistent.mapper.WfTaskMapper;
import com.xb.service.IWfInstHistService;
import com.xb.service.IWfInstanceService;
import com.xb.service.IWfTaskConnService;
import com.xb.service.IWfTaskService;
import com.xb.vo.TaskVO;

/**
 *
 * WfTask 表数据服务层接口实现类
 *
 */
@Service
public class WfTaskServiceImpl extends SuperServiceImpl<WfTaskMapper, WfTask> implements IWfTaskService {

	@Autowired
	IWfInstHistService histService;
	@Autowired
	IWfTaskConnService taskConnService;
	@Autowired
	IWfInstanceService instService;
	
	public List<TaskVO> getTasksInbox(String userId){
		return baseMapper.getTasksInbox(userId);
	}

	@Transactional
	public void processTask(String histId, String userId, String opt){
		WfInstHist histParm = new WfInstHist();
		histParm.setHistId(histId);
		WfInstHist histCurr = histService.selectOne(histParm);
		histCurr.setSTATUS("D");
		histService.updateById(histCurr);
		
		WfTaskConn connParm = new WfTaskConn();
		connParm.setSourceTaskId(histCurr.getTaskId());
		WfTaskConn conn = taskConnService.selectOne(connParm);
		if(conn==null){
			System.err.println("no conn record for taksId="+histCurr.getTaskId()+", workflow over");
			return;
		}
		String targetTaskId = conn.getTargetTaskId();
		WfTask taskParm = new WfTask();
		taskParm.setTaskId(targetTaskId);
		WfTask taskNext = this.selectOne(taskParm);
		
		boolean wfDoneFlag = false;
		WfInstHist histNext = new WfInstHist();
		histNext.setInstId(histCurr.getInstId());
		histNext.setOptSeq(histCurr.getOptSeq()+1);
		histNext.setOptType(opt);//A:Approve, R:Reject
		histNext.setOptUser(userId);
		histNext.setSTATUS("I");
		histNext.setWfId(histCurr.getWfId());
		if("R".equals(opt)){
			histNext.setTaskId(conn.getSourceTaskId());
			histNext.setNextAssigner(histCurr.getOptUser());
		}else{
			histNext.setTaskId(taskNext.getTaskId());
			histNext.setNextAssigner(taskNext.getAssignUsers());//TODO:  this field real need?
			if(taskNext.getTaskType().equals(WFConstants.TaskTypes.E.getTypeCode())){
				histNext.setSTATUS("D");
				wfDoneFlag = true;
			}
		}
		//For next task is end
		histService.insert(histNext);
		
		if(wfDoneFlag){
			WfInstance instParm = new WfInstance();
			instParm.setInstId(histCurr.getInstId());
			WfInstance inst = instService.selectOne(instParm);
			inst.setWfStatus("D");
			instService.updateById(inst);
			System.out.println("update instance to Done status");
		}
		
	}
}