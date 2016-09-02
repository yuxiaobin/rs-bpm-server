package com.xb.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.xb.common.WFConstants;
import com.xb.persistent.WfAwt;
import com.xb.persistent.WfInstance;
import com.xb.persistent.WfTask;
import com.xb.persistent.mapper.WfAwtMapper;
import com.xb.service.IWfAwtService;
import com.xb.service.IWfInstanceService;
import com.xb.vo.TaskOptVO;

/**
 *
 * WfAwt 表数据服务层接口实现类
 *
 */
@Service
public class WfAwtServiceImpl extends CommonServiceImpl<WfAwtMapper, WfAwt> implements IWfAwtService {
	
	@Autowired
	IWfInstanceService instService;

	/**
	 * 根据登录用户，获取待办事宜。
	 */
	public List<WfAwt> getAwtByUserId(String userId){
		
		return baseMapper.getAwtByUserId(userId);
	}
	
	public WfAwt getAwtByParam(String rsWfId, int instNum, String currUserId){
		Map<String,Object> parmMap = new HashMap<String,Object>();
		parmMap.put("rsWfId", rsWfId);
		parmMap.put("instNum", instNum);
		parmMap.put("currUserId", currUserId);
		List<WfAwt> awtList = baseMapper.getAwtByParam(parmMap);
		if(awtList!=null && !awtList.isEmpty()){
			for(WfAwt awt:awtList){
				String completeFlag = awt.getCompleteFlag();
				if(completeFlag==null || completeFlag.equals("N")){
					return awt;
				}
			}
			return awtList.get(0);
		}
		return null;
	}
	
	public void renewAwt(WfAwt prev, WfTask currtask,WfTask nextTask,  WfInstance wfInst, TaskOptVO optVO, String currUserId){
		String instId = prev.getInstId();
		if(WFConstants.TxCodes.COUNTERSIGN.equals(currtask.getTxType())){
			prev.setCompleteFlag("Y");
			this.updateById(prev);
			String csOptJson = currtask.getSignChoices();
			JSONObject csOpt = (JSONObject) JSONObject.parse(csOptJson);
			WfAwt parm = new WfAwt();
			parm.setInstId(instId);
			parm.setCompleteFlag("Y");
			int completedCount = this.selectCount(parm);
			if(Boolean.TRUE.toString().equals(csOpt.getString("AllHandledThenGo"))){
				parm.setCompleteFlag(null);
				int allCount = this.selectCount(parm);
				if(allCount!=completedCount){
					//not finished, return
					updateCurrAssigners4CS(wfInst, currUserId);
					return;
				}
			}
			else{
				if(Boolean.TRUE.toString().equals(csOpt.getString("PartHandledThenGo"))){
					int AtLeastHandled = csOpt.getIntValue("AtLeastHandled");
					if(AtLeastHandled==0){
						AtLeastHandled = 1;
					}
					if(completedCount<AtLeastHandled){
						//not finished, return
						updateCurrAssigners4CS(wfInst, currUserId);
						return;
					}
				}
			}
		}
		//delete by rsWfId&instNum&taskId
		WfAwt parm = new WfAwt();
		parm.setInstId(instId);
		parm.setCompleteFlag(null);
//		parm.setTaskIdCurr(prev.getTaskIdCurr());
		this.deleteSelective(parm);
		//create new awt(s) with next taskId
		String nextAssigners = optVO.getNextAssigners();
		if(nextAssigners!=null && !optVO.isNextEndTaskFlag()){
			String[] nextAssignersArr = nextAssigners.split(",");
			WfAwt awt = null;
			Date beginDate = new Date();
			Date limitDate = calculateDate(beginDate, nextTask.getTimeLimitTp(), nextTask.getTimeLimit());
			Date alarmDate = nextTask.getAlarmTime()==null?null:calculateDate(beginDate, nextTask.getAlarmTimeTp(), nextTask.getAlarmTime());
			for(String assigner : nextAssignersArr){
				if(!StringUtils.isEmpty(assigner)){
					awt = new WfAwt();
					awt.setAssignerId(assigner);
					awt.setAwtBegin(beginDate);
					awt.setAwtEnd(limitDate);
					awt.setAwtAlarm(alarmDate);
					awt.setHistIdPre(optVO.getPrevInstHistId());
					awt.setInstId(instId);
					awt.setTaskIdCurr(optVO.getNextTaskId());
					this.insert(awt);
				}
			}
		}
		wfInst.setCurrAssigners(nextAssigners);
		if(optVO.isNextEndTaskFlag()){
			wfInst.setWfStatus(WFConstants.WFStatus.DONE);
		}
		instService.updateById(wfInst);
	}
	
	private Date calculateDate(Date beginDate, String timeType, Integer amount){
		Date limitDate = null;
		timeType = timeType==null?"":timeType;
		amount = amount==null?0:amount;
		switch (timeType) {
		case "M":
			limitDate = DateUtils.addMinutes(beginDate, amount);
			break;
		case "H":
			limitDate = DateUtils.addHours(beginDate, amount);
			break;
		case "D":
			limitDate = DateUtils.addDays(beginDate, amount);
			break;
		default:
//			limitDate = beginDate;
			break;
		}
		return limitDate;
	}
	
	private void updateCurrAssigners4CS(WfInstance wfInst, String currUserId){
		String currAssigners4Inst = wfInst.getCurrAssigners();
		if(currAssigners4Inst!=null){
			if(currAssigners4Inst.contains(currUserId+",")){
				wfInst.setCurrAssigners(currAssigners4Inst.replace(currUserId+",", ""));
				instService.updateById(wfInst );//更新当前处理人
				wfInst.setCurrAssigners(currAssigners4Inst);
			}
		}
	}

	@Override
	public List<WfAwt> getAwtListByInstId(String instId) {
		Map<String,Object> parmMap = new HashMap<String,Object>();
		parmMap.put("instId", instId);
		return baseMapper.getAwtByParam(parmMap);
	}
}