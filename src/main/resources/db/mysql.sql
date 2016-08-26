-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.6.31 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 mybatis-plus 的数据库结构
CREATE DATABASE IF NOT EXISTS `mybatis-plus` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `mybatis-plus`;


-- 导出  表 mybatis-plus.rs_module 结构
CREATE TABLE IF NOT EXISTS `rs_module` (
  `MOD_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `NAME` varchar(50) COLLATE utf8_bin NOT NULL,
  `OUTPUT_CLASS` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `OUTPUT_OPT` varchar(5) COLLATE utf8_bin DEFAULT NULL,
  `WF_FLAG` varchar(2) COLLATE utf8_bin DEFAULT NULL,
  `RS_WF_ID` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `CREATED_BY` varchar(50) COLLATE utf8_bin NOT NULL,
  `CREATED_DT` datetime NOT NULL,
  `UPDATED_BY` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`MOD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 mybatis-plus.rs_workflow 结构
CREATE TABLE IF NOT EXISTS `rs_workflow` (
  `RS_WF_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `RS_WF_NAME` varchar(50) COLLATE utf8_bin NOT NULL,
  `CREATED_BY` varchar(50) COLLATE utf8_bin NOT NULL,
  `CREATED_DT` datetime NOT NULL,
  `UPDATED_BY` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`RS_WF_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 mybatis-plus.tbl_group 结构
CREATE TABLE IF NOT EXISTS `tbl_group` (
  `GROUP_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `GROUP_NAME` varchar(50) COLLATE utf8_bin NOT NULL,
  `CREATED_BY` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `CREATED_DT` datetime DEFAULT NULL,
  `UPDATED_BY` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 mybatis-plus.tbl_user 结构
CREATE TABLE IF NOT EXISTS `tbl_user` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `name` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '0',
  `age` int(3) DEFAULT '0',
  `email` varchar(50) COLLATE utf8_bin DEFAULT '0',
  `created_dt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 mybatis-plus.tbl_user2group 结构
CREATE TABLE IF NOT EXISTS `tbl_user2group` (
  `user2group_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `user_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `group_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`user2group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 mybatis-plus.wf_awt 结构
CREATE TABLE IF NOT EXISTS `wf_awt` (
  `wf_awt_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `INST_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `TASK_ID_CURR` varchar(50) COLLATE utf8_bin NOT NULL,
  `HIST_ID_PRE` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ASSIGNER_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `awt_begin` datetime DEFAULT NULL,
  `awt_end` datetime DEFAULT NULL,
  `awt_alarm` datetime DEFAULT NULL,
  `awt_title` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `awt_summary` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `complete_flag` varchar(1) COLLATE utf8_bin DEFAULT NULL,
  `CREATED_BY` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `CREATED_DT` datetime DEFAULT NULL,
  `UPDATED_BY` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`wf_awt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 mybatis-plus.wf_def 结构
CREATE TABLE IF NOT EXISTS `wf_def` (
  `WF_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `RS_WF_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `VERSION` int(10) NOT NULL,
  `CREATED_BY` varchar(50) COLLATE utf8_bin NOT NULL,
  `CREATED_DT` datetime NOT NULL,
  `UPDATED_BY` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`WF_ID`),
  KEY `FK_wf_def_rs_workflow` (`RS_WF_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 mybatis-plus.wf_instance 结构
CREATE TABLE IF NOT EXISTS `wf_instance` (
  `INST_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `WF_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `WF_STATUS` varchar(2) COLLATE utf8_bin NOT NULL,
  `RS_WF_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `INST_NUM` int(10) NOT NULL,
  `REF_MKID` varchar(50) COLLATE utf8_bin NOT NULL,
  `CURR_ASSIGNERS` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `CREATED_BY` varchar(50) COLLATE utf8_bin NOT NULL,
  `CREATED_DT` datetime NOT NULL,
  `UPDATED_BY` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`INST_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 mybatis-plus.wf_inst_hist 结构
CREATE TABLE IF NOT EXISTS `wf_inst_hist` (
  `HIST_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `INST_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `WF_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `TASK_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `OPT_USER` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `OPT_TYPE` varchar(2) COLLATE utf8_bin DEFAULT NULL,
  `OPT_COMM` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `OPT_SEQ` int(2) NOT NULL,
  `NEXT_ASSIGNER` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `TASK_OWNER` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `TASK_BEGIN` datetime DEFAULT NULL,
  `TASK_END` datetime DEFAULT NULL,
  `TASK_REND` datetime DEFAULT NULL,
  `CREATED_BY` varchar(50) COLLATE utf8_bin NOT NULL,
  `CREATED_DT` datetime NOT NULL,
  `UPDATED_BY` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`HIST_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 mybatis-plus.wf_task 结构
CREATE TABLE IF NOT EXISTS `wf_task` (
  `TASK_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `WF_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `TASK_PG_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `TASK_TYPE` varchar(50) COLLATE utf8_bin NOT NULL,
  `TASK_DESCP` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `POS_TOP` double NOT NULL,
  `POS_LEFT` double NOT NULL,
  `CREATED_BY` varchar(50) COLLATE utf8_bin NOT NULL,
  `CREATED_DT` datetime NOT NULL,
  `UPDATED_BY` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  `TX_CODE` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `TX_TYPE` varchar(5) COLLATE utf8_bin DEFAULT NULL,
  `BUZ_STATUS` varchar(5) COLLATE utf8_bin DEFAULT NULL,
  `TIME_LIMIT` int(11) DEFAULT NULL,
  `TIME_LIMIT_TP` varchar(5) COLLATE utf8_bin DEFAULT NULL,
  `ALARM_TIME` int(11) DEFAULT NULL,
  `ALARM_TIME_TP` varchar(5) COLLATE utf8_bin DEFAULT NULL,
  `MODULE_ID` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `Run_Param` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `TASK_DESCP_DISP` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `TX_CHOICES` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `TX_PR_CHOICES` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `TX_BK_CHOICES` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `SIGN_CHOICES` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`TASK_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 mybatis-plus.wf_task_assign 结构
CREATE TABLE IF NOT EXISTS `wf_task_assign` (
  `TASK_ASSIGN_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `TASK_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `ASSIGN_TYPE` varchar(2) COLLATE utf8_bin NOT NULL,
  `ASSIGN_REL_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `DEF_SEL_FLAG` varchar(1) COLLATE utf8_bin DEFAULT NULL,
  `SEL_ALL_FLAG` varchar(1) COLLATE utf8_bin DEFAULT NULL,
  `EXE_CONDITION` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `CREATED_BY` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `CREATED_DT` datetime DEFAULT NULL,
  `UPDATED_BY` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`TASK_ASSIGN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。


-- 导出  表 mybatis-plus.wf_task_conn 结构
CREATE TABLE IF NOT EXISTS `wf_task_conn` (
  `CONN_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `WF_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `SOURCE_TASK_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `TARGET_TASK_ID` varchar(50) COLLATE utf8_bin NOT NULL,
  `CONN_VAL` varchar(50) COLLATE utf8_bin NOT NULL,
  `CONN_DESCP` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `CREATED_BY` varchar(50) COLLATE utf8_bin NOT NULL,
  `CREATED_DT` datetime NOT NULL,
  `UPDATED_BY` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`CONN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
