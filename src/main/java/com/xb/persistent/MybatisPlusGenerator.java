package com.xb.persistent;

import com.baomidou.mybatisplus.annotations.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.ConfigDataSource;
import com.baomidou.mybatisplus.generator.ConfigGenerator;
import com.baomidou.mybatisplus.generator.ConfigIdType;

public class MybatisPlusGenerator {
	
	public static void main( String[] args ) {
		ConfigGenerator cg = getConfigGenerator();
		/* 此处设置 String 类型数据库ID，默认Long类型 */
		cg.setConfigIdType(ConfigIdType.STRING);
		/* oracle 数据库相关配置 */
		setupMysql(cg);

		/*
		 * 表主键 ID 生成类型, 自增该设置无效。
		 * <p>
		 * IdType.AUTO 			数据库ID自增
		 * IdType.INPUT			用户输入ID
		 * IdType.ID_WORKER		全局唯一ID，内容为空自动填充（默认配置）
		 * IdType.UUID			全局唯一ID，内容为空自动填充
		 * </p>
		 */
		cg.setIdType(IdType.UUID);
		
		/*
		 * 指定生成表名（默认，所有表）
		 */
		//cg.setTableNames(new String[]{"user"});
		
		AutoGenerator.run(cg);
	}
	

	private static ConfigGenerator setupMysql(ConfigGenerator cg){
		cg.setConfigDataSource(ConfigDataSource.MYSQL);
		cg.setDbDriverName("com.mysql.jdbc.Driver");
		cg.setDbUser("root");
		cg.setDbPassword("");
		cg.setDbUrl("jdbc:mysql://127.0.0.1:3306/mybatis-plus?useUnicode=true&characterEncoding=UTF8");
		return cg;
	}
	
	private static ConfigGenerator setupOracle(ConfigGenerator cg){
		cg.setConfigDataSource(ConfigDataSource.ORACLE);
		cg.setDbDriverName("oracle.jdbc.driver.OracleDriver");
		cg.setDbUser("rsmgr");
		cg.setDbPassword("rsmgr");
		cg.setDbUrl("jdbc:oracle:thin:@localhost:1521:mgrdb");
		return cg;
	}
	
	private static ConfigGenerator getConfigGenerator(){
		ConfigGenerator cg = new ConfigGenerator();
        cg.setEntityPackage("com.xb.persistent");//entity 实体包路径
        cg.setMapperPackage("com.xb.persistent.mapper");//mapper 映射文件路径
        cg.setServicePackage("com.xb.service");//service 层路径
        cg.setXmlPackage("com.xb.persistent.mapper");//xml层路径（可以不写）
        cg.setServiceImplPackage("com.xb.service.impl");//serviceimpl层路径（可以不写）

		/* 此处可以配置 SuperServiceImpl 子类路径，默认如下 */
        //cg.setSuperServiceImpl("com.baomidou.framework.service.impl.SuperServiceImpl");

		/* 此处设置 String 类型数据库ID，默认Long类型 */
        //cg.setConfigIdType(ConfigIdType.STRING);

//        cg.setSaveDir("D:/workspace2/Test2/src/main/java");// 生成文件保存位置
        cg.setSaveDir("D:/workspace2/Test/src/main/java");// 生成文件保存位置
		/*
         * 设置数据库字段是否为驼峰命名，驼峰 true 下划线分割 false
		 */
        cg.setDbColumnUnderline(false);
//        cg.setColumnHump(false);
        /*
         * 表是否包括前缀
		 * <p>
		 * 例如 mp_user 生成实体类 false 为 MpUser , true 为 User
		 * </p>
		 */
        cg.setDbPrefix(false);
        /*
         * 默认值为true , 是否覆盖当前路径下已有文件
         */
        cg.setFileOverride(true);
        
        String[] tableNames = {"rs_module","rs_workflow","wf_def","wf_task",
        		"wf_task_conn","wf_group","wf_instance","wf_inst_hist"};
        cg.setTableNames(tableNames);
        return cg;
	}
}
