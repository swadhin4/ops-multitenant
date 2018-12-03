-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.6.37-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for probmng30
DROP DATABASE IF EXISTS `probmng30`;
CREATE DATABASE IF NOT EXISTS `probmng30` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `probmng30`;

-- Dumping structure for table probmng30.pm_asset
DROP TABLE IF EXISTS `pm_asset`;
CREATE TABLE IF NOT EXISTS `pm_asset` (
  `asset_id` int(11) NOT NULL AUTO_INCREMENT,
  `asset_code` varchar(20) NOT NULL,
  `site_id` int(11) NOT NULL,
  `asset_name` varchar(50) DEFAULT NULL,
  `asset_desc` varchar(100) DEFAULT NULL,
  `model_number` varchar(20) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `content` varchar(50) DEFAULT NULL,
  `location_id` int(11) NOT NULL,
  `image_path` varchar(400) DEFAULT NULL,
  `document_path` varchar(400) DEFAULT NULL,
  `sp_id` int(11) DEFAULT NULL,
  `rsp_id` int(11) DEFAULT NULL,
  `sp_type` char(2) DEFAULT NULL,
  `date_commissioned` datetime DEFAULT NULL,
  `date_decomissioned` datetime DEFAULT NULL,
  `is_asset_electrical` char(3) DEFAULT NULL,
  `is_pw_sensor_attached` char(3) DEFAULT NULL,
  `pw_sensor_number` varchar(20) DEFAULT NULL,
  `subcategory1_id` int(11) DEFAULT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_date` datetime DEFAULT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `modified_by` varchar(50) DEFAULT NULL,
  `del_flag` int(11) DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`asset_id`),
  UNIQUE KEY `asset_code_site_id` (`asset_code`,`site_id`),
  KEY `sp_id` (`sp_id`),
  KEY `category_id` (`category_id`),
  KEY `location_id` (`location_id`),
  KEY `site_id` (`site_id`),
  KEY `FK_pm_asset_pm_asset_subcategory1` (`subcategory1_id`),
  KEY `FK_pm_asset_sp_registered` (`rsp_id`),
  CONSTRAINT `FK_pm_asset_pm_asset_subcategory1` FOREIGN KEY (`subcategory1_id`) REFERENCES `pm_asset_subcategory1` (`subcategory1_id`),
  CONSTRAINT `FK_pm_asset_pm_serviceprovider` FOREIGN KEY (`sp_id`) REFERENCES `pm_service_provider` (`sp_id`),
  CONSTRAINT `FK_pm_asset_sp_registered` FOREIGN KEY (`rsp_id`) REFERENCES `pm_sp_registered` (`sp_id`),
  CONSTRAINT `pm_asset_ibfk_3` FOREIGN KEY (`category_id`) REFERENCES `pm_asset_category` (`category_id`),
  CONSTRAINT `pm_asset_ibfk_4` FOREIGN KEY (`location_id`) REFERENCES `pm_asset_location` (`location_id`),
  CONSTRAINT `pm_asset_ibfk_5` FOREIGN KEY (`site_id`) REFERENCES `pm_site` (`site_id`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8;

-- Dumping data for table probmng30.pm_asset: ~40 rows (approximately)
/*!40000 ALTER TABLE `pm_asset` DISABLE KEYS */;
INSERT INTO `pm_asset` (`asset_id`, `asset_code`, `site_id`, `asset_name`, `asset_desc`, `model_number`, `category_id`, `content`, `location_id`, `image_path`, `document_path`, `sp_id`, `rsp_id`, `sp_type`, `date_commissioned`, `date_decomissioned`, `is_asset_electrical`, `is_pw_sensor_attached`, `pw_sensor_number`, `subcategory1_id`, `created_date`, `modified_date`, `created_by`, `modified_by`, `del_flag`, `version`) VALUES
	(40, 'DD', 73, 'D2h Remote', NULL, '1123', 7, 'Switches', 5, NULL, NULL, 17, NULL, NULL, '2017-10-04 00:00:00', '2017-10-20 00:00:00', 'YES', 'YES', '42342', 34, '2017-10-13 01:07:44', '2017-11-03 23:00:58', 'swadhin4@gmail.com', 'swadhin4@gmail.com', 0, 5),
	(41, 'MT', 73, 'ABC', 'fsdfsdf', 'fsd', 1, NULL, 2, 'GB01/asset/abc_1514746475664.jpg', NULL, 27, NULL, NULL, '2017-10-13 00:00:00', '2017-10-20 00:00:00', 'NO', 'NO', '', 1, '2017-10-14 19:12:15', '2018-06-21 21:49:14', 'swadhin4@gmail.com', 'swadhin4@gmail.com', 0, 27),
	(42, 'iii', 74, 'TestS', 'sdasda', NULL, 3, NULL, 4, NULL, NULL, 19, NULL, NULL, '2017-11-02 00:00:00', '2017-11-15 00:00:00', NULL, NULL, NULL, NULL, '2017-11-11 00:01:17', NULL, 'swadhin4@gmail.com', NULL, 0, 0),
	(43, 'MSC1', 77, 'MultipleAsset', 'NO', NULL, 2, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-11-08 00:00:00', '2017-11-17 00:00:00', 'NO', 'NO', '', 1, '2017-11-23 21:59:01', '2017-11-28 21:42:56', 'swadhin4@gmail.com', 'swadhin4@gmail.com', 0, 2),
	(44, 'jh', 74, 'jghj', NULL, NULL, 2, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-11-02 00:00:00', NULL, 'NO', 'NO', '', NULL, '2017-11-23 22:10:03', '2017-11-23 22:11:34', 'swadhin4@gmail.com', 'swadhin4@gmail.com', 0, 1),
	(45, 'MT', 77, 'MTest', NULL, NULL, 2, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-11-09 00:00:00', NULL, 'NO', 'NO', '', NULL, '2017-11-24 00:35:01', '2017-11-28 21:39:46', 'swadhin4@gmail.com', 'swadhin4@gmail.com', 0, 2),
	(46, 'ABCddd', 73, 'ABCTest1', NULL, NULL, 2, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-11-01 00:00:00', NULL, 'NO', 'NO', '', 6, '2017-11-28 21:49:22', '2018-06-10 02:08:02', 'swadhin4@gmail.com', 'swadhin4@gmail.com', 0, 4),
	(47, 'ABCddd1', 74, 'ABCTest1', NULL, NULL, 2, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-11-01 00:00:00', NULL, 'NO', 'NO', '', NULL, '2017-11-28 21:49:28', '2017-12-08 16:34:16', 'swadhin4@gmail.com', 'swadhin4@gmail.com', 1, 3),
	(51, 'ss2', 73, 'ss2', NULL, NULL, 3, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-12-01 00:00:00', '2017-12-14 00:00:00', NULL, NULL, NULL, NULL, '2017-12-08 01:09:48', NULL, 'swadhin4@gmail.com', NULL, 0, 0),
	(52, 'ss2', 78, 'ss2', NULL, NULL, 3, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-12-01 00:00:00', '2017-12-14 00:00:00', NULL, NULL, NULL, NULL, '2017-12-08 01:10:20', NULL, 'swadhin4@gmail.com', NULL, 0, 0),
	(53, 'dd1', 73, 'dd1', NULL, NULL, 3, NULL, 6, NULL, NULL, 17, NULL, NULL, '2017-12-01 00:00:00', '2017-12-14 00:00:00', NULL, NULL, NULL, NULL, '2017-12-08 12:11:20', '2017-12-08 12:26:14', 'swadhin4@gmail.com', 'swadhin4@gmail.com', 0, 1),
	(54, 'dd1', 75, 'dd1', NULL, NULL, 3, NULL, 6, NULL, NULL, 17, NULL, NULL, '2017-12-01 00:00:00', '2017-12-14 00:00:00', NULL, NULL, NULL, NULL, '2017-12-08 12:11:38', NULL, 'swadhin4@gmail.com', NULL, 0, 0),
	(56, 'ads', 75, 'aaew', 'dasdasd', NULL, 3, NULL, 6, NULL, NULL, 27, NULL, NULL, '2017-12-01 00:00:00', '2017-12-21 00:00:00', NULL, NULL, NULL, 11, '2017-12-08 12:54:23', '2018-06-21 21:25:46', 'swadhin4@gmail.com', 'swadhin4@gmail.com', 0, 15),
	(57, 'lll', 73, 'lll', NULL, NULL, 3, NULL, 4, NULL, NULL, 18, NULL, NULL, '2017-12-01 00:00:00', '2017-12-14 00:00:00', NULL, NULL, NULL, NULL, '2017-12-08 13:21:42', NULL, 'swadhin4@gmail.com', NULL, 0, 1),
	(58, 'lll', 75, 'lll', NULL, NULL, 3, NULL, 4, NULL, NULL, 18, NULL, NULL, '2017-12-01 00:00:00', '2017-12-14 00:00:00', NULL, NULL, NULL, NULL, '2017-12-08 13:21:42', NULL, 'swadhin4@gmail.com', NULL, 0, 1),
	(59, 'lll', 77, 'lll', NULL, NULL, 3, NULL, 4, NULL, NULL, 18, NULL, NULL, '2017-12-01 00:00:00', '2017-12-14 00:00:00', NULL, NULL, NULL, NULL, '2017-12-08 13:21:42', NULL, 'swadhin4@gmail.com', NULL, 0, 1),
	(60, 'lll', 78, 'lll', NULL, NULL, 3, NULL, 4, NULL, NULL, 18, NULL, NULL, '2017-12-01 00:00:00', '2017-12-14 00:00:00', NULL, NULL, NULL, NULL, '2017-12-08 13:21:42', NULL, 'swadhin4@gmail.com', NULL, 0, 1),
	(61, 'ASD', 77, 'TestEquipment1', NULL, NULL, 2, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-12-01 00:00:00', NULL, 'NO', 'NO', '', NULL, '2017-12-14 00:27:16', NULL, 'swadhin4@gmail.com', NULL, 0, 0),
	(62, 'ADE', 77, 'TestAssetUpload1', NULL, NULL, 2, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-12-02 00:00:00', NULL, 'NO', 'NO', '', NULL, '2017-12-14 00:35:41', NULL, 'swadhin4@gmail.com', NULL, 0, 1),
	(63, 'SER', 77, 'TestAssetService1', NULL, NULL, 3, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-12-08 00:00:00', NULL, NULL, NULL, NULL, NULL, '2017-12-14 00:41:32', NULL, 'swadhin4@gmail.com', NULL, 0, 1),
	(64, 'ADEd', 75, 'AssetCreate1', NULL, NULL, 2, NULL, 6, NULL, NULL, 17, NULL, NULL, '2017-12-08 00:00:00', NULL, 'NO', 'NO', '', NULL, '2017-12-14 00:44:31', '2017-12-14 00:45:40', 'swadhin4@gmail.com', 'swadhin4@gmail.com', 0, 2),
	(65, 'ADEd', 78, 'AssetCreate1', NULL, NULL, 2, NULL, 6, NULL, NULL, 17, NULL, NULL, '2017-12-08 00:00:00', NULL, 'NO', 'NO', '', NULL, '2017-12-14 00:44:31', NULL, 'swadhin4@gmail.com', NULL, 1, 1),
	(66, 'ADEd', 80, 'AssetCreate1', NULL, NULL, 2, NULL, 6, NULL, NULL, 17, NULL, NULL, '2017-12-08 00:00:00', NULL, 'NO', 'NO', '', NULL, '2017-12-14 00:44:31', NULL, 'swadhin4@gmail.com', NULL, 1, 1),
	(67, 'ADEd', 82, 'AssetCreate1', NULL, NULL, 2, NULL, 6, NULL, NULL, 17, NULL, NULL, '2017-12-08 00:00:00', NULL, 'NO', 'NO', '', NULL, '2017-12-14 00:44:31', NULL, 'swadhin4@gmail.com', NULL, 1, 1),
	(68, 'SDDE', 73, 'ServiceCreate1', NULL, NULL, 3, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-12-15 00:00:00', NULL, NULL, NULL, NULL, NULL, '2017-12-14 00:46:46', '2017-12-14 00:47:14', 'swadhin4@gmail.com', 'swadhin4@gmail.com', 0, 2),
	(69, 'SDDE', 74, 'ServiceCreate1', NULL, NULL, 3, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-12-15 00:00:00', NULL, NULL, NULL, NULL, NULL, '2017-12-14 00:46:46', NULL, 'swadhin4@gmail.com', NULL, 0, 0),
	(70, 'SDDE', 77, 'ServiceCreate1', NULL, NULL, 3, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-12-15 00:00:00', NULL, NULL, NULL, NULL, NULL, '2017-12-14 00:46:46', NULL, 'swadhin4@gmail.com', NULL, 0, 0),
	(71, 'AWEDF', 81, 'AssetFromIncident', NULL, NULL, 8, NULL, 6, NULL, NULL, 19, NULL, NULL, '2017-12-08 00:00:00', NULL, 'NO', 'NO', '', NULL, '2017-12-14 01:06:46', NULL, 'swadhin4@gmail.com', NULL, 0, 2),
	(72, 'AVGB', 81, 'AssetServiceIncident', NULL, NULL, 3, NULL, 4, NULL, NULL, 27, NULL, NULL, '2017-12-22 00:00:00', NULL, NULL, NULL, NULL, 13, '2017-12-14 01:18:50', '2018-06-10 14:12:37', 'swadhin4@gmail.com', 'swadhin4@gmail.com', 0, 4),
	(73, 'sdffs', 80, 'TestPDF', NULL, NULL, 3, NULL, 4, NULL, NULL, 18, NULL, NULL, '2017-12-08 00:00:00', NULL, NULL, NULL, NULL, NULL, '2017-12-14 21:05:11', NULL, 'swadhin4@gmail.com', NULL, 0, 1),
	(74, 'DFE', 73, 'ServiceTest1334', NULL, NULL, 3, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-12-09 00:00:00', NULL, NULL, NULL, NULL, NULL, '2017-12-14 21:45:08', NULL, 'swadhin4@gmail.com', NULL, 0, 0),
	(75, 'DFE', 74, 'ServiceTest1334', NULL, NULL, 3, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-12-09 00:00:00', NULL, NULL, NULL, NULL, NULL, '2017-12-14 21:45:08', NULL, 'swadhin4@gmail.com', NULL, 0, 0),
	(76, 'DFE', 75, 'ServiceTest1334', NULL, NULL, 3, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-12-09 00:00:00', NULL, NULL, NULL, NULL, NULL, '2017-12-14 21:45:08', NULL, 'swadhin4@gmail.com', NULL, 0, 0),
	(77, 'upserv55575', 77, 'service from site', 'test comment', NULL, 3, NULL, 4, NULL, NULL, 17, NULL, NULL, '2017-12-08 00:00:00', NULL, NULL, NULL, NULL, NULL, '2017-12-15 01:09:39', NULL, 'swadhin4@gmail.com', NULL, 0, 2),
	(78, 'HELO', 77, 'Hello', NULL, NULL, 8, NULL, 4, NULL, NULL, 19, NULL, NULL, '2017-12-08 00:00:00', NULL, 'NO', 'NO', '', NULL, '2017-12-17 01:10:17', '2017-12-17 01:12:26', 'swadhin4@gmail.com', 'swadhin4@gmail.com', 0, 2),
	(79, 'HELO', 79, 'Hello', NULL, NULL, 8, NULL, 4, NULL, NULL, 19, NULL, NULL, '2017-12-08 00:00:00', NULL, 'NO', 'NO', '', NULL, '2017-12-17 01:10:28', '2017-12-17 01:13:07', 'swadhin4@gmail.com', 'swadhin4@gmail.com', 0, 1),
	(80, 'jjffdsd', 79, 'hfghfhf', 'ghjgjhgjg', '5353453', 9, 'fsgdgdg', 6, NULL, NULL, 27, NULL, NULL, '2018-06-12 00:00:00', NULL, 'NO', 'NO', '', 40, '2018-06-10 15:09:51', NULL, 'swadhin4@gmail.com', NULL, 0, 0),
	(81, '4234re', 77, 'HHJJKKK', 'sdfsdf', 'fsdf32', 9, NULL, 4, NULL, NULL, 27, NULL, NULL, '2018-06-06 00:00:00', '2018-06-21 00:00:00', 'NO', 'NO', '', 39, '2018-06-24 01:53:37', NULL, 'swadhin4@gmail.com', NULL, 0, 0),
	(82, '4234re', 79, 'HHJJKKK', 'sdfsdf', 'fsdf32', 9, NULL, 4, NULL, NULL, 27, NULL, NULL, '2018-06-06 00:00:00', '2018-06-21 00:00:00', 'NO', 'NO', '', 39, '2018-06-24 01:53:37', NULL, 'swadhin4@gmail.com', NULL, 0, 0),
	(83, 'wwee', 77, 'Attachment Test', 'fsdfsf', 'rewr', 9, 'fsdfsf', 4, 'GB01/asset/attachment_test_1530379259474.png', NULL, 27, NULL, NULL, '2018-06-05 00:00:00', '2018-07-07 00:00:00', 'NO', 'NO', '', 39, '2018-06-30 22:50:59', NULL, 'swadhin4@gmail.com', NULL, 0, 1),
	(88, 'RAST002', 73, 'RSP Asset', 'Registered SP Asset', 'M2458', 1, NULL, 1, NULL, NULL, NULL, 1, 'R', '2018-11-30 23:29:19', '2018-11-30 22:00:00', 'NO', NULL, NULL, 41, '2018-11-30 23:30:32', NULL, NULL, NULL, 0, 0);
/*!40000 ALTER TABLE `pm_asset` ENABLE KEYS */;

-- Dumping structure for table probmng30.pm_registered_sp_sla
DROP TABLE IF EXISTS `pm_registered_sp_sla`;
CREATE TABLE IF NOT EXISTS `pm_registered_sp_sla` (
  `rsla_id` int(11) NOT NULL AUTO_INCREMENT,
  `rsp_id` int(11) NOT NULL,
  `priority_id` int(11) DEFAULT NULL,
  `duration` int(11) NOT NULL,
  `unit` varchar(6) NOT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `created_on` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`rsla_id`),
  KEY `FK_pm_registered_sp_sla_pm_sp_registered` (`rsp_id`),
  KEY `FK_pm_registered_sp_sla_pm_ct_priority_settings` (`priority_id`),
  CONSTRAINT `FK_pm_registered_sp_sla_pm_ct_priority_settings` FOREIGN KEY (`priority_id`) REFERENCES `pm_ct_priority_settings` (`priority_id`),
  CONSTRAINT `FK_pm_registered_sp_sla_pm_sp_registered` FOREIGN KEY (`rsp_id`) REFERENCES `pm_sp_registered` (`sp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Dumping data for table probmng30.pm_registered_sp_sla: ~4 rows (approximately)
/*!40000 ALTER TABLE `pm_registered_sp_sla` DISABLE KEYS */;
INSERT INTO `pm_registered_sp_sla` (`rsla_id`, `rsp_id`, `priority_id`, `duration`, `unit`, `created_by`, `created_on`) VALUES
	(1, 1, 1, 2, 'hour', NULL, '2018-11-30 23:07:52'),
	(2, 1, 2, 4, 'hour', NULL, '2018-11-30 23:08:14'),
	(3, 1, 3, 2, 'days', NULL, '2018-11-30 23:08:49'),
	(4, 1, 4, 4, 'days', NULL, '2018-11-30 23:09:44');
/*!40000 ALTER TABLE `pm_registered_sp_sla` ENABLE KEYS */;

-- Dumping structure for table probmng30.pm_rsp_escalation_levels
DROP TABLE IF EXISTS `pm_rsp_escalation_levels`;
CREATE TABLE IF NOT EXISTS `pm_rsp_escalation_levels` (
  `resc_id` int(11) NOT NULL AUTO_INCREMENT,
  `rsp_id` int(11) NOT NULL,
  `resc_level` int(11) NOT NULL,
  `resc_person` varchar(50) NOT NULL,
  `resc_email` varchar(50) NOT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `created_on` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`resc_id`),
  KEY `FK_pm_rsp_escalation_levels_pm_sp_registered` (`rsp_id`),
  CONSTRAINT `FK_pm_rsp_escalation_levels_pm_sp_registered` FOREIGN KEY (`rsp_id`) REFERENCES `pm_sp_registered` (`sp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table probmng30.pm_rsp_escalation_levels: ~0 rows (approximately)
/*!40000 ALTER TABLE `pm_rsp_escalation_levels` DISABLE KEYS */;
/*!40000 ALTER TABLE `pm_rsp_escalation_levels` ENABLE KEYS */;

-- Dumping structure for table probmng30.pm_sp_registered
DROP TABLE IF EXISTS `pm_sp_registered`;
CREATE TABLE IF NOT EXISTS `pm_sp_registered` (
  `sp_id` int(11) NOT NULL AUTO_INCREMENT,
  `sp_name` varchar(50) NOT NULL,
  `sp_code` varchar(50) DEFAULT NULL,
  `country_id` int(11) NOT NULL,
  `sp_desc` text,
  `sp_email` varchar(50) NOT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(50) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_by` varchar(50) DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `help_desk_number` bigint(20) DEFAULT NULL,
  `help_desk_email` varchar(45) NOT NULL,
  `sla_description` text,
  PRIMARY KEY (`sp_id`),
  UNIQUE KEY `sp_email` (`sp_email`),
  KEY `FK_pm_sp_registered_country_id` (`country_id`),
  CONSTRAINT `FK_pm_sp_registered_country_id` FOREIGN KEY (`country_id`) REFERENCES `pm_country` (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table probmng30.pm_sp_registered: ~0 rows (approximately)
/*!40000 ALTER TABLE `pm_sp_registered` DISABLE KEYS */;
INSERT INTO `pm_sp_registered` (`sp_id`, `sp_name`, `sp_code`, `country_id`, `sp_desc`, `sp_email`, `created_date`, `created_by`, `modified_date`, `modified_by`, `version`, `help_desk_number`, `help_desk_email`, `sla_description`) VALUES
	(1, 'Sample R Provider', 'SP01', 1, NULL, 'rsp1@gmail.com', '2018-12-02 14:25:12', NULL, NULL, NULL, 0, NULL, 'rsp_helpdesk@gmail.com', NULL);
/*!40000 ALTER TABLE `pm_sp_registered` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
