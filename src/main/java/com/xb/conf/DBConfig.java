package com.xb.conf;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;

@Configuration
public class DBConfig {

	@Bean
	public SqlSessionFactory getMybatisSqlSessionFactoryBean(DataSource dataSource) throws Exception {
		MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		ResourcePatternResolver resolver = (ResourcePatternResolver) new PathMatchingResourcePatternResolver();
		factoryBean.setTypeAliasesPackage("com.xb.persistent");
		Resource[] resources = resolver.getResources("classpath:/com/xb/persistent/mapper/*.xml");
		factoryBean.setMapperLocations(resources);
		factoryBean.setCache(null);
//		PageHelper pageHelper = new com.github.pagehelper.PageHelper();
//		Properties p = new Properties();
//		p.setProperty("dialect", "mysql");
//		pageHelper.setProperties(p);
//		factoryBean.setPlugins(new Interceptor[]{pageHelper});
		MybatisConfiguration configuration = new MybatisConfiguration();
		configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		factoryBean.setConfiguration(configuration);
		return factoryBean.getObject();
	}

	@Bean
	public MapperScannerConfigurer getMapperScannerConfigurer() {
		MapperScannerConfigurer scanConf = new MapperScannerConfigurer();
		scanConf.setBasePackage("com.xb.persistent.mapper");
		return scanConf;
	}

}
