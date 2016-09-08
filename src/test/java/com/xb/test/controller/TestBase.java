package com.xb.test.controller;

import static com.jayway.restassured.RestAssured.given;

import org.hamcrest.Matcher;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.matcher.ResponseAwareMatcher;
import com.jayway.restassured.response.Response;
import com.xb.service.ITblUserService;

public abstract class TestBase {
	
	@Autowired
	ITblUserService userService;
	@Value("${local.server.port}")   // 6
    int port;
	
	String rsWfId;
	Integer instNum;
	String currTaskId;
	String nextTaskId4Commit;
	
	public abstract String getRefMkid();
	
	public void initMethod(){
		userService.deleteJunitData(getRefMkid());
		RestAssured.port = port;
		//create workflow
		JSONObject parm = new JSONObject();
		parm.put("gnmkId", getRefMkid());
		given().contentType("application/json")
        .request().body(parm.toJSONString())
        .when().post("/wfadmin/module")
        .then()
        .body("rsWfId", new ResponseAwareMatcher<Response>() {
			@Override
			public Matcher<?> matcher(Response response) throws Exception {
				JSONObject json = JSONObject.parseObject(response.getBody().prettyPrint());
				rsWfId = json.getString("rsWfId");
				return NotNull.NOT_NULL;
			}
		});
		
		JSONObject createFLow = JSONObject.parseObject("{\"tasks\":[{\"id\":\"start-task\",\"rsType\":\"start-task\",\"descpDisp\":\"开始"+getRefMkid()+"\",\"assigners\":[],\"position\":{\"top\":63,\"left\":366},\"txCode\":\"0000\",\"txType\":\"B\",\"buzStatus\":\"I\",\"timeLimit\":null,\"timeLimitTp\":\"H\",\"alarmTime\":null,\"alarmTimeTp\":\"H\",\"taskDescp\":\"\",\"taskDescpDisp\":\"开始ju-recall-next-committed\",\"TX_CHOICES\":{\"AllowEdit\":true,\"AllowDelete\":true,\"SignWhenGoBack\":false,\"SignWhenReCall\":false,\"SignWhenVeto\":false},\"TX_PR_CHOICES\":{},\"TX_BK_CHOICES\":{\"GoBackToPrevious\":true},\"SIGN_CHOICES\":{\"AllHandledThenGo\":true}},{\"id\":\"jsPlumb_2_1\",\"rsType\":\"end-task\",\"descpDisp\":\"结束\",\"assigners\":[],\"position\":{\"top\":371,\"left\":360},\"txCode\":\"9999\",\"txType\":\"E\",\"buzStatus\":\"C\",\"timeLimit\":null,\"timeLimitTp\":\"H\",\"alarmTime\":null,\"alarmTimeTp\":\"H\",\"taskDescpDisp\":\"结束\",\"TX_CHOICES\":{\"AllowEdit\":false,\"AllowDelete\":false},\"TX_PR_CHOICES\":{},\"TX_BK_CHOICES\":{\"GoBackToPrevious\":true},\"SIGN_CHOICES\":{\"AllHandledThenGo\":true}},{\"id\":\"1473303575745\",\"rsType\":\"user-task\",\"descpDisp\":\"事务1\",\"assigners\":[],\"position\":{\"top\":167,\"left\":219},\"txCode\":\"001\",\"txType\":\"S\",\"buzStatus\":\"I\",\"timeLimit\":24,\"timeLimitTp\":\"H\",\"alarmTime\":null,\"alarmTimeTp\":\"H\",\"taskDescp\":\"\",\"taskDescpDisp\":\"事务1\",\"TX_CHOICES\":{\"AllowGoBack\":true,\"SignWhenGoBack\":true,\"AllowVeto\":true,\"SignWhenVeto\":true,\"SignWhenReCall\":true,\"AllowReCall\":true},\"TX_PR_CHOICES\":{\"NoticeElseAfterGo\":false},\"TX_BK_CHOICES\":{\"GoBackToPrevious\":true},\"SIGN_CHOICES\":{\"AllHandledThenGo\":false,\"AtLeastHandled\":1,\"PartHandledThenGo\":false}},{\"id\":\"1473303577816\",\"rsType\":\"user-task\",\"descpDisp\":\"事务2\",\"assigners\":[],\"position\":{\"top\":239,\"left\":473},\"txCode\":\"002\",\"txType\":\"S\",\"buzStatus\":\"I\",\"timeLimit\":24,\"timeLimitTp\":\"H\",\"alarmTime\":null,\"alarmTimeTp\":\"H\",\"taskDescp\":\"\",\"taskDescpDisp\":\"事务2\",\"TX_CHOICES\":{\"AllowGoBack\":true,\"SignWhenGoBack\":true,\"AllowVeto\":true,\"SignWhenVeto\":true,\"SignWhenReCall\":true,\"AllowReCall\":true},\"TX_PR_CHOICES\":{\"NoticeElseAfterGo\":false},\"TX_BK_CHOICES\":{\"GoBackToPrevious\":true},\"SIGN_CHOICES\":{\"AllHandledThenGo\":false,\"AtLeastHandled\":1,\"PartHandledThenGo\":false}}],\"conns\":[{\"con_id\":\"con_7\",\"con_descp\":\"Next\",\"con_value\":\"\",\"source_id\":\"start-task\",\"target_id\":\"1473303575745\"},{\"con_id\":\"con_19\",\"con_descp\":\"Next\",\"con_value\":\"\",\"source_id\":\"1473303575745\",\"target_id\":\"1473303577816\"},{\"con_id\":\"con_31\",\"con_descp\":\"Next\",\"con_value\":\"\",\"source_id\":\"1473303577816\",\"target_id\":\"jsPlumb_2_1\"}]}");
		given().contentType("application/json")
        .request().body(createFLow.toJSONString())
        .when().post("/wfadmin/module/"+rsWfId+"/wf")
        .then()
        .body("return_code", new Equals(0));
	}

}
