#框架介绍

Spring boot + Mybatis-plus + Mysql

##Spring Boot 部分
用的Spring MVC + Freemarker(.ftl)

##Mybatis-plus部分
用的github上一个第三方插件，根据表自动生成service,mapper,xml, 单表CRUD无需写任何sql，调用现成方法即可（mapper.xml里面也没有sql）
具体使用，可参考[github上wiki](http://git.oschina.net/juapk/mybatis-plus)， 生成代码参考MybatisPlusGenerator.java

如果要切Oracle数据库，也是分分钟的事情

功能简单，只是实现工作流，其他功能都没有
有创建工作流

##Junit Test
使用了com.jayway.restassured:rest-assured来对Controller做测试

##生成war
mvn install -f pom-war.xml

##一些零散知识点
1.跨域配置
MyWebMvcConfigAdapter.java  
```
 public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/wfapi/**");//这里可以配置多个规则的URL开启跨域访问
	}
```  
2.Durid使用
* DatasourceConfig.java
* 还有在application.properties里加上配置
* 启用Druid自带监控：FilterConfig.java，ServletConfig.java

