package com.xb.conf;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class MyWebMvcConfigAdapter extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler(pathPatterns)
//		registry.addResourceHandler("/static");
		registry.addResourceHandler("/static/**").addResourceLocations(
				"classpath:/static/");
		super.addResourceHandlers(registry);
	}

	
	
}
