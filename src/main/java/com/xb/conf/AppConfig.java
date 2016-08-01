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


	/*@Bean
	public FreeMarkerViewResolver freemarkerViewResolver() {
		FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
		resolver.setPrefix("/public/templates");
		resolver.setSuffix(".ftl");
		resolver.setContentType("text/html;charset=UTF-8");
		resolver.setViewClass(FreeMarkerView.class);
		resolver.setExposeSpringMacroHelpers(true);
		resolver.setExposeRequestAttributes(true);
		return resolver;
	}*/
	/*
	 * @Bean public FreeMarkerConfigurer freeMarkerConfig() {
	 * FreeMarkerConfigurer configurer = new FreeMarkerConfigurer(); return
	 * configurer; }
	 * 
	 * protected void setFreemarkerSettings(Properties settings) {
	 * settings.setProperty("default_encoding", "UTF-8");
	 * settings.setProperty("url_escaping_charset", "UTF-8");
	 * settings.setProperty("number_format", "0.##"); }
	 * 
	 */
	
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
