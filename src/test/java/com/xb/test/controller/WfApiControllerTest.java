package com.xb.test.controller;

import static com.jayway.restassured.RestAssured.given;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.GreaterThan;
import org.mockito.internal.matchers.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.matcher.ResponseAwareMatcher;
import com.jayway.restassured.response.Response;
import com.xb.MyWorkflowApp;
import com.xb.service.ITblUserService;

@RunWith(SpringJUnit4ClassRunner.class)   // 1
@SpringApplicationConfiguration(classes = MyWorkflowApp.class)   // 2
@WebAppConfiguration   // 3
@IntegrationTest("server.port:0")   // 4: random port
//@Transactional
public class WfApiControllerTest {
	
	@Autowired
	ITblUserService userService;
	@Value("${local.server.port}")   // 6
    int port;
	
	private static final String refMkid = "junitTest";
	
	String rsWfId;
	Integer instNum;
	String currTaskId;
	String nextTaskId4Commit;

	@Before
	public void setup(){
		RestAssured.port = port;
		final String bodyString = "{\"gnmkId\": \"junitTest\"}";
        given().
        contentType("application/json")
        .request().body(bodyString)
//     .expect()
//     .statusCode(200).
        .when()
        .post("/wfadmin/module");
       /* .then().body("rsWfId", new ResponseAwareMatcher<Response>() {
			@Override
			public Matcher<?> matcher(Response response) throws Exception {
				JSONObject json = JSONObject.parseObject(response.getBody().prettyPrint());
				rsWfId = json.getString("rsWfId");
				System.out.println("rsWfId is : "+ rsWfId);
				return NotNull.NOT_NULL;
			}
		});*/
	}
	
	@Test
	public void testStartWF(){
		JSONObject parm = new JSONObject();
		parm.put("userId", "staff1");
		parm.put("gnmkId", refMkid);
		given().contentType("application/json")
        .request().body(parm.toJSONString())
        .when().post("/wfapi/start")
        .then()
        .body("return_code", new ResponseAwareMatcher<Response>() {
			@Override
			public Matcher<?> matcher(Response response) throws Exception {
				return new Equals(0);
			}
		})
        .body("wf_inst_num", new ResponseAwareMatcher<Response>() {
			@Override
			public Matcher<?> matcher(Response response) throws Exception {
				JSONObject json = JSONObject.parseObject(response.getBody().prettyPrint());
				instNum = json.getInteger("wf_inst_num");
				currTaskId = json.getString("curr_task_id");
				System.out.println("testStartWF():\tinstNum is "+instNum);
				return new GreaterThan<Integer>(0);
			}
		})
        ;
		testGetNextTask4Commit();
		testCommit();
		testGetNextTask4Commit2();
		testCommit2Tx2();
		testRecall();
//		userService.deleteJunitData(refMkid);
	}
	
	public void testGetNextTask4Commit(){
		JSONObject parm = new JSONObject();
		parm.put("wfInstNum", instNum);
		parm.put("gnmkId", refMkid);
		parm.put("optCode", "C");
		given().contentType("application/json")
        .request().body(parm.toJSONString())
        .when().post("/wfapi/tasks")
        .then()
        .body("records", new ResponseAwareMatcher<Response>() {
			@Override
			public Matcher<?> matcher(Response response) throws Exception {
				JSONObject json = JSONObject.parseObject(response.getBody().prettyPrint());
				JSONArray records = json.getJSONArray("records");
				nextTaskId4Commit = records.getJSONObject(0).getString("taskId");
				System.out.println("testGetNextTask4Commit():\t nextTaskId4Commit is "+nextTaskId4Commit);
				return NotNull.NOT_NULL;
			}
		})
        ;
	}
	
	public void testRecall(){
		JSONObject parm = new JSONObject();
		parm.put("userId", "staff1");
		parm.put("gnmkId", refMkid);
		parm.put("wfInstNum", instNum);
		parm.put("optCode", "RC");
		parm.put("currTaskId", currTaskId);
		parm.put("comments", "junitTest: staff1 recall");
		System.out.println("testRecall.data="+parm.toJSONString());
		/*given().contentType("application/json")
        .request().body(parm.toJSONString())
        .when().post("/wfapi/operate")
        .then()
        .body("return_code", new ResponseAwareMatcher<Response>() {
			@Override
			public Matcher<?> matcher(Response response) throws Exception {
				return new Equals(0);
			}
		})
        ;*/
	}
	
	public void testCommit(){
		JSONObject parm = new JSONObject();
		parm.put("userId", "staff1");
		parm.put("gnmkId", refMkid);
		parm.put("comments", "junitTest: staff1 commit");
		parm.put("nextTaskId", nextTaskId4Commit);
		parm.put("nextUserIds", "staff1");
		parm.put("optCode", "C");
		parm.put("wfInstNum", instNum);
		parm.put("currTaskId", currTaskId);
		given().contentType("application/json")
        .request().body(parm.toJSONString())
        .when().post("/wfapi/operate")
        .then()
        .body("return_code", new ResponseAwareMatcher<Response>() {
			@Override
			public Matcher<?> matcher(Response response) throws Exception {
				currTaskId = nextTaskId4Commit;
				return new Equals(0);
			}
		})
        ;
	}
	
	public void testGetNextTask4Commit2(){
		JSONObject parm = new JSONObject();
		parm.put("wfInstNum", instNum);
		parm.put("gnmkId", refMkid);
		parm.put("optCode", "C");
		given().contentType("application/json")
        .request().body(parm.toJSONString())
        .when().post("/wfapi/tasks")
        .then()
        .body("return_code", new Equals(0))
        .body("records", new ResponseAwareMatcher<Response>() {
			@Override
			public Matcher<?> matcher(Response response) throws Exception {
				JSONObject json = JSONObject.parseObject(response.getBody().prettyPrint());
				JSONArray records = json.getJSONArray("records");
				nextTaskId4Commit = records.getJSONObject(0).getString("taskId");
				System.out.println("testGetNextTask4Commit():\t nextTaskId4Commit is "+nextTaskId4Commit);
				return NotNull.NOT_NULL;
			}
		})
        ;
	}
	public void testCommit2Tx2(){
		JSONObject parm = new JSONObject();
		parm.put("userId", "staff1");
		parm.put("gnmkId", refMkid);
		parm.put("comments", "junitTest: staff1 commit");
		parm.put("nextTaskId", nextTaskId4Commit);
		parm.put("nextUserIds", "manager1");
		parm.put("optCode", "C");
		parm.put("wfInstNum", instNum);
		parm.put("currTaskId", currTaskId);
		given().contentType("application/json")
        .request().body(parm.toJSONString())
        .when().post("/wfapi/operate")
        .then()
        .body("return_code", new ResponseAwareMatcher<Response>() {
			@Override
			public Matcher<?> matcher(Response response) throws Exception {
				currTaskId = nextTaskId4Commit;
				return new Equals(0);
			}
		})
        ;
	}
	
	@After
	public void destory(){
		
	}
}
