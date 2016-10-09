package com.xb.test.controller;

import static com.jayway.restassured.RestAssured.given;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.NotNull;

import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.matcher.ResponseAwareMatcher;
import com.jayway.restassured.response.Response;

public abstract class TestBase4Normal extends TestBase{

	@Before
	public void initMethod4Normal(){
		deleteTest();
		RestAssured.port = port;
		//create workflow
		JSONObject parm = new JSONObject();
		parm.put("refMkid", getRefMkid());
		given().contentType("application/json")
        .request().body(parm.toJSONString())
        .when().post("/wfadmin/module")
        .then()
        .body("refMkid", new ResponseAwareMatcher<Response>() {
			@Override
			public Matcher<?> matcher(Response response) throws Exception {
				JSONObject json = JSONObject.parseObject(response.getBody().prettyPrint());
				refMkid = json.getString("refMkid");
				return NotNull.NOT_NULL;
			}
		});
		//End Task allow recall
		JSONObject createFLow = JSONObject.parseObject("{\"tasks\":[{\"id\":\"start-task\",\"rsType\":\"start-task\",\"descpDisp\":\"Start Task\",\"assigners\":[],\"position\":{\"top\":70,\"left\":350},\"txCode\":\"0000\",\"txType\":\"B\",\"buzStatus\":\"I\",\"timeLimit\":null,\"timeLimitTp\":\"H\",\"alarmTime\":null,\"alarmTimeTp\":\"H\",\"taskDescpDisp\":\"Start Task Disp\",\"TX_CHOICES\":{\"AllowEdit\":true,\"AllowDelete\":true},\"TX_PR_CHOICES\":{},\"TX_BK_CHOICES\":{\"GoBackToPrevious\":true},\"SIGN_CHOICES\":{\"AllHandledThenGo\":true}},{\"id\":\"jsPlumb_2_1\",\"rsType\":\"end-task\",\"descpDisp\":\"End Task\",\"assigners\":[],\"position\":{\"top\":396,\"left\":438},\"txCode\":\"9999\",\"txType\":\"E\",\"buzStatus\":\"C\",\"timeLimit\":null,\"timeLimitTp\":\"H\",\"alarmTime\":null,\"alarmTimeTp\":\"H\",\"taskDescp\":\"\",\"taskDescpDisp\":\"End Task Disp\",\"TX_CHOICES\":{\"AllowEdit\":false,\"AllowDelete\":false,\"AllowReCall\":true,\"SignWhenReCall\":true,\"SignWhenGoBack\":false,\"SignWhenVeto\":false},\"TX_PR_CHOICES\":{},\"TX_BK_CHOICES\":{\"GoBackToPrevious\":true},\"SIGN_CHOICES\":{\"AllHandledThenGo\":true}},{\"id\":\"1473405967485\",\"rsType\":\"user-task\",\"descpDisp\":\"tx1\",\"assigners\":[],\"position\":{\"top\":179,\"left\":223},\"txCode\":\"001\",\"txType\":\"S\",\"buzStatus\":\"I\",\"timeLimit\":24,\"timeLimitTp\":\"H\",\"alarmTime\":null,\"alarmTimeTp\":\"H\",\"taskDescp\":\"\",\"taskDescpDisp\":\"tx1\",\"TX_CHOICES\":{\"AllowGoBack\":true,\"SignWhenGoBack\":true,\"AllowVeto\":true,\"SignWhenVeto\":true,\"SignWhenReCall\":true,\"AllowReCall\":true},\"TX_PR_CHOICES\":{\"NoticeElseAfterGo\":false},\"TX_BK_CHOICES\":{\"GoBackToPrevious\":true},\"SIGN_CHOICES\":{\"AllHandledThenGo\":false,\"AtLeastHandled\":1,\"PartHandledThenGo\":false}},{\"id\":\"1473405982196\",\"rsType\":\"user-task\",\"descpDisp\":\"tx2\",\"assigners\":[],\"position\":{\"top\":298,\"left\":333},\"txCode\":\"002\",\"txType\":\"S\",\"buzStatus\":\"I\",\"timeLimit\":24,\"timeLimitTp\":\"H\",\"alarmTime\":null,\"alarmTimeTp\":\"H\",\"taskDescp\":\"\",\"taskDescpDisp\":\"tx2\",\"TX_CHOICES\":{\"AllowGoBack\":true,\"SignWhenGoBack\":true,\"AllowVeto\":true,\"SignWhenVeto\":true,\"SignWhenReCall\":true,\"AllowReCall\":true},\"TX_PR_CHOICES\":{\"NoticeElseAfterGo\":false},\"TX_BK_CHOICES\":{\"GoBackToPrevious\":true},\"SIGN_CHOICES\":{\"AllHandledThenGo\":false,\"AtLeastHandled\":1,\"PartHandledThenGo\":false}}],\"conns\":[{\"con_id\":\"con_7\",\"con_descp\":\"Next\",\"con_value\":\"\",\"source_id\":\"start-task\",\"target_id\":\"1473405967485\"},{\"con_id\":\"con_19\",\"con_descp\":\"Next\",\"con_value\":\"\",\"source_id\":\"1473405967485\",\"target_id\":\"1473405982196\"},{\"con_id\":\"con_31\",\"con_descp\":\"Next\",\"con_value\":\"\",\"source_id\":\"1473405982196\",\"target_id\":\"jsPlumb_2_1\"}]}");
		given().contentType("application/json")
        .request().body(createFLow.toJSONString())
        .when().post("/wfadmin/module/"+refMkid+"/wf")
        .then()
        .body("return_code", new Equals(0));
	}
}
