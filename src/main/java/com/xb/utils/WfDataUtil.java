package com.xb.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xb.common.WFConstants;
import com.xb.persistent.WfTask;
import com.xb.persistent.WfTaskAssign;
import com.xb.persistent.WfTaskConn;
import com.xb.vo.WFDetailVO;

public class WfDataUtil {

	/****************************
	 * generate json from object
	 ******************************/

	public static JSONObject generateWfJson(WFDetailVO wfDtl) {
		JSONArray tasks = genTaskJson(wfDtl.getTasks());
		JSONArray conns = genConnections(wfDtl.getConns(), wfDtl.getTasks());
		JSONObject result = new JSONObject();
		result.put("tasks", tasks);
		result.put("conns", conns);
		return result;
	}

	public static JSONArray genTaskJson(List<WfTask> tasks) {
		JSONArray array = new JSONArray();
		if (tasks == null) {
			return array;
		}
		JSONObject record = null;
		JSONObject pos = null;
		for (int i = 0; i < tasks.size(); ++i) {
			WfTask task = tasks.get(i);
			record = new JSONObject();
			record.put("pgId", task.getTaskPgId());
			record.put("rsType", WFConstants.TaskTypes.valueOf(task.getTaskType()).getTypeDescp());
			record.put("taskDescp", task.getTaskDescp());
			record.put("taskDescpDisp", task.getTaskDescpDisp());
			record.put("id", task.getTaskId());
//			record.put("assignUsers", task.getAssignUsers());
//			record.put("assignGroups", task.getAssignGroups());
			List<WfTaskAssign> assignerList = task.getAssignerList();
			JSONArray assigners = new JSONArray();
			if(assignerList!=null){
				JSONObject asnj = null;
				for(WfTaskAssign asn:assignerList){
					asnj = new JSONObject();
					String assignType = asn.getAssignType();
					if(assignType==null){
						assignType = "";
					}
					switch(assignType) {
						case "U": asnj.put("name", asn.getUserName());break;
						case "G": asnj.put("name", asn.getGroupName());break;
						default: break;
					}
					asnj.put("assignTypeCode", assignType);
					asnj.put("id", asn.getAssignRelId());
					asnj.put("defSelMod", asn.getDefSelFlag());
					String selectAllFlag = asn.getSelAllFlag();
					if(WFConstants.TaskSelectAllFlag.YES.equals(selectAllFlag)){
						asnj.put("checkFlag", true);
					}else{
						asnj.put("checkFlag", false);
					}
					asnj.put("exeConn", asn.getExeCondition());
					assigners.add(asnj);
				}
			}
			record.put("assigners", assigners);
			pos = new JSONObject();
			pos.put("top", task.getPosTop());
			pos.put("left", task.getPosLeft());
			record.put("position", pos);
			String status = "";
			if (!StringUtils.isEmpty(task.getCurrTaskId())) {
				status = "PEND";
			} else if (!StringUtils.isEmpty(task.getProcessedFlag())) {
				status = "PROC";
			}
			record.put("status", status);
			record.put("txCode", task.getTxCode());
			record.put("txType", task.getTxType());
			record.put("buzStatus", task.getBuzStatus());
			record.put("timeLimit", task.getTimeLimit());
			record.put("timeLimitTp", task.getTimeLimitTp());
			record.put("alarmTime", task.getAlarmTime());
			record.put("alarmTimeTp", task.getAlarmTimeTp());
			record.put("moduleId", task.getModuleId());
			record.put("runParam", task.getRunParam());
			//json data below
			record.put("TX_CHOICES", JSONObject.parse(task.getTxChoices()));
			record.put("TX_PR_CHOICES", JSONObject.parse(task.getTxPrChoices()));
			record.put("TX_BK_CHOICES", JSONObject.parse(task.getTxBkChoices()));
			record.put("SIGN_CHOICES", JSONObject.parse(task.getSignChoices()));
			array.add(record);
		}
		return array;
	}

	public static JSONArray genConnections(List<WfTaskConn> conns, List<WfTask> tasks) {
		JSONArray array = new JSONArray();
		if (conns == null) {
			return array;
		}
		Map<String, WfTask> taskIdMap = new HashMap<String, WfTask>(tasks.size());
		for (WfTask task : tasks) {
			taskIdMap.put(task.getTaskId(), task);
		}
		JSONObject record = null;
		for (WfTaskConn conn : conns) {
			record = new JSONObject();
			record.put("con_id", conn.getConnId());
			record.put("con_descp", conn.getConnDescp());
			record.put("con_value", conn.getConnVal());
			record.put("source_id", taskIdMap.get(conn.getSourceTaskId()).getTaskPgId());
			record.put("target_id", taskIdMap.get(conn.getTargetTaskId()).getTaskPgId());
			array.add(record);
		}
		return array;
	}

