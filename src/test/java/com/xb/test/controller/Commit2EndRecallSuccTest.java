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
public class Commit2EndRecallSuccTest extends TestBase4Normal{
	
	private static final String refMkid = "ju-normal-commit-end";
	
	@Override
	public String getRefMkid() {
		return refMkid;
	}
	
	@Test
	public void testCommitTask2End(){
		startWf();
		commitTask(TEST_STAFF1, TEST_MANAGER1);
		commitTask(TEST_MANAGER1, "");
		recallSuccess(TEST_MANAGER1);//ended workflow can be recall if end task setting allow recall
	}
	
}
