package com.xb.conf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
@ConditionalOnMissingClass("org.springframework.test.context.junit4.SpringJUnit4ClassRunner")
public class FilterConfig {
	
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		filterRegistrationBean.addInitParameter("profileEnable", "true");
		return filterRegistrationBean;
	}
	
	/*@Bean
	public FilterRegistrationBean crossFilterBean() {
		
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new CorsFilter());
		filterRegistrationBean.addUrlPatterns("/wfapi/**");
		return filterRegistrationBean;
	}*/
	
	
	
}
