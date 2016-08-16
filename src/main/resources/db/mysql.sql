
CREATE DATABASE IF NOT EXISTS `mybatis-plus` ;
USE `mybatis-plus`;


-- 导出  表 mybatis-plus.rs_module 结构
CREATE TABLE IF NOT EXISTS `rs_module` (
  `MOD_ID` varchar(50) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `OUTPUT_CLASS` varchar(50) DEFAULT NULL,
  `OUTPUT_OPT` varchar(5) DEFAULT NULL,
  `WF_FLAG` varchar(2) DEFAULT NULL,
  `RS_WF_ID` varchar(50) DEFAULT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `CREATED_DT` datetime NOT NULL,
  `UPDATED_BY` varchar(50) DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`MOD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 正在导出表  mybatis-plus.rs_module 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `rs_module` DISABLE KEYS */;
INSERT INTO `rs_module` (`MOD_ID`, `NAME`, `OUTPUT_CLASS`, `OUTPUT_OPT`, `WF_FLAG`, `RS_WF_ID`, `CREATED_BY`, `CREATED_DT`, `UPDATED_BY`, `UPDATED_DT`) VALUES
	('0f067433d3214ca8ac74cba11a126032', 'AAA', NULL, NULL, 'T', 'fa294e1ee2a1456b8cda007468064ee9', 'system', '2016-08-05 13:10:54', 'admin', '2016-08-05 13:22:40'),
	('c3f28e95f18645c5a33edd8ed8e6f325', 'test', NULL, NULL, 'T', 'f1a3f687b04d49a5bfd02c6dbc19d710', 'system', '2016-08-05 15:03:47', 'admin', '2016-08-05 15:04:32');
/*!40000 ALTER TABLE `rs_module` ENABLE KEYS */;


-- 导出  表 mybatis-plus.rs_workflow 结构
CREATE TABLE IF NOT EXISTS `rs_workflow` (
  `RS_WF_ID` varchar(50) NOT NULL,
  `RS_WF_NAME` varchar(50) NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `CREATED_DT` datetime NOT NULL,
  `UPDATED_BY` varchar(50) DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`RS_WF_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 正在导出表  mybatis-plus.rs_workflow 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `rs_workflow` DISABLE KEYS */;
INSERT INTO `rs_workflow` (`RS_WF_ID`, `RS_WF_NAME`, `CREATED_BY`, `CREATED_DT`, `UPDATED_BY`, `UPDATED_DT`) VALUES
	('f1a3f687b04d49a5bfd02c6dbc19d710', 'Workflow for moduleName:TODO', 'admin', '2016-08-05 15:04:32', NULL, NULL),
	('fa294e1ee2a1456b8cda007468064ee9', 'Workflow for moduleName:TODO', 'admin', '2016-08-05 13:22:40', NULL, NULL);
/*!40000 ALTER TABLE `rs_workflow` ENABLE KEYS */;


-- 导出  表 mybatis-plus.tbl_group 结构
CREATE TABLE IF NOT EXISTS `tbl_group` (
  `GROUP_ID` varchar(50) NOT NULL,
  `GROUP_NAME` varchar(50) NOT NULL,
  `CREATED_BY` varchar(50) DEFAULT NULL,
  `CREATED_DT` datetime DEFAULT NULL,
  `UPDATED_BY` varchar(50) DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 正在导出表  mybatis-plus.tbl_group 的数据：~10 rows (大约)
/*!40000 ALTER TABLE `tbl_group` DISABLE KEYS */;
INSERT INTO `tbl_group` (`GROUP_ID`, `GROUP_NAME`, `CREATED_BY`, `CREATED_DT`, `UPDATED_BY`, `UPDATED_DT`) VALUES
	('1', 'group1', NULL, NULL, NULL, NULL),
	('10', 'group10', NULL, NULL, NULL, NULL),
	('2', 'group2', NULL, NULL, NULL, NULL),
	('3', 'group3', NULL, NULL, NULL, NULL),
	('4', 'group4', NULL, NULL, NULL, NULL),
	('5', 'group5', NULL, NULL, NULL, NULL),
	('6', 'group6', NULL, NULL, NULL, NULL),
	('7', 'group7', NULL, NULL, NULL, NULL),
	('8', 'group8', NULL, NULL, NULL, NULL),
	('9', 'group9', NULL, NULL, NULL, NULL);
/*!40000 ALTER TABLE `tbl_group` ENABLE KEYS */;


-- 导出  表 mybatis-plus.tbl_user 结构
CREATE TABLE IF NOT EXISTS `tbl_user` (
  `id` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL DEFAULT '0',
  `age` int(3) DEFAULT '0',
  `email` varchar(50) DEFAULT '0',
  `created_dt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 正在导出表  mybatis-plus.tbl_user 的数据：~33 rows (大约)
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;
INSERT INTO `tbl_user` (`id`, `name`, `age`, `email`, `created_dt`) VALUES
	('1', '1', 1, '0', NULL),
	('11', 'name11', 0, '0', NULL),
	('12', 'name12', 0, '0', NULL),
	('123', '123', 123, '0', '2016-08-08 09:05:24'),
	('13', 'name13', 0, '0', NULL),
	('14', 'name14', 0, '0', NULL),
	('15', 'name15', 0, '0', NULL),
	('16', 'name16', 0, '0', NULL),
	('17', 'name17', 0, '0', NULL),
	('18', 'name18', 0, '0', NULL),
	('19', 'name19', 0, '0', NULL),
	('2', '2', 0, '0', NULL),
	('20', 'name20', 0, '0', NULL),
	('21', 'name21', 0, '0', NULL),
	('22', 'name22', 0, '0', NULL),
	('23', 'name23', 0, '0', NULL),
	('24', 'name24', 0, '0', NULL),
	('25', 'name25', 0, '0', NULL),
	('26', 'name26', 0, '0', NULL),
	('27', 'name27', 0, '0', NULL),
	('28', 'name28', 0, '0', NULL),
	('29', 'name29', 0, '0', NULL),
	('3', '3', 0, '0', NULL),
	('4', '4', 0, '0', NULL);
/*!40000 ALTER TABLE `tbl_user` ENABLE KEYS */;


-- 导出  表 mybatis-plus.tbl_user2group 结构
CREATE TABLE IF NOT EXISTS `tbl_user2group` (
  `user2group_id` varchar(50) NOT NULL,
  `user_id` varchar(50) DEFAULT NULL,
  `group_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user2group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 正在导出表  mybatis-plus.tbl_user2group 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `tbl_user2group` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_user2group` ENABLE KEYS */;


-- 导出  表 mybatis-plus.wf_def 结构
CREATE TABLE IF NOT EXISTS `wf_def` (
  `WF_ID` varchar(50) NOT NULL,
  `RS_WF_ID` varchar(50) NOT NULL,
  `VERSION` int(10) NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `CREATED_DT` datetime NOT NULL,
  `UPDATED_BY` varchar(50) DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`WF_ID`),
  KEY `FK_wf_def_rs_workflow` (`RS_WF_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 正在导出表  mybatis-plus.wf_def 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `wf_def` DISABLE KEYS */;
INSERT INTO `wf_def` (`WF_ID`, `RS_WF_ID`, `VERSION`, `CREATED_BY`, `CREATED_DT`, `UPDATED_BY`, `UPDATED_DT`) VALUES
	('616d85e4d2534969a56b7dce897e82ab', 'fa294e1ee2a1456b8cda007468064ee9', 2, 'admin', '2016-08-05 13:37:15', NULL, NULL),
	('987da299429547a7aca9597f027979a8', 'fa294e1ee2a1456b8cda007468064ee9', 1, 'admin', '2016-08-05 13:22:40', NULL, NULL),
	('99fef0712cb84a178af69dcd1109e335', 'fa294e1ee2a1456b8cda007468064ee9', 3, 'admin', '2016-08-10 09:49:15', NULL, NULL),
	('bf28e46d610c407a91f7d80c4d776510', 'f1a3f687b04d49a5bfd02c6dbc19d710', 1, 'admin', '2016-08-05 15:04:32', NULL, NULL);
/*!40000 ALTER TABLE `wf_def` ENABLE KEYS */;


-- 导出  表 mybatis-plus.wf_instance 结构
CREATE TABLE IF NOT EXISTS `wf_instance` (
  `INST_ID` varchar(50) NOT NULL,
  `WF_ID` varchar(50) NOT NULL,
  `WF_STATUS` varchar(2) NOT NULL,
  `CURR_TASK_ID` varchar(50) DEFAULT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `CREATED_DT` datetime NOT NULL,
  `UPDATED_BY` varchar(50) DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`INST_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 正在导出表  mybatis-plus.wf_instance 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `wf_instance` DISABLE KEYS */;
INSERT INTO `wf_instance` (`INST_ID`, `WF_ID`, `WF_STATUS`, `CURR_TASK_ID`, `CREATED_BY`, `CREATED_DT`, `UPDATED_BY`, `UPDATED_DT`) VALUES
	('7f4b5145969047c8b44b2922a7ed868c', '616d85e4d2534969a56b7dce897e82ab', 'D', 'bd35e2ad719b419d82b826f3e28aefea', 'staff', '2016-08-05 13:37:25', 'manager', '2016-08-05 13:40:02'),
	('c9be3e8ada95497dad6f37f28b56f041', 'bf28e46d610c407a91f7d80c4d776510', 'D', 'bde6c424ab4840e5a947473d3a6d9546', 'staff', '2016-08-05 15:05:58', 'manager', '2016-08-05 15:07:08'),
	('ea2249aaba5a471d9fd69e6ef699ca47', '99fef0712cb84a178af69dcd1109e335', 'I', 'abc1d439d5254d3d8be4e3f0a3d4bdf1', 'staff', '2016-08-10 11:03:30', 'staff', '2016-08-10 11:03:30'),
	('ebdd7b2bae9e4784bab97d70ef76f917', '987da299429547a7aca9597f027979a8', 'D', 'd35335d931554f27b024f21bd76f5fce', 'staff', '2016-08-05 13:22:55', 'manager', '2016-08-05 13:34:26');
/*!40000 ALTER TABLE `wf_instance` ENABLE KEYS */;


-- 导出  表 mybatis-plus.wf_inst_hist 结构
CREATE TABLE IF NOT EXISTS `wf_inst_hist` (
  `HIST_ID` varchar(50) NOT NULL,
  `INST_ID` varchar(50) NOT NULL,
  `WF_ID` varchar(50) NOT NULL,
  `TASK_ID` varchar(50) NOT NULL,
  `OPT_USER` varchar(50) DEFAULT NULL,
  `OPT_TYPE` varchar(2) DEFAULT NULL,
  `OPT_SEQ` int(2) NOT NULL,
  `NEXT_ASSIGNER` varchar(50) DEFAULT NULL,
  `STATUS` varchar(2) NOT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `CREATED_DT` datetime NOT NULL,
  `UPDATED_BY` varchar(50) DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`HIST_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 正在导出表  mybatis-plus.wf_inst_hist 的数据：~17 rows (大约)
/*!40000 ALTER TABLE `wf_inst_hist` DISABLE KEYS */;
INSERT INTO `wf_inst_hist` (`HIST_ID`, `INST_ID`, `WF_ID`, `TASK_ID`, `OPT_USER`, `OPT_TYPE`, `OPT_SEQ`, `NEXT_ASSIGNER`, `STATUS`, `CREATED_BY`, `CREATED_DT`, `UPDATED_BY`, `UPDATED_DT`) VALUES
	('08ada6f0cd1345949bfa2abc6fe33f48', '7f4b5145969047c8b44b2922a7ed868c', '616d85e4d2534969a56b7dce897e82ab', '58f904f8f7954b598649352fe31bbdc6', 'manager', 'AP', 7, ',manager,', 'D', 'manager', '2016-08-05 13:40:02', 'manager', '2016-08-05 13:40:02'),
	('188efbc55cfc49b2b1ef7e46705ed0d2', 'c9be3e8ada95497dad6f37f28b56f041', 'bf28e46d610c407a91f7d80c4d776510', 'f4bdef07ad8b4839bf1588ce16bc9fee', 'manager', 'RJ', 3, 'manager', 'D', 'manager', '2016-08-05 15:06:35', 'manager', '2016-08-05 15:06:40'),
	('1a5b0837f3654bee9570cf1b997244a2', 'ebdd7b2bae9e4784bab97d70ef76f917', '987da299429547a7aca9597f027979a8', '2153492e4d6c4794a58bbaa5d9ab07e2', 'manager', 'AP', 4, ',manager,', 'D', 'manager', '2016-08-05 13:34:25', 'manager', '2016-08-05 13:34:25'),
	('28ad07bc446249de834af33e8f543be3', 'c9be3e8ada95497dad6f37f28b56f041', 'bf28e46d610c407a91f7d80c4d776510', 'cd4ab1052bfa4616bf84d1aee72e4316', 'staff', 'RQ', 5, ',manager,', 'D', 'staff', '2016-08-05 15:06:55', 'manager', '2016-08-05 15:07:06'),
	('2b7cfab9fb744320a1dd703fb8767bb3', 'c9be3e8ada95497dad6f37f28b56f041', 'bf28e46d610c407a91f7d80c4d776510', '1b0db364b43d4656b42a1aef14f60b5c', 'manager', 'RJ', 4, 'staff', 'D', 'manager', '2016-08-05 15:06:40', 'staff', '2016-08-05 15:06:55'),
	('418867145a0e4f36bdbc77a969b0e5ae', '7f4b5145969047c8b44b2922a7ed868c', '616d85e4d2534969a56b7dce897e82ab', 'b53bffcd844042d9846d117c6b49a8d5', 'manager', 'AP', 2, ',manager,', 'D', 'manager', '2016-08-05 13:38:41', 'manager', '2016-08-05 13:39:08'),
	('42947ef47e5d47c1839e6cfa1c369cee', '7f4b5145969047c8b44b2922a7ed868c', '616d85e4d2534969a56b7dce897e82ab', 'b53bffcd844042d9846d117c6b49a8d5', 'manager', 'AP', 6, ',manager,', 'D', 'manager', '2016-08-05 13:39:59', 'manager', '2016-08-05 13:40:02'),
	('67d6c37a38cd48e3823d7c55adebd7de', 'ebdd7b2bae9e4784bab97d70ef76f917', '987da299429547a7aca9597f027979a8', 'dfb4aceb931b416595d63cf0ec474b4a', 'staff', 'RQ', 1, ',manager,', 'D', 'staff', '2016-08-05 13:22:55', 'manager', '2016-08-05 13:29:36'),
	('80f2253626af468a9ce43ad89aa9cb4c', 'ebdd7b2bae9e4784bab97d70ef76f917', '987da299429547a7aca9597f027979a8', '2153492e4d6c4794a58bbaa5d9ab07e2', 'manager', 'RJ', 2, 'staff', 'D', 'manager', '2016-08-05 13:29:36', 'staff', '2016-08-05 13:34:08'),
	('84c9dd34c40247d2ab802f46ef5dea66', '7f4b5145969047c8b44b2922a7ed868c', '616d85e4d2534969a56b7dce897e82ab', '9be280cda3a84f218f029f44172d8047', 'staff', 'RQ', 1, ',manager,', 'D', 'staff', '2016-08-05 13:37:25', 'manager', '2016-08-05 13:38:41'),
	('8eae53aa49634b30ab9ac21332108c8c', 'c9be3e8ada95497dad6f37f28b56f041', 'bf28e46d610c407a91f7d80c4d776510', '1b0db364b43d4656b42a1aef14f60b5c', 'manager', 'AP', 6, NULL, 'D', 'manager', '2016-08-05 15:07:06', 'manager', '2016-08-05 15:07:08'),
	('a072ba1adf834c65b45ed262a3ea4cc9', '7f4b5145969047c8b44b2922a7ed868c', '616d85e4d2534969a56b7dce897e82ab', 'b53bffcd844042d9846d117c6b49a8d5', 'manager', 'RJ', 4, 'staff', 'D', 'manager', '2016-08-05 13:39:28', 'staff', '2016-08-05 13:39:46'),
	('a498547f91504323a6286518749c8bf8', '7f4b5145969047c8b44b2922a7ed868c', '616d85e4d2534969a56b7dce897e82ab', '58f904f8f7954b598649352fe31bbdc6', 'manager', 'RJ', 3, 'manager', 'D', 'manager', '2016-08-05 13:39:08', 'manager', '2016-08-05 13:39:28'),
	('ad1e1be6dbc9450ca590d6a051ccc1b1', 'ea2249aaba5a471d9fd69e6ef699ca47', '99fef0712cb84a178af69dcd1109e335', '2a6af2700bd34310b35159b5c580120d', 'staff', 'RQ', 1, ',1,name12,123,', 'I', 'staff', '2016-08-10 11:03:30', 'staff', '2016-08-10 11:03:30'),
	('c6243fb5b2414bb9a3d7c32f3b624016', 'c9be3e8ada95497dad6f37f28b56f041', 'bf28e46d610c407a91f7d80c4d776510', '1b0db364b43d4656b42a1aef14f60b5c', 'manager', 'AP', 2, NULL, 'D', 'manager', '2016-08-05 15:06:29', 'manager', '2016-08-05 15:06:35'),
	('e76521564b9640f68b36bdc5a7973fe3', 'c9be3e8ada95497dad6f37f28b56f041', 'bf28e46d610c407a91f7d80c4d776510', 'cd4ab1052bfa4616bf84d1aee72e4316', 'staff', 'RQ', 1, ',manager,', 'D', 'staff', '2016-08-05 15:05:58', 'manager', '2016-08-05 15:06:29'),
	('e85de34c53df41bfaa654d4106b71a98', 'c9be3e8ada95497dad6f37f28b56f041', 'bf28e46d610c407a91f7d80c4d776510', 'f4bdef07ad8b4839bf1588ce16bc9fee', 'manager', 'AP', 7, NULL, 'D', 'manager', '2016-08-05 15:07:08', 'manager', '2016-08-05 15:07:08'),
	('fafbd1860c5d4bf29812216dff7d71c6', 'ebdd7b2bae9e4784bab97d70ef76f917', '987da299429547a7aca9597f027979a8', 'dfb4aceb931b416595d63cf0ec474b4a', 'staff', 'RQ', 3, ',manager,', 'D', 'staff', '2016-08-05 13:34:08', 'manager', '2016-08-05 13:34:25'),
	('ff002b0b684948ff8302263f271abbe2', '7f4b5145969047c8b44b2922a7ed868c', '616d85e4d2534969a56b7dce897e82ab', '9be280cda3a84f218f029f44172d8047', 'staff', 'RQ', 5, ',manager,', 'D', 'staff', '2016-08-05 13:39:46', 'manager', '2016-08-05 13:39:59');
/*!40000 ALTER TABLE `wf_inst_hist` ENABLE KEYS */;


-- 导出  表 mybatis-plus.wf_task 结构

CREATE TABLE `wf_task` (
	`TASK_ID` VARCHAR(50) NOT NULL,
	`WF_ID` VARCHAR(50) NOT NULL,
	`TASK_PG_ID` VARCHAR(50) NOT NULL,
	`TASK_TYPE` VARCHAR(50) NOT NULL,
	`TASK_DESCP` VARCHAR(100) NULL DEFAULT NULL,
	`POS_TOP` DOUBLE NOT NULL,
	`POS_LEFT` DOUBLE NOT NULL,
	`ASSIGN_USERS` VARCHAR(200) NULL DEFAULT NULL,
	`ASSIGN_GROUPS` VARCHAR(200) NULL DEFAULT NULL,
	`CREATED_BY` VARCHAR(50) NOT NULL,
	`CREATED_DT` DATETIME NOT NULL,
	`UPDATED_BY` VARCHAR(50) NULL DEFAULT NULL,
	`UPDATED_DT` DATETIME NULL DEFAULT NULL,
	`TX_CODE` VARCHAR(50) NULL DEFAULT NULL,
	`TX_TYPE` VARCHAR(5) NULL DEFAULT NULL,
	`BUZ_STATUS` VARCHAR(5) NULL DEFAULT NULL,
	`TIME_LIMIT` INT(11) NULL DEFAULT NULL,
	`TIME_LIMIT_TP` VARCHAR(5) NULL DEFAULT NULL,
	`ALARM_TIME` INT(11) NULL DEFAULT NULL,
	`ALARM_TIME_TP` VARCHAR(5) NULL DEFAULT NULL,
	`MODULE_ID` VARCHAR(50) NULL DEFAULT NULL,
	`Run_Param` VARCHAR(500) NULL DEFAULT NULL,
	`TASK_DESCP_DISP` VARCHAR(500) NULL DEFAULT NULL,
	`TX_CHOICES` VARCHAR(500) NULL DEFAULT NULL,
	`TX_PR_CHOICES` VARCHAR(500) NULL DEFAULT NULL,
	`TX_BK_CHOICES` VARCHAR(50) NULL DEFAULT NULL,
	`SIGN_CHOICES` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`TASK_ID`)
)ENGINE=InnoDB;

-- 正在导出表  mybatis-plus.wf_task 的数据：~15 rows (大约)
/*!40000 ALTER TABLE `wf_task` DISABLE KEYS */;
INSERT INTO `wf_task` (`TASK_ID`, `WF_ID`, `TASK_PG_ID`, `TASK_TYPE`, `TASK_DESCP`, `POS_TOP`, `POS_LEFT`, `ASSIGN_USERS`, `ASSIGN_GROUPS`, `CREATED_BY`, `CREATED_DT`, `UPDATED_BY`, `UPDATED_DT`) VALUES
	('06dace37bfcc484a80d18ef988ed3769', '99fef0712cb84a178af69dcd1109e335', 'jsPlumb_2_1', 'E', 'End Node2', 432, 354, NULL, NULL, 'admin', '2016-08-10 09:49:15', NULL, NULL),
	('1b0db364b43d4656b42a1aef14f60b5c', 'bf28e46d610c407a91f7d80c4d776510', '1470380632476', 'U', 'User Task123', 150, 351, ',manager,', NULL, 'admin', '2016-08-05 15:04:32', NULL, NULL),
	('2153492e4d6c4794a58bbaa5d9ab07e2', '987da299429547a7aca9597f027979a8', '1470374515680', 'U', 'User Task aaa', 139, 129, ',manager,', NULL, 'admin', '2016-08-05 13:22:40', NULL, NULL),
	('2a6af2700bd34310b35159b5c580120d', '99fef0712cb84a178af69dcd1109e335', 'start-task', 'S', 'Start Node13', 35, 349, NULL, NULL, 'admin', '2016-08-10 09:49:15', NULL, NULL),
	('58f904f8f7954b598649352fe31bbdc6', '616d85e4d2534969a56b7dce897e82ab', '1470375353543', 'U', 'CFO approve', 288, 357, ',manager,', NULL, 'admin', '2016-08-05 13:37:15', NULL, NULL),
	('7716494f34cd42f88a219b54587459b8', '99fef0712cb84a178af69dcd1109e335', '1470375353543', 'U', 'CFO approve', 288, 357, ',manager,', ',manager,', 'admin', '2016-08-10 09:49:15', NULL, NULL),
	('9be280cda3a84f218f029f44172d8047', '616d85e4d2534969a56b7dce897e82ab', 'start-task', 'S', 'Start Node13', 35, 349, NULL, NULL, 'admin', '2016-08-05 13:37:15', NULL, NULL),
	('abc1d439d5254d3d8be4e3f0a3d4bdf1', '99fef0712cb84a178af69dcd1109e335', '1470375351774', 'U', 'Approve Officer task', 153, 231, ',1,name12,123,', ',group1,group10,group2,', 'admin', '2016-08-10 09:49:15', NULL, NULL),
	('b53bffcd844042d9846d117c6b49a8d5', '616d85e4d2534969a56b7dce897e82ab', '1470375351774', 'U', 'Approve Officer task', 153, 231, ',manager,', NULL, 'admin', '2016-08-05 13:37:15', NULL, NULL),
	('bd35e2ad719b419d82b826f3e28aefea', '616d85e4d2534969a56b7dce897e82ab', 'jsPlumb_2_1', 'E', 'End Node2', 432, 354, NULL, NULL, 'admin', '2016-08-05 13:37:15', NULL, NULL),
	('bde6c424ab4840e5a947473d3a6d9546', 'bf28e46d610c407a91f7d80c4d776510', 'jsPlumb_2_1', 'E', 'End Node', 428, 353, NULL, NULL, 'admin', '2016-08-05 15:04:32', NULL, NULL),
	('cd4ab1052bfa4616bf84d1aee72e4316', 'bf28e46d610c407a91f7d80c4d776510', 'start-task', 'S', 'Start Node234', 10, 349, NULL, NULL, 'admin', '2016-08-05 15:04:32', NULL, NULL),
	('d35335d931554f27b024f21bd76f5fce', '987da299429547a7aca9597f027979a8', 'jsPlumb_2_1', 'E', 'End Node2', 258, 365, NULL, NULL, 'admin', '2016-08-05 13:22:40', NULL, NULL),
	('dfb4aceb931b416595d63cf0ec474b4a', '987da299429547a7aca9597f027979a8', 'start-task', 'S', 'Start Node1', 35, 349, NULL, NULL, 'admin', '2016-08-05 13:22:40', NULL, NULL),
	('f4bdef07ad8b4839bf1588ce16bc9fee', 'bf28e46d610c407a91f7d80c4d776510', '1470380637187', 'U', 'User Task', 294, 355, NULL, NULL, 'admin', '2016-08-05 15:04:32', NULL, NULL);
/*!40000 ALTER TABLE `wf_task` ENABLE KEYS */;


-- 导出  表 mybatis-plus.wf_task_assign 结构
CREATE TABLE IF NOT EXISTS `wf_task_assign` (
  `TASK_ASSIGN_ID` varchar(50) NOT NULL,
  `TASK_ID` varchar(50) NOT NULL,
  `ASSIGN_TYPE` varchar(2) NOT NULL,
  `ASSIGN_REL_ID` varchar(50) NOT NULL,
  `DEF_SEL_FLAG` varchar(1) DEFAULT NULL,
  `SEL_ALL_FLAG` varchar(1) DEFAULT NULL,
  `EXE_CONDITION` varchar(500) DEFAULT NULL,
  `CREATED_BY` varchar(50) DEFAULT NULL,
  `CREATED_DT` datetime DEFAULT NULL,
  `UPDATED_BY` varchar(50) DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`TASK_ASSIGN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 正在导出表  mybatis-plus.wf_task_assign 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `wf_task_assign` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_task_assign` ENABLE KEYS */;


-- 导出  表 mybatis-plus.wf_task_conn 结构
CREATE TABLE IF NOT EXISTS `wf_task_conn` (
  `CONN_ID` varchar(50) NOT NULL,
  `WF_ID` varchar(50) NOT NULL,
  `SOURCE_TASK_ID` varchar(50) NOT NULL,
  `TARGET_TASK_ID` varchar(50) NOT NULL,
  `CONN_VAL` varchar(50) NOT NULL,
  `CONN_DESCP` varchar(100) DEFAULT NULL,
  `CREATED_BY` varchar(50) NOT NULL,
  `CREATED_DT` datetime NOT NULL,
  `UPDATED_BY` varchar(50) DEFAULT NULL,
  `UPDATED_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`CONN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 正在导出表  mybatis-plus.wf_task_conn 的数据：~11 rows (大约)
/*!40000 ALTER TABLE `wf_task_conn` DISABLE KEYS */;
INSERT INTO `wf_task_conn` (`CONN_ID`, `WF_ID`, `SOURCE_TASK_ID`, `TARGET_TASK_ID`, `CONN_VAL`, `CONN_DESCP`, `CREATED_BY`, `CREATED_DT`, `UPDATED_BY`, `UPDATED_DT`) VALUES
	('09061e5cbfa14daab452c0cd8c8551cc', 'bf28e46d610c407a91f7d80c4d776510', '1b0db364b43d4656b42a1aef14f60b5c', 'f4bdef07ad8b4839bf1588ce16bc9fee', '', 'Next', 'admin', '2016-08-05 15:04:32', NULL, NULL),
	('2b8261342bf445fcb2f25446d2e474f6', '616d85e4d2534969a56b7dce897e82ab', '58f904f8f7954b598649352fe31bbdc6', 'bd35e2ad719b419d82b826f3e28aefea', '', 'Next', 'admin', '2016-08-05 13:37:15', NULL, NULL),
	('4d3d7bbc655140419bf734e1aa07152f', '99fef0712cb84a178af69dcd1109e335', '7716494f34cd42f88a219b54587459b8', '06dace37bfcc484a80d18ef988ed3769', '', 'Next', 'admin', '2016-08-10 09:49:15', NULL, NULL),
	('53d8e5a146b94b858e33b9e97600eade', '987da299429547a7aca9597f027979a8', '2153492e4d6c4794a58bbaa5d9ab07e2', 'd35335d931554f27b024f21bd76f5fce', '', 'Next', 'admin', '2016-08-05 13:22:40', NULL, NULL),
	('6456811a421e40b4b75745e2b07d9e56', '987da299429547a7aca9597f027979a8', 'dfb4aceb931b416595d63cf0ec474b4a', '2153492e4d6c4794a58bbaa5d9ab07e2', '', 'Next', 'admin', '2016-08-05 13:22:40', NULL, NULL),
	('798af5ae04f34e2986c314aef00fb5c3', 'bf28e46d610c407a91f7d80c4d776510', 'cd4ab1052bfa4616bf84d1aee72e4316', '1b0db364b43d4656b42a1aef14f60b5c', '', 'Next', 'admin', '2016-08-05 15:04:32', NULL, NULL),
	('886c46f41c8946f59729496114b7be63', '616d85e4d2534969a56b7dce897e82ab', 'b53bffcd844042d9846d117c6b49a8d5', '58f904f8f7954b598649352fe31bbdc6', '', 'Next', 'admin', '2016-08-05 13:37:15', NULL, NULL),
	('a5c2b88368c94c0d8559104eff3610c3', '616d85e4d2534969a56b7dce897e82ab', '9be280cda3a84f218f029f44172d8047', 'b53bffcd844042d9846d117c6b49a8d5', '', 'Next', 'admin', '2016-08-05 13:37:15', NULL, NULL),
	('a70dfa50bf4545be9454666e30bde33b', '99fef0712cb84a178af69dcd1109e335', 'abc1d439d5254d3d8be4e3f0a3d4bdf1', '7716494f34cd42f88a219b54587459b8', '', 'Next', 'admin', '2016-08-10 09:49:15', NULL, NULL),
	('f91efc2a57374c928c5342d25c63cb16', '99fef0712cb84a178af69dcd1109e335', '2a6af2700bd34310b35159b5c580120d', 'abc1d439d5254d3d8be4e3f0a3d4bdf1', '', 'Next', 'admin', '2016-08-10 09:49:15', NULL, NULL),
	('fd688437f1ca49029f9fc4a755fe48b5', 'bf28e46d610c407a91f7d80c4d776510', 'f4bdef07ad8b4839bf1588ce16bc9fee', 'bde6c424ab4840e5a947473d3a6d9546', '', 'Next', 'admin', '2016-08-05 15:04:32', NULL, NULL);
/*!40000 ALTER TABLE `wf_task_conn` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
