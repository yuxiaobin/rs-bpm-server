package com.xb.test.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xb.MyWorkflowApp;

@RunWith(SpringJUnit4ClassRunner.class)   // 1
@SpringApplicationConfiguration(classes = MyWorkflowApp.class)   // 2
@WebIntegrationTest("server.port:0")   // 4: random port
public class RecallCSC2SmUsFwRCC2Test extends TestBase4CS{
	
	/**
	 * staff1,staff2 commit CS to manager1,staff1
	 * 
	 * staff1 forward 2 manager2
	 * 
	 * staff1 recall
	 * 
	 * staff2 recall
	 * 
	 */
	private static final String refMkid = "ju-recall-cs-c2-SameUser-F-RC_c2";
	
	@Override
	public String getRefMkid() {
		return refMkid;
	}
	
	@Test
	public void testCSRecall(){
		startWf();
		//CS commit
		commitTask(TEST_STAFF1, TEST_MANAGER1+","+TEST_STAFF1);
		commitTask(TEST_STAFF2, TEST_MANAGER1+","+TEST_STAFF1);
		checkAwt(TEST_MANAGER1, 1);
		checkAwt(TEST_STAFF1, 1);
		forwardTask(TEST_STAFF1, TEST_MANAGER2);
		checkAwt(TEST_MANAGER1, 1);
		checkAwt(TEST_MANAGER2, 1);
		checkAwt(TEST_STAFF1, 0);
		
		recallSuccess(TEST_STAFF1);
		checkAwt(TEST_MANAGER1, 1);
		checkAwt(TEST_MANAGER2, 0);
		checkAwt(TEST_STAFF1, 1);
		
		recallFail(TEST_STAFF1);
		
		recallSuccess(TEST_STAFF2);
		checkAwt(TEST_STAFF2, 1);
		checkAwt(TEST_STAFF1, 0);
		checkAwt(TEST_MANAGER1, 0);
		
		commitTask(TEST_STAFF2,TEST_MANAGER1);
		checkAwt(TEST_MANAGER1,1);
		checkAwt(TEST_STAFF2,0);
		
	}
	
}
