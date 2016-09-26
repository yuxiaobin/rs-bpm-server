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
public class RecallCSRCNextForwardTest extends TestBase4CS{
	
	private static final String refMkid = "ju-recall-cs-simple-C2-F1-RC_F";
	
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
		recallSuccess(TEST_MANAGER1);
//		checkAwt(TEST_MANAGER1, 1);
	}
	
}
