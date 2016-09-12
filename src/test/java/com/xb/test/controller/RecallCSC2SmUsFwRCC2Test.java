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
		commitTask("staff1", "manager1,staff1");
		commitTask("staff2", "manager1,staff1");
		checkAwt("manager1", 1);
		checkAwt("staff1", 1);
		forwardTask("staff1", "manager2");
		checkAwt("manager1", 1);
		checkAwt("manager2", 1);
		checkAwt("staff1", 0);
		
		recallSuccess("staff1");
		checkAwt("manager1", 1);
		checkAwt("manager2", 0);
		checkAwt("staff1", 1);
		
		recallFail("staff1");
		
		recallSuccess("staff2");
		checkAwt("staff2", 1);
		checkAwt("staff1", 0);
		checkAwt("manager1", 0);
		
		commitTask("staff2","manager1");
		checkAwt("manager1",1);
		checkAwt("staff2",0);
		
	}
	
}
