package com.xb.test.controller;

import static com.jayway.restassured.RestAssured.given;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.NotNull;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.response.ValidatableResponse;
import com.xb.MyWorkflowApp;

@RunWith(SpringJUnit4ClassRunner.class)   // 1
@SpringApplicationConfiguration(classes = MyWorkflowApp.class)   // 2
@WebIntegrationTest("server.port:0")   // 4: random port
public class GetAssigers4RecallEndTest extends TestBase4Normal{
	
	private static final String refMkid = "ju-normal-commit-end-recall-get";
	
	@Override
	public String getRefMkid() {
		return refMkid;
	}
	
	@Test
	public void testCommitTask2End(){
		startWf();
		commitTask(TEST_STAFF1, TEST_MANAGER1);
		commitTask(TEST_MANAGER1, "");//committed to End task, workflow close
		getAssigner4Recall(TEST_MANAGER1);
		getNextTask4Recall(TEST_MANAGER1);
	}
	
	private void getAssigner4Recall(String recaller){
		JSONObject parm = new JSONObject();
		parm.put("currUserId", recaller);
		parm.put(PARM_REF_MKID, getRefMkid());
		parm.put("optCode", "RC");
		parm.put("instNum", instNum);
		ValidatableResponse response  = given().contentType("application/json")
        .request().body(parm.toJSONString())
        .when().post("/task/next/tasks")
        .then()
        .body("records",  NotNull.NOT_NULL);
		JSONObject json = JSONObject.parseObject(response.extract().asString());
		JSONArray array = json.getJSONArray("records");
		if(array==null || array.size()!=1){
			Assert.fail();
		}else{
			JSONObject obj = array.getJSONObject(0);
			String taskType = obj.getString("taskType");
			if("end-task".equals(taskType)){
				Assert.fail();
			}
		}
	}
	
	private void getNextTask4Recall(String recaller){
		JSONObject parm = new JSONObject();
		parm.put("currUserId", recaller);
		parm.put(PARM_REF_MKID, getRefMkid());
		parm.put("optCode", "RC");
		parm.put("instNum", instNum);
		ValidatableResponse response  = given().contentType("application/json")
        .request().body(parm.toJSONString())
        .when().post("/task/next/usergroups")
        .then()
        .body("result",  NotNull.NOT_NULL);
		JSONObject json = JSONObject.parseObject(response.extract().asString());
		JSONObject result = json.getJSONObject("result");
		JSONArray users = result.getJSONArray("users");
		if(users==null || users.size()!=1){
			Assert.fail("nextAssigners size is not 1");
		}
		JSONObject user = users.getJSONObject(0);
		Assert.assertEquals(recaller, user.getString("id"));
		System.out.println("getNextTask4Recall("+recaller+"): users="+users.toJSONString());
		
	}
}
