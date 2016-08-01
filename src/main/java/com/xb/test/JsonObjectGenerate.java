package com.xb.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonObjectGenerate {
	
	public static void main(String[] args) {
		JSONArray tasks = genTaskJson();
		JSONArray conns = genConnections();
		JSONObject result = new JSONObject();
		result.put("tasks", tasks);
		result.put("conns", conns);
		System.out.println(result);
	}
	
	
	
	public static JSONArray genTaskJson(){
		JSONArray array = new JSONArray();
		JSONObject record = null;
		JSONObject pos = null;
		
		record = new JSONObject();
		record.put("id", "start-task");
		record.put("rsType", "start-task");
		record.put("descp", "Start");
		pos = new JSONObject();
		pos.put("top", 70);
		pos.put("left", 350);
		record.put("position", pos);
		array.add(record);
		
		record = new JSONObject();
		record.put("id", "end-task");
		record.put("rsType", "end-task");
		record.put("descp", "End");
		pos = new JSONObject();
		pos.put("top", 370);
		pos.put("left", 350);
		record.put("position", pos);
		array.add(record);
		
		record = new JSONObject();
		record.put("id", "userTask1");
		record.put("rsType", "user-task");
		record.put("descp", "userTask1");
		pos = new JSONObject();
		pos.put("top", 200);
		pos.put("left", 550);
		record.put("position", pos);
		array.add(record);
		
		record = new JSONObject();
		record.put("id", "userTask2");
		record.put("rsType", "user-task");
		record.put("descp", "userTask2");
		pos = new JSONObject();
		pos.put("top", 370);
		pos.put("left", 550);
		record.put("position", pos);
		array.add(record);
		
		record = new JSONObject();
		record.put("id", "condition");
		record.put("rsType", "rs-cond-task");
		record.put("descp", "condition adjust");
		pos = new JSONObject();
		pos.put("top", 200);
		pos.put("left", 350);
		record.put("position", pos);
		array.add(record);
		
//		System.out.println(array.toJSONString());
		return array;
		
	}

	public static JSONArray genConnections(){
		JSONArray array = new JSONArray();
		JSONObject record = null;
		
		record = new JSONObject();
		record.put("con_id", "con_20");
		record.put("con_descp", "Start to process");
		record.put("con_value", "");
		record.put("source_id", "start-task");
		record.put("target_id", "condition");
		array.add(record);
		
		record = new JSONObject();
		record.put("con_id", "con_40");
		record.put("con_descp", "Yes");
		record.put("con_value", "Yes");
		record.put("source_id", "condition");
		record.put("target_id", "end-task");
		array.add(record);

		record = new JSONObject();
		record.put("con_id", "con_12");
		record.put("con_descp", "No");
		record.put("con_value", "No");
		record.put("source_id", "condition");
		record.put("target_id", "userTask1");
		array.add(record);
		
		record = new JSONObject();
		record.put("con_id", "con_13");
		record.put("con_descp", "Next step");
		record.put("con_value", "");
		record.put("source_id", "userTask1");
		record.put("target_id", "userTask2");
		array.add(record);
		
		record = new JSONObject();
		record.put("con_id", "con_14");
		record.put("con_descp", "Over");
		record.put("con_value", "");
		record.put("source_id", "userTask2");
		record.put("target_id", "end-task");
		array.add(record);
		
		return array;
	}
}