	/***************************
	 * generate object from json
	 *******************************/
	// public static WFDetailVO generateObjectFromJson(JSONObject jsonobj,
	// String wfId){
	// JSONArray tasks = jsonobj.getJSONArray("tasks");
	// JSONArray conns = jsonobj.getJSONArray("conns");
	// WFDetailVO wfDtl = new WFDetailVO();
	// wfDtl.setConns(generateTaskConnList(conns, wfId));
	// wfDtl.setTasks(generateTaskList(tasks, wfId));
	// return wfDtl;
	// }

	public static List<WfTask> generateTaskList(JSONArray tasks, String wfId) {
		if (tasks == null || tasks.isEmpty()) {
			return null;
		}
		int size = tasks.size();
		List<WfTask> taskList = new ArrayList<WfTask>(size);
		WfTask task = null;
		for (int i = 0; i < size; ++i) {
			JSONObject taskj = (JSONObject) tasks.get(i);
			task = new WfTask();
			task.setTaskId(UUID.randomUUID().toString().replace("-", ""));
			task.setWfId(wfId);
			task.setTaskPgId(taskj.getString("id"));
			task.setTaskType(WFConstants.parse2Code(taskj.getString("rsType")));
			task.setTaskDescp(taskj.getString("taskDescp"));
			JSONObject pos = taskj.getJSONObject("position");
			task.setPosTop(Double.valueOf(pos.getString("top")));
			task.setPosLeft(Double.valueOf(pos.getString("left")));
			if (taskj.containsKey("assigners")) {
				String assigners = taskj.getString("assigners");
				JSONArray assignersArray = JSONObject.parseArray(assigners);
				int assignerSize = assignersArray.size();
				List<WfTaskAssign> assignerList = new ArrayList<WfTaskAssign>(assignerSize);
				WfTaskAssign asn = null;
				for(int j=0;j<assignerSize;++j){
					JSONObject assigner = assignersArray.getJSONObject(j);
					asn = new WfTaskAssign();
					asn.setAssignType(assigner.getString("assignTypeCode"));
					asn.setAssignRelId(assigner.getString("id"));
					asn.setDefSelFlag(assigner.getString("defSelMod"));
					Boolean checkFlag = assigner.getBoolean("checkFlag") ==null ? false:assigner.getBoolean("checkFlag") ;
					asn.setSelAllFlag(checkFlag?WFConstants.TaskSelectAllFlag.YES:WFConstants.TaskSelectAllFlag.NO);
					asn.setExeCondition(assigner.getString("exeConn"));
					asn.setTaskId(task.getTaskId());
					assignerList.add(asn);
				}
				task.setAssignerList(assignerList);
			}
			task.setTxCode(taskj.getString("txCode"));
			task.setTxType(taskj.getString("txType"));
			task.setBuzStatus(taskj.getString("buzStatus"));
			task.setTimeLimit(taskj.getInteger("timeLimit"));
			task.setTimeLimitTp(taskj.getString("timeLimitTp"));
			task.setAlarmTime(taskj.getInteger("alarmTime"));
			task.setAlarmTimeTp(taskj.getString("alarmTimeTp"));
			task.setModuleId(taskj.getString("moduleId"));
			task.setRunParam(taskj.getString("runParam"));
			task.setTaskDescpDisp(taskj.getString("taskDescpDisp"));
			task.setTxChoices(taskj.getString("TX_CHOICES"));
			task.setTxPrChoices(taskj.getString("TX_PR_CHOICES"));
			task.setTxBkChoices(taskj.getString("TX_BK_CHOICES"));
			task.setSignChoices(taskj.getString("SIGN_CHOICES"));
			
			taskList.add(task);
		}
		return taskList;
	}

	/**
	 * This method must be executed after taskList inserted.
	 * 
	 * @param conns
	 * @param wfId
	 * @return
	 */
	public static List<WfTaskConn> generateTaskConnList(JSONArray conns, String wfId, List<WfTask> taskList) {
		Map<String, String> taskIdMap = new HashMap<String, String>();
		for (WfTask task : taskList) {
			taskIdMap.put(task.getTaskPgId(), task.getTaskId());
		}
		if (conns == null || conns.isEmpty()) {
			return null;
		}
		int size = conns.size();
		List<WfTaskConn> connList = new ArrayList<WfTaskConn>(size);
		WfTaskConn conn = null;
		for (int i = 0; i < size; ++i) {
			JSONObject connj = (JSONObject) conns.get(i);
			conn = new WfTaskConn();
			conn.setWfId(wfId);
			conn.setTargetTaskId(taskIdMap.get(connj.getString("target_id")));
			conn.setSourceTaskId(taskIdMap.get(connj.getString("source_id")));
			conn.setConnVal(connj.getString("con_value"));
			conn.setConnDescp(connj.getString("con_descp"));
			connList.add(conn);
		}
		return connList;
	}

}
