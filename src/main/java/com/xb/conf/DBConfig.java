package com.xb.conf;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.github.pagehelper.PageHelper;

@Configuration
@PropertySource({ "classpath:/jdbc.properties"})
public class DBConfig {

//	protected Environment env;
	
	/*@Autowired
	MyConfigProperties myConfigProperties;
	
	@Value(value = "${jdbc.driver.name}")
	private String jdbcDriverName;*/

	/*@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public DBSettings getDBSettings() {
		System.out.println("Environment is +++++++++++" + env);
		DBSettings dbSet = new DBSettings();
		;
		dbSet.setDriverName("com.mysql.jdbc.Driver");
		dbSet.setPassword("");
		dbSet.setUsername("root");
		dbSet.setReaddbOpenUrl("jdbc:mysql://127.0.0.1:3306/mybatis-plus?useUnicode=true&characterEncoding=UTF8");
		return dbSet;
	}*/

	/*@Bean(destroyMethod = "close")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public DataSource myDs() {
		System.out.println("myConfigProperties===="+myConfigProperties);
		System.out.println("jdbcDriverName======="+jdbcDriverName);
		DBSettings env = new DBSettings();
		env.setDriverName("com.mysql.jdbc.Driver");
		env.setPassword("");
		env.setUsername("root");
		env.setReaddbOpenUrl("jdbc:mysql://127.0.0.1:3306/mybatis-plus?useUnicode=true&characterEncoding=UTF8");
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getDriverName());
		dataSource.setUrl(env.getReaddbOpenUrl());
		dataSource.setUsername(env.getUsername());
		dataSource.setPassword(env.getPassword());
		// dataSource.setDriverClassName(env.getRequiredProperty("jdbc.driver.name"));
		// dataSource.setUrl(env.getRequiredProperty("jdbc.readdb.open.url"));
		// dataSource.setUsername(env.getRequiredProperty("jdbc.username"));
		// dataSource.setPassword(env.getRequiredProperty("jdbc.password"));
		dataSource.setTestOnBorrow(true);
		dataSource.setValidationQuery("select 1 from dual");
		// dataSource.setMaxTotal(env.getRequiredProperty("jdbc.readdb.maxtotal",
		// Integer.class));
		// dataSource.setMaxIdle(env.getRequiredProperty("jdbc.readdb.maxidle",
		// Integer.class));
		// dataSource.setTimeBetweenEvictionRunsMillis(env.getRequiredProperty("jdbc.readdb.TimeBetweenEvictionRunsMillis",
		// Long.class));
		// dataSource.setMinEvictableIdleTimeMillis(env.getRequiredProperty("jdbc.readdb.MinEvictableIdleTimeMillis",
		// Long.class));
		// dataSource.setSoftMinEvictableIdleTimeMillis(env.getRequiredProperty("jdbc.readdb.SoftMinEvictableIdleTimeMillis",
		// Long.class));
		return dataSource;
	}*/

	/*@Bean
	public PaginationInterceptor getPaginationInterceptor() {
		PaginationInterceptor intpor = new PaginationInterceptor();
		intpor.setDialectType("mqsql");
		return intpor;
	}*/
	
	

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SqlSessionFactory getMybatisSqlSessionFactoryBean(DataSource myDs) throws Exception {
		MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
		factoryBean.setDataSource(myDs);
		ResourcePatternResolver resolver = (ResourcePatternResolver) new PathMatchingResourcePatternResolver();
//		Resource res = resolver.getResource("classpath:mysql-config.xml");
//		factoryBean.setConfigLocation(res);
		factoryBean.setTypeAliasesPackage("com.xb.persistent");
		Resource[] resources = resolver.getResources("classpath:/com/xb/persistent/mapper/*.xml");
		factoryBean.setMapperLocations(resources);
//		factoryBean.setPlugins(new Interceptor[] { getPaginationInterceptor() });
		factoryBean.setCache(null);
		PageHelper pageHelper = new com.github.pagehelper.PageHelper();
		Properties p = new Properties();
		p.setProperty("dialect", "mysql");
		pageHelper.setProperties(p);
//		pageHelper.setSqlUtilConfig(config);
		factoryBean.setPlugins(new Interceptor[]{pageHelper});
		return factoryBean.getObject();
	}

	@Bean
	public MapperScannerConfigurer getMapperScannerConfigurer() {
		MapperScannerConfigurer scanConf = new MapperScannerConfigurer();
		scanConf.setBasePackage("com.xb.persistent.mapper");
		return scanConf;
	}

	/*@Inject
	public void setEnv(Environment env) {
		this.env = env;
	}*/

}
