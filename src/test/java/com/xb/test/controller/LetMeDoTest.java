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
public class LetMeDoTest extends TestBase4Normal{
	
	private static final String refMkid = "ju-normal-let-me-do";
	
	@Override
	public String getRefMkid() {
		return refMkid;
	}
	
	@Test
	public void testLetMeDo(){
		startWf();
		letMeDo(TEST_STAFF2);
		checkAwt(TEST_STAFF2,1);
		checkAwt(TEST_STAFF1,0);
		checkAwt(TEST_STAFF3,0);
	}
	
}
