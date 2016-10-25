package com.xb.conf;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.xb.service.impl", "com.xb.interceptor", "com.xb.controller","com.xb.aop" })
public class AppConfig {

	protected Environment env;

	@Inject
	public void setEnv(Environment env) {
		this.env = env;
	}

	@Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setPrefix("");
        resolver.setSuffix(".ftl");
        resolver.setContentType("text/html; charset=UTF-8");
        resolver.setRequestContextAttribute("base");
        return resolver;
    }
	
	@Bean
	public MyWebMvcConfigAdapter getMyWebMvcConfigAdapter(){
		return new MyWebMvcConfigAdapter();
	}
	
}
