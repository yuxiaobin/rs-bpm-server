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
public class RecallNormalSimpleTest extends TestBase4NormalSimple{
	
	private static final String refMkid = "ju-recall-normal-simple";
	@Override
	public String getRefMkid() {
		return refMkid;
	}
	
	@Test
	public void normalTxCommit_Recall(){
		startWf();
		forwardTask(TEST_STAFF2,TEST_STAFF3);
		
		commitTask(TEST_STAFF3, "");
		recallSuccess(TEST_STAFF3);
	}
	
}
