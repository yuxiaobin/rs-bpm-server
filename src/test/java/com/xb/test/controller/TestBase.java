package com.xb.test.controller;

import static com.jayway.restassured.RestAssured.given;

import org.apache.logging.log4j.LogManager;
import org.hamcrest.Matcher;
import org.junit.After;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.GreaterThan;
import org.mockito.internal.matchers.NotNull;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.matcher.ResponseAwareMatcher;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;
import com.xb.service.ITblUserService;
import com.xb.service.IWfInstanceService;

public abstract class TestBase {
	org.apache.logging.log4j.Logger log = LogManager.getLogger(TestBase.class);
	
	@Autowired
	ITblUserService userService;
	@Autowired
	IWfInstanceService instService;
	
	@Value("${local.server.port}")   // 6
    int port;
	
	String rsWfId;
	Integer instNum;
	String currTaskId;
	String nextTaskId4Commit;
	
	public abstract String getRefMkid();
	
	final String TEST_STAFF1 = "jtest_staff1";
	final String TEST_STAFF2 = "jtest_staff2";
	final String TEST_STAFF3 = "jtest_staff3";
	final String TEST_MANAGER1 = "jtest_manager1";
	final String TEST_MANAGER2 = "jtest_manager2";
	final String TEST_MANAGER3 = "jtest_manager3";
	
	public void deleteTest(){
		userService.deleteJunitData(getRefMkid());
	}
	
	/**
	 * Common method
	 */
	public void startWf(){
		startWf(TEST_STAFF1, TEST_STAFF1+","+TEST_STAFF2+","+TEST_STAFF3);//From start task committed to first task.
	}
	
