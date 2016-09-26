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
public class RecallCSForwardRCCommit2Test extends TestBase4CS{
	
	private static final String refMkid = "ju-recall-cs-simple-C2-F1-RC2C";
	
	@Override
	public String getRefMkid() {
		return refMkid;
	}
	
	@Test
	public void testCSRecall(){
		startWf();
		//CS commit
		commitTask(TEST_STAFF1, TEST_MANAGER1);
		commitTask(TEST_STAFF2, TEST_MANAGER1);
		forwardTask(TEST_MANAGER1, TEST_MANAGER2);
//		checkAwt(TEST_MANAGER1, 0);
//		checkAwt(TEST_MANAGER2, 1);
//		checkAwt(TEST_STAFF1, 0);
//		checkAwt(TEST_STAFF2, 0);
		recallSuccess(TEST_STAFF1);
//		checkAwt(TEST_MANAGER1, 0);
//		checkAwt(TEST_MANAGER2, 0);
//		checkAwt(TEST_STAFF1, 1);
//		checkAwt(TEST_STAFF2, 0);
		recallSuccess(TEST_STAFF2);
//		checkAwt(TEST_STAFF2, 1);
		commitTask(TEST_STAFF1, TEST_MANAGER1);
//		checkAwt(TEST_MANAGER1, 0);
		commitTask(TEST_STAFF2, TEST_MANAGER1);
//		checkAwt(TEST_MANAGER1, 1);
//		checkAwt(TEST_STAFF1, 0);
//		checkAwt(TEST_STAFF2, 0);
	}
	
}
