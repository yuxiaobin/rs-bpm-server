package com.xb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xb.base.BaseController;
import com.xb.conf.AppConfig;
import com.xb.conf.DBConfig;

@RestController
@EnableAutoConfiguration
@Import(value = {AppConfig.class, DBConfig.class  })
public class MyWorkflowApp extends BaseController implements EmbeddedServletContainerCustomizer {

	/*@Autowired
	private ITblProductService productService;
	@Autowired
	private ITblUserService userService;*/
	
	@RequestMapping("/")
	String home() {
		return "hello world";
	}
	
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = SpringApplication.run(MyWorkflowApp.class, args);
//		String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
//		if(activeProfiles!=null){
//			for(String str:activeProfiles){
//				System.out.println("Using profile is =================="+str);
//			}
//		}else{
//			System.out.println("Using profile is =================="+"null");
//		}
	}

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		// TODO Auto-generated method stub
		container.setPort(8080);
	}
	
}