	public void startWf(String starterId, String nextTaskAssigners){
		JSONObject parm = new JSONObject();
		parm.put("userId", starterId);
		parm.put("gnmkId", getRefMkid());
		ValidatableResponse response = given().contentType("application/json")
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
				return new GreaterThan<Integer>(0);
			}
		});
		JSONObject json = JSONObject.parseObject(response.extract().asString());
		instNum = json.getInteger("wf_inst_num");
		currTaskId = json.getString("curr_task_id");
		getNextTask4Commit();
		commitTask(starterId, nextTaskAssigners);//From start task committed to first task.
	}
	
	public void letMeDo(String userId){
		JSONObject parm = new JSONObject();
		parm.put("userId", userId);
		parm.put("gnmkId", getRefMkid());
		parm.put("optCode", "LMD");
		parm.put("wfInstNum", instNum);
		given().contentType("application/json")
		.request().body(parm.toJSONString())
		.when().post("/wfapi/operate")
		.then()
		.body("return_code",new Equals(0));
	}
	
	/**
	 * Common method
	 */
	private void getNextTask4Commit(){
		getNextTask("C");
	}
	private void getNextTask4Reject(){
		getNextTask("RJ");
	}
	
	private void getNextTask(String optCode){
		JSONObject parm = new JSONObject();
		parm.put("wfInstNum", instNum);
		parm.put("gnmkId", getRefMkid());
		parm.put("optCode", optCode);
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
		}) ;
	}
	
	/**
	 * Common method
	 * @param committer
	 * @param nextAssignerIds
	 */
	public void commitTask(String committer, String nextAssignerIds){
		commitTask(committer, nextAssignerIds, true);
	}
	
	public void commitTask(String committer, String nextAssignerIds, boolean needGetNextTaskFlag){
		if(needGetNextTaskFlag){
			getNextTask4Commit();
		}
		JSONObject parm = new JSONObject();
		parm.put("userId", committer);
		parm.put("gnmkId", getRefMkid());
		parm.put("comments", "junitTest: "+committer+" commit");
		parm.put("nextTaskId", nextTaskId4Commit);
		parm.put("nextUserIds", nextAssignerIds);
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
		});
	}
	
	public void rejectTask(String rejector, String nextAssignerIds){
		getNextTask4Reject();
		
		JSONObject parm = new JSONObject();
		parm.put("userId", rejector);
		parm.put("gnmkId", getRefMkid());
		parm.put("comments", "junitTest: "+rejector+" reject task");
		parm.put("nextTaskId", nextTaskId4Commit);
		parm.put("nextUserIds", nextAssignerIds);
		parm.put("optCode", "RJ");
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
		});
	}
	
	public void forwardTask(String fromUser, String toUser){
		JSONObject parm = new JSONObject();
		parm.put("userId", fromUser);
		parm.put("gnmkId", getRefMkid());
		parm.put("comments", "junitTest: from:"+fromUser+" forward to "+toUser);
		parm.put("nextTaskId", currTaskId);
		parm.put("nextUserIds", toUser);
		parm.put("optCode", "F");
		parm.put("wfInstNum", instNum);
		parm.put("currTaskId", currTaskId);
		given().contentType("application/json")
        .request().body(parm.toJSONString())
        .when().post("/wfapi/operate")
        .then()
        .body("return_code", new ResponseAwareMatcher<Response>() {
			@Override
			public Matcher<?> matcher(Response response) throws Exception {
				return new Equals(0);
			}
		}) ;
	}
	
	public void recallSuccess(String recaller){
		JSONObject parm = new JSONObject();
		parm.put("userId", recaller);
		parm.put("gnmkId", getRefMkid());
		parm.put("wfInstNum", instNum);
		parm.put("optCode", "RC");
		parm.put("comments", "junitTest: "+recaller+" recall");
		given().contentType("application/json")
        .request().body(parm.toJSONString())
        .when().post("/wfapi/operate")
        .then()
        .body("return_code", new ResponseAwareMatcher<Response>() {
			@Override
			public Matcher<?> matcher(Response response) throws Exception {
				log.info(response.getBody().prettyPrint());
				return new Equals(0);
			}
		});
	}
	
	public void recallFail(String recaller){
		JSONObject parm = new JSONObject();
		parm.put("userId", recaller);
		parm.put("gnmkId", getRefMkid());
		parm.put("wfInstNum", instNum);
		parm.put("optCode", "RC");
		parm.put("comments", "junitTest: "+recaller+" recall");
		given().contentType("application/json")
        .request().body(parm.toJSONString())
        .when().post("/wfapi/operate")
        .then()
        .body("return_code", new Equals(3));
	}
	
	public void checkAwt(String currUserId, final int awtCount){
		JSONObject parm = new JSONObject();
		parm.put("userId", currUserId);
		given().contentType("application/json")
        .request().body(parm.toJSONString())
        .when().post("/wfapi/awt")
        .then()
        .body("return_code", new ResponseAwareMatcher<Response>() {
			@Override
			public Matcher<?> matcher(Response response) throws Exception {
				if(awtCount==0){
	        		return new Equals(2);
	        	}else{
	        		return new Equals(0);//return code: { 0:success, 1:invalidParm, 2:no record, 9:syserror}
	        	}
			}
        })
        .body("count", new ResponseAwareMatcher<Response>() {
			@Override
			public Matcher<?> matcher(Response response) throws Exception {
				JSONObject json = JSONObject.parseObject(response.getBody().prettyPrint());
				JSONArray records = json.getJSONArray("records");
				Integer countReturn = json.getInteger("count");
				if(countReturn==null ){
					if(awtCount!=0)
						return NotNull.NOT_NULL;
					else
						return Null.NULL;
				}
				int count = 0;
				for(int i=0;i<records.size();++i){
					if(records.getJSONObject(i).getString("gnmkId").equals(getRefMkid())){
						count++;
					}
				}
				if(count!=awtCount){
					return Null.NULL;
				}
				return NotNull.NOT_NULL;
			}
		});
	}
	
	@After
	public void destory(){
		deleteTest();
	}

}
