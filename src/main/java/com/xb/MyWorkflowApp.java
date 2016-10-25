package com.xb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xb.base.BaseController;
import com.xb.conf.AppConfig;
import com.xb.conf.DBConfig;
import com.xb.conf.DatasourceConfig;
import com.xb.conf.FilterConfig;
import com.xb.conf.InterceptorConfig;
import com.xb.conf.ServletConfig;

@RestController
@EnableAutoConfiguration
@Import(value = {AppConfig.class, DatasourceConfig.class, DBConfig.class, FilterConfig.class, InterceptorConfig.class,ServletConfig.class })
@ComponentScan(basePackages={"com.rshare.service.wf.annotations"})
public class MyWorkflowApp extends BaseController implements EmbeddedServletContainerCustomizer {

	@RequestMapping("/")
	String home() {
		return "hello world";
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MyWorkflowApp.class, args);
	}

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.setPort(8080);
	}
	
}
