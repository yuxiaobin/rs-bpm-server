package com.xb.conf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

@Configuration
@EnableTransactionManagement
@ConditionalOnMissingClass("org.springframework.test.context.junit4.SpringJUnit4ClassRunner")
public class DatasourceConfig {
	
	private static final String MYSQL_PREFIX = "mysql.";
	private static final String DRUID_PREFIX = "druid.";
	
    @Autowired
    private Environment environment;
    
    @Bean
    public DataSource dataSource() {
    	
        Properties dbProperties = new Properties();
        Map<String, Object> map = new HashMap<String, Object>();
        MutablePropertySources properties =  ((AbstractEnvironment) environment).getPropertySources();
        Iterator<PropertySource<?>> it = properties.iterator(); 
        while (it.hasNext()) {
            PropertySource<?> propertySource = it.next();
            getPropertiesFromSource(propertySource, map);
        }
        dbProperties.putAll(map);
        
        DruidDataSource dataSource = null;
        try {
        	dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(dbProperties);
        	if(null != dataSource) {
//        		dataSource.setFilters("wall,stat");
//        		dataSource.setTimeBetweenLogStatsMillis(5000);
        		dataSource.init();
        	}
        } catch (Exception e) {
        	throw new RuntimeException("load datasource error, dbProperties is :" + dbProperties, e);
        }
        
        return dataSource;
    }
    
    private void getPropertiesFromSource(PropertySource<?> propertySource, Map<String, Object> map) {
    	
        if (propertySource instanceof MapPropertySource) {
            for (String key : ((MapPropertySource) propertySource).getPropertyNames()) {
            	if (key.startsWith(MYSQL_PREFIX)) {
					map.put(key.replaceFirst(MYSQL_PREFIX, ""), propertySource.getProperty(key));
				} else if (key.startsWith(DRUID_PREFIX)) {
					map.put(key.replaceFirst(DRUID_PREFIX, ""), propertySource.getProperty(key));
				}
            }
        }
        
        if (propertySource instanceof CompositePropertySource) {
            for (PropertySource<?> s : ((CompositePropertySource) propertySource).getPropertySources()) {
                getPropertiesFromSource(s, map);
            }
        }
    }
    
    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    
}
