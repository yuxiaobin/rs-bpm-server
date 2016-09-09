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
		commitTask("staff1", "manager1");
		commitTask("staff2", "manager1");
		forwardTask("manager1", "manager2");
		checkAwt("manager1", 0);
		checkAwt("manager2", 1);
		checkAwt("staff1", 0);
		checkAwt("staff2", 0);
		recallSuccess("staff1");
		checkAwt("manager1", 0);
		checkAwt("manager2", 0);
		checkAwt("staff1", 1);
		checkAwt("staff2", 0);
		recallSuccess("staff2");
		checkAwt("staff2", 1);
		commitTask("staff1", "manager1");
		checkAwt("manager1", 0);
		commitTask("staff2", "manager1");
		checkAwt("manager1", 1);
		checkAwt("staff1", 0);
		checkAwt("staff2", 0);
	}
	
}
