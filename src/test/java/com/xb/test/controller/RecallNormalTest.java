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
public class RecallNormalTest extends TestBase4Normal{
	
	private static final String refMkid = "ju-recall-normal-tx-commit";
	@Override
	public String getRefMkid() {
		return refMkid;
	}
	
	@Test
	public void normalTxCommit_Recall(){
		startWf();
		commitTask(TEST_STAFF1, TEST_MANAGER1);
		recallSuccess(TEST_STAFF1);
		checkAwt(TEST_STAFF1,1);
		checkAwt(TEST_MANAGER1,0);
	}
	
	@Test
	public void normalTxCommit2Self_Recall(){
		startWf();
		commitTask(TEST_STAFF1, TEST_STAFF1);
		recallSuccess(TEST_STAFF1);
		checkAwt(TEST_STAFF1,1);
	}
	
}
