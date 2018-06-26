/*
MySQL Data Transfer
Source Host: localhost
Source Database: aihudong-duoping
Target Host: localhost
Target Database: aihudong-duoping
Date: 2018/6/25 14:52:47
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for admin
-- ----------------------------
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `truename` varchar(255) DEFAULT NULL,
  `sex` int(11) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `power` int(11) DEFAULT NULL,
  `higher_id` int(11) DEFAULT NULL,
  `screen_num` int(11) DEFAULT NULL,
  `screen_remain` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for building
-- ----------------------------
CREATE TABLE `building` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `building_name` varchar(255) DEFAULT NULL,
  `admin_id` int(11) DEFAULT NULL,
  `zone_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for faculty
-- ----------------------------
CREATE TABLE `faculty` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `faculty_name` varchar(255) DEFAULT NULL,
  `admin_id` int(11) DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for file_record
-- ----------------------------
CREATE TABLE `file_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `absolute_path` varchar(255) DEFAULT NULL,
  `room_id` varchar(255) DEFAULT NULL,
  `upload_time` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for group
-- ----------------------------
CREATE TABLE `group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for license
-- ----------------------------
CREATE TABLE `license` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `remaining_time` varchar(255) DEFAULT NULL,
  `screen_num` int(11) DEFAULT NULL,
  `people_num` int(11) DEFAULT NULL,
  `screen_num_sametime` int(11) DEFAULT NULL,
  `license_id` varchar(255) DEFAULT NULL,
  `license_size` varchar(255) DEFAULT NULL,
  `license_type` varchar(255) DEFAULT NULL,
  `is_current` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for record
-- ----------------------------
CREATE TABLE `record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) DEFAULT NULL,
  `screen_id` varchar(255) DEFAULT NULL,
  `role` int(11) DEFAULT NULL,
  `system_id` int(11) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for room
-- ----------------------------
CREATE TABLE `room` (
  `id` varchar(255) NOT NULL,
  `num` varchar(255) DEFAULT NULL,
  `building_id` int(11) DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for screen
-- ----------------------------
CREATE TABLE `screen` (
  `id` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `room_id` varchar(255) DEFAULT NULL,
  `admin_id` int(11) DEFAULT NULL,
  `duration` varchar(255) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `resolution` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for student
-- ----------------------------
CREATE TABLE `student` (
  `id` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `truename` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `sex` int(11) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `subject_id` int(11) DEFAULT NULL,
  `duration` varchar(255) DEFAULT NULL,
  `time` int(11) DEFAULT NULL,
  `remake` varchar(255) DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for subject
-- ----------------------------
CREATE TABLE `subject` (
  `id` int(11) NOT NULL,
  `subject_name` varchar(255) DEFAULT NULL,
  `faculty_id` int(11) DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system
-- ----------------------------
CREATE TABLE `system` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `osver` varchar(255) DEFAULT NULL,
  `cpu` varchar(255) DEFAULT NULL,
  `memory` varchar(255) DEFAULT NULL,
  `graphicscard` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
CREATE TABLE `teacher` (
  `id` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `openid` varchar(255) DEFAULT NULL,
  `truename` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `sex` int(11) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `job` varchar(255) DEFAULT NULL,
  `subject_id` int(11) DEFAULT NULL,
  `screen_num_sametime` int(11) DEFAULT NULL,
  `duration` varchar(255) DEFAULT NULL,
  `times` int(11) DEFAULT NULL,
  `remake` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for zone
-- ----------------------------
CREATE TABLE `zone` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `zone_name` varchar(255) DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `admin` VALUES ('1', 'genji', '123', '杰克', '0', '13525846234', '123@qq.com', '0', null, '1000', '1147');
INSERT INTO `admin` VALUES ('2', 'caozuo', '123', '李四', '0', '17654784752', '789@qq.com', '2', '3', '500', '488');
INSERT INTO `admin` VALUES ('3', 'yiji', '123', '明月', '1', '18846287456', '456@qq.com', '1', '1', '1000', '500');
INSERT INTO `admin` VALUES ('4', 'jin', 'jin', '金金', '0', '15784562123', '152@163.com', '1', '1', '600', '499');
INSERT INTO `admin` VALUES ('5', 'caozuo', '1234', '水淼', '1', '15474865544', '152@163.com', '2', '4', '101', '101');
INSERT INTO `admin` VALUES ('13', '111', '11', '111', '0', '111', '111', '1', '1', '0', '0');
INSERT INTO `admin` VALUES ('14', 'fff', 'fff', 'ff', '0', 'ff', 'ff', '1', '1', '0', '0');
INSERT INTO `building` VALUES ('1', '学知楼', '2', '1');
INSERT INTO `building` VALUES ('2', '聚贤楼', '0', '1');
INSERT INTO `building` VALUES ('3', '实验楼', null, '1');
INSERT INTO `building` VALUES ('6', '啊啊', null, '2');
INSERT INTO `building` VALUES ('7', '1111', null, '1');
INSERT INTO `building` VALUES ('8', 'ABC', null, '3');
INSERT INTO `faculty` VALUES ('1', '数学系', '3', null);
INSERT INTO `faculty` VALUES ('2', '化工系', '4', null);
INSERT INTO `faculty` VALUES ('3', '电子信息系', '3', null);
INSERT INTO `faculty` VALUES ('4', '教育系', '4', null);
INSERT INTO `group` VALUES ('1', '1组', null);
INSERT INTO `license` VALUES ('1', '1年', '300', '5000', '200', 'v1.0', '45M', '测试', '1');
INSERT INTO `license` VALUES ('2', '3年', '400', '10000', '400', 'v1.1', '50M', '正版', '0');
INSERT INTO `record` VALUES ('1', 'te10', 'sc9', '1', null, '2018-03-21 16:36:33', '2018-03-21 16:38:56');
INSERT INTO `record` VALUES ('2', 'sc9', null, '4', null, '2018-03-21 16:37:39', '2018-03-21 16:38:55');
INSERT INTO `record` VALUES ('3', 'te10', 'sc9', '1', null, '2018-03-21 16:39:18', '2018-03-21 16:39:30');
INSERT INTO `record` VALUES ('9', 'sc5', null, '4', null, '2018-03-28 10:47:53', '2018-03-28 10:48:21');
INSERT INTO `record` VALUES ('11', 'sc5', null, '4', null, '2018-03-28 11:36:26', '2018-03-28 11:36:51');
INSERT INTO `record` VALUES ('33', 'te18', 'sc1', '1', null, '2018-03-28 15:35:26', '2018-03-28 15:35:52');
INSERT INTO `record` VALUES ('35', 'te18', 'sc4', '1', null, '2018-03-28 15:41:40', '2018-03-28 15:42:27');
INSERT INTO `record` VALUES ('36', 'te18', 'sc4', '1', null, '2018-03-28 15:45:22', '2018-03-28 15:46:37');
INSERT INTO `record` VALUES ('37', 'te18', null, '1', null, '2018-03-28 15:49:19', '2018-03-28 15:49:56');
INSERT INTO `record` VALUES ('38', 'te18', 'sc5', '1', null, '2018-03-28 15:50:06', '2018-03-28 15:50:26');
INSERT INTO `record` VALUES ('39', 'te18', 'sc5', '1', null, '2018-03-28 15:53:24', '2018-03-28 15:54:15');
INSERT INTO `record` VALUES ('40', 'te18', 'sc5', '1', null, '2018-03-28 15:56:26', '2018-03-28 15:56:57');
INSERT INTO `record` VALUES ('41', 'te18', 'sc4', '1', null, '2018-03-28 16:05:38', '2018-03-28 16:06:49');
INSERT INTO `record` VALUES ('42', 'te18', 'sc5', '1', null, '2018-03-28 16:08:23', '2018-03-28 16:09:25');
INSERT INTO `record` VALUES ('43', 'te18', 'sc4', '1', null, '2018-03-28 16:10:32', '2018-03-28 16:12:08');
INSERT INTO `record` VALUES ('44', 'te18', 'sc3', '1', null, '2018-03-28 16:17:18', '2018-03-28 16:19:25');
INSERT INTO `record` VALUES ('45', 'te18', 'sc8', '1', null, '2018-03-28 16:20:56', '2018-03-28 16:24:08');
INSERT INTO `record` VALUES ('46', 'te18', 'sc9', '1', null, '2018-03-28 16:25:06', '2018-03-28 16:25:52');
INSERT INTO `record` VALUES ('47', 'te18', 'sc9', '1', null, '2018-03-28 16:34:32', '2018-03-28 16:35:09');
INSERT INTO `record` VALUES ('48', 'te18', 'sc9', '1', null, '2018-03-28 16:37:22', '2018-03-28 16:38:03');
INSERT INTO `record` VALUES ('49', 'te18', 'sc10', '1', null, '2018-03-28 16:40:03', '2018-03-28 16:40:45');
INSERT INTO `record` VALUES ('50', 'te18', 'sc10', '1', null, '2018-03-28 16:43:44', '2018-03-28 16:44:20');
INSERT INTO `record` VALUES ('51', 'te18', 'sc10', '1', null, '2018-03-28 16:46:32', '2018-03-28 16:47:33');
INSERT INTO `record` VALUES ('52', 'te18', 'sc10', '1', null, '2018-03-28 16:49:43', '2018-03-28 16:50:35');
INSERT INTO `record` VALUES ('53', 'te18', 'sc12', '1', null, '2018-03-28 17:13:40', '2018-03-28 17:15:05');
INSERT INTO `record` VALUES ('54', 'te18', 'sc10', '1', null, '2018-03-28 17:35:21', '2018-03-28 17:38:51');
INSERT INTO `record` VALUES ('55', 'te18', 'sc10', '1', null, '2018-03-29 09:03:27', '2018-03-29 09:15:24');
INSERT INTO `record` VALUES ('56', 'te6', 'sc10', '1', null, '2018-03-29 10:11:17', '2018-03-29 10:16:30');
INSERT INTO `record` VALUES ('57', 'te6', 'sc10', '1', null, '2018-03-29 10:18:02', '2018-03-29 10:18:41');
INSERT INTO `record` VALUES ('58', 'te6', 'sc10', '1', null, '2018-03-29 10:35:15', '2018-03-29 10:37:54');
INSERT INTO `record` VALUES ('59', 'te6', 'sc10', '1', null, '2018-03-29 11:16:54', '2018-03-29 11:19:57');
INSERT INTO `record` VALUES ('60', 'te6', 'sc10', '1', null, '2018-03-29 11:41:14', '2018-03-29 11:47:03');
INSERT INTO `record` VALUES ('62', 'te6', 'sc10', '1', null, '2018-03-29 14:19:35', '2018-03-29 14:20:15');
INSERT INTO `record` VALUES ('63', 'te6', 'sc10', '1', null, '2018-03-29 14:21:07', '2018-03-29 14:22:09');
INSERT INTO `record` VALUES ('64', 'te6', 'sc12', '1', null, '2018-03-29 14:23:04', '2018-03-29 14:27:00');
INSERT INTO `record` VALUES ('65', 'te6', 'sc10', '1', null, '2018-03-29 14:29:55', '2018-03-29 14:33:54');
INSERT INTO `record` VALUES ('66', 'te6', 'sc10', '1', null, '2018-03-29 14:34:26', '2018-03-29 15:11:55');
INSERT INTO `record` VALUES ('67', 'te6', 'sc10', '1', null, '2018-03-29 15:13:12', '2018-03-29 15:23:36');
INSERT INTO `record` VALUES ('68', 'te6', 'sc10', '1', null, '2018-03-29 15:23:56', '2018-03-29 15:46:53');
INSERT INTO `record` VALUES ('69', 'te6', 'sc10', '1', null, '2018-03-29 15:47:14', '2018-03-29 15:47:42');
INSERT INTO `record` VALUES ('70', 'te6', 'sc10', '1', null, '2018-03-29 15:47:58', '2018-03-29 16:31:23');
INSERT INTO `record` VALUES ('71', 'te6', 'sc10', '1', null, '2018-03-29 16:37:10', '2018-03-29 16:48:12');
INSERT INTO `record` VALUES ('72', 'te6', 'sc10', '1', null, '2018-03-29 17:05:38', '2018-03-29 17:32:20');
INSERT INTO `record` VALUES ('73', 'te6', 'sc10', '1', null, '2018-03-29 17:32:58', '2018-03-29 17:34:49');
INSERT INTO `record` VALUES ('75', 'te6', 'sc10', '1', null, '2018-03-29 17:46:38', '2018-03-29 18:05:10');
INSERT INTO `record` VALUES ('76', 'te6', 'sc10', '1', null, '2018-03-29 18:06:14', '2018-03-29 18:10:33');
INSERT INTO `record` VALUES ('77', 'te18', 'sc5', '1', null, '2018-03-29 18:07:16', '2018-03-29 18:08:55');
INSERT INTO `record` VALUES ('78', 'te18', 'sc5', '1', null, '2018-03-29 18:09:13', '2018-03-29 18:10:01');
INSERT INTO `record` VALUES ('79', 'te18', 'sc5', '1', null, '2018-03-29 18:10:17', '2018-03-29 18:10:30');
INSERT INTO `record` VALUES ('80', 'te18', 'sc5', '1', null, '2018-03-29 18:12:09', '2018-03-29 18:12:16');
INSERT INTO `record` VALUES ('81', 'te18', 'sc5', '1', null, '2018-03-29 18:14:45', '2018-03-29 18:15:46');
INSERT INTO `record` VALUES ('82', 'te18', 'sc5', '1', null, '2018-03-29 18:17:22', '2018-03-29 18:18:43');
INSERT INTO `record` VALUES ('84', 'te18', 'sc5', '1', null, '2018-03-29 18:21:35', '2018-03-29 18:21:58');
INSERT INTO `record` VALUES ('85', 'te18', 'sc5', '1', null, '2018-03-29 18:22:28', '2018-03-29 18:22:45');
INSERT INTO `record` VALUES ('86', 'te18', 'sc5', '1', null, '2018-03-29 18:25:47', '2018-03-29 18:27:38');
INSERT INTO `record` VALUES ('87', 'te18', 'sc5', '1', null, '2018-03-29 18:28:32', '2018-03-29 18:29:33');
INSERT INTO `record` VALUES ('89', 'te18', 'sc5', '1', null, '2018-03-29 18:30:01', '2018-03-29 18:31:54');
INSERT INTO `record` VALUES ('90', 'te18', 'sc5', '1', null, '2018-03-29 18:32:40', '2018-03-29 18:38:40');
INSERT INTO `record` VALUES ('91', 'te6', 'sc10', '1', null, '2018-03-30 10:03:17', '2018-03-30 10:03:59');
INSERT INTO `record` VALUES ('92', 'te6', 'sc10', '1', null, '2018-03-30 10:05:08', '2018-03-30 10:27:30');
INSERT INTO `record` VALUES ('93', 'te6', 'sc10', '1', null, '2018-03-30 10:34:04', '2018-03-30 10:34:40');
INSERT INTO `record` VALUES ('94', 'te6', 'sc10', '1', null, '2018-03-30 10:34:54', '2018-03-30 10:54:13');
INSERT INTO `record` VALUES ('95', 'te6', 'sc10', '1', null, '2018-03-30 10:57:09', '2018-03-30 11:06:58');
INSERT INTO `record` VALUES ('96', 'te6', 'sc10', '1', null, '2018-03-30 11:10:07', '2018-03-30 11:12:38');
INSERT INTO `record` VALUES ('97', 'te18', 'sc5', '1', null, '2018-03-30 11:14:33', '2018-03-30 11:15:48');
INSERT INTO `record` VALUES ('99', 'te18', 'sc5', '1', null, '2018-03-30 12:07:42', '2018-03-30 12:42:34');
INSERT INTO `record` VALUES ('100', 'te18', 'sc5', '1', null, '2018-03-30 12:47:52', '2018-03-30 13:30:27');
INSERT INTO `record` VALUES ('102', 'te18', 'sc5', '1', null, '2018-04-02 10:36:28', '2018-04-02 10:41:04');
INSERT INTO `record` VALUES ('103', 'te6', 'sc10', '1', null, '2018-04-02 10:37:08', '2018-04-02 10:57:19');
INSERT INTO `record` VALUES ('104', 'te18', null, '1', null, '2018-04-30 15:45:55', null);
INSERT INTO `record` VALUES ('105', 'te18', null, '1', null, '2018-04-30 15:48:23', null);
INSERT INTO `record` VALUES ('106', 'te18', null, '1', null, '2018-04-30 16:00:13', null);
INSERT INTO `record` VALUES ('107', 'te18', null, '1', null, '2018-04-30 16:11:54', null);
INSERT INTO `record` VALUES ('108', 'te18', null, '1', null, '2018-04-30 16:15:47', null);
INSERT INTO `record` VALUES ('109', 'te18', null, '1', null, '2018-04-30 16:16:23', null);
INSERT INTO `record` VALUES ('110', 'te18', null, '1', null, '2018-04-30 16:19:02', null);
INSERT INTO `record` VALUES ('111', 'te18', null, '1', null, '2018-04-30 16:19:56', null);
INSERT INTO `record` VALUES ('112', 'te18', null, '1', null, '2018-04-30 16:22:28', null);
INSERT INTO `record` VALUES ('113', 'te18', null, '1', null, '2018-04-30 16:27:19', null);
INSERT INTO `record` VALUES ('114', 'te10', null, '1', null, '2018-04-30 16:31:16', null);
INSERT INTO `room` VALUES ('1001407', 'abc', '7', null);
INSERT INTO `room` VALUES ('783595', '784', '2', null);
INSERT INTO `room` VALUES ('783618', '155', '2', null);
INSERT INTO `room` VALUES ('783726', '456', '3', null);
INSERT INTO `room` VALUES ('783762', '78412', '3', null);
INSERT INTO `room` VALUES ('790650', '444', '7', null);
INSERT INTO `room` VALUES ('840001', '7777', '6', null);
INSERT INTO `room` VALUES ('840032', '777', '8', null);
INSERT INTO `screen` VALUES ('sc1', '0001', '123', '783595', '1', '00:00:00', '0', '2', null);
INSERT INTO `screen` VALUES ('sc10', '0010', '123', '1001407', '1', '00:00:00', '0', '2', null);
INSERT INTO `screen` VALUES ('sc11', '0011', '123', '1001407', '1', '00:00:00', '0', '2', null);
INSERT INTO `screen` VALUES ('sc12', '0012', '123', '783726', '1', '00:00:00', '0', '1', null);
INSERT INTO `screen` VALUES ('sc13', '0013', '123', '840032', '1', '00:00:00', '0', '1', null);
INSERT INTO `screen` VALUES ('sc14', '0014', '123', '840032', '1', '00:00:00', '0', '1', null);
INSERT INTO `screen` VALUES ('sc15', '0015', '123', '840032', '1', '00:00:00', '0', '1', null);
INSERT INTO `screen` VALUES ('sc16', '0016', '123', '840032', '1', '00:00:00', '0', '1', null);
INSERT INTO `screen` VALUES ('sc2', '0002', '123', '783726', '1', '00:00:00', '0', '1', null);
INSERT INTO `screen` VALUES ('sc3', '0003', '123', '783595', '1', '00:00:00', '0', '1', null);
INSERT INTO `screen` VALUES ('sc4', '0004', '123', '783595', '1', '00:00:00', '0', '1', null);
INSERT INTO `screen` VALUES ('sc5', '0005', '123', '783726', '1', '0:0:52', '2', '1', null);
INSERT INTO `screen` VALUES ('sc6', '0006', '123', '783726', '1', '00:00:00', '0', '1', null);
INSERT INTO `screen` VALUES ('sc7', '0007', '123', '783726', '1', '00:00:00', '0', '1', null);
INSERT INTO `screen` VALUES ('sc8', '0008', '123', '783726', '1', '00:00:00', '0', '1', null);
INSERT INTO `screen` VALUES ('sc9', '0009', '123', '1001407', '1', '0:1:15', '1', '2', null);
INSERT INTO `student` VALUES ('st1', '111', '11', '111', null, '1', '111', '111', '4', null, '0', null, null);
INSERT INTO `student` VALUES ('st2', 'username1', 'password', 'truename', null, '1', 'telephone', 'email', '1', '00:00:00', '0', 'remake', null);
INSERT INTO `student` VALUES ('st3', 'username2', 'password', 'truename', null, '1', 'telephone', 'email', '1', '00:00:00', '0', 'remake', null);
INSERT INTO `student` VALUES ('st4', 'username3', 'password', 'truename', null, '1', 'telephone', 'email', '1', '00:00:00', '0', 'remake', null);
INSERT INTO `subject` VALUES ('1', '数学与应用数学', '1', null);
INSERT INTO `subject` VALUES ('2', '计算机科学与技术', '1', null);
INSERT INTO `subject` VALUES ('3', '应用化学', '2', null);
INSERT INTO `subject` VALUES ('4', '高分子材料', '2', null);
INSERT INTO `subject` VALUES ('5', '物理专业', '3', null);
INSERT INTO `subject` VALUES ('6', '学前教育', '4', null);
INSERT INTO `subject` VALUES ('7', '心理学专业', '4', null);
INSERT INTO `subject` VALUES ('8', '电子信息工程', '3', null);
INSERT INTO `subject` VALUES ('9', '数字媒体专业', '3', null);
INSERT INTO `teacher` VALUES ('te1', '1', '123', null, '123', null, '0', '123', '123', '1', '2', '0', '00:00:00', '0', null);
INSERT INTO `teacher` VALUES ('te10', '1234', '1234', null, '王守忠', null, '0', '13845721684', '123@qq.com', '教授', '1', '100', '0:8:43', '3', null);
INSERT INTO `teacher` VALUES ('te11', '3', '3', null, '3', null, '0', '3', '3', '3', '4', '0', '00:00:00', '0', null);
INSERT INTO `teacher` VALUES ('te12', 'username', 'password', null, 'truename', null, '1', 'telephone', 'email', 'job', '1', '0', '00:00:00', '0', 'remake');
INSERT INTO `teacher` VALUES ('te13', 'username', 'password', null, 'truename', null, '1', 'telephone', 'email', 'job', '1', '0', '00:00:00', '0', 'remake');
INSERT INTO `teacher` VALUES ('te14', '1234123123', '12321321', null, '123213', null, '0', '123213', '1321321', '123213', '1', '0', '00:00:00', '0', null);
INSERT INTO `teacher` VALUES ('te15', '2132321', '123213', null, '213213', null, '0', '1321', '132', '132', '1', '0', '00:00:00', '0', null);
INSERT INTO `teacher` VALUES ('te16', '31232', '1321', null, '213', null, '0', '123', '123', '132', '1', '0', '2:5:12', '0', null);
INSERT INTO `teacher` VALUES ('te18', 'zyl', '111', null, '张勇良', null, '0', null, null, null, '1', '100', '2:15:37', '38', null);
INSERT INTO `teacher` VALUES ('te2', '5678', '5678', null, '孙素华', null, '1', '15675458478', '123@qq.com', '助教', '1', '100', '00:00:00', '0', null);
INSERT INTO `teacher` VALUES ('te3', 'a123', 'a123', null, '杨金山', null, '0', '18254568741', '456@qq.com', '讲师', '2', '100', '00:00:00', '0', null);
INSERT INTO `teacher` VALUES ('te4', 'f123', 'f123', null, '冯俊丽', null, '1', '18354796547', '789652@qq.com', '讲师', '6', '0', '00:00:00', '0', null);
INSERT INTO `teacher` VALUES ('te5', 'i789', 'i789', null, '李亚辉', null, '1', '15587462541', '451@qq.com', '讲师', '8', '0', '00:00:00', '0', null);
INSERT INTO `teacher` VALUES ('te6', 's123', 's123', null, '宋博文', null, '0', '16532547852', '154@qq.com', '教授', '3', '0', '4:45:12', '26', null);
INSERT INTO `teacher` VALUES ('te7', 'l456', 'l456', null, '李启超', null, '1', '18465475412', '789@qq.com', '副教授', '3', '0', '00:00:00', '0', null);
INSERT INTO `teacher` VALUES ('te8', 'ff789', 'ff789', null, '冯娜', null, '1', '15865475412', '157@163.com', '导员', '6', '0', '00:00:00', '0', null);
INSERT INTO `teacher` VALUES ('te9', '2', '2', null, '2', null, '0', '2', '2', '2', '3', '0', '00:00:00', '0', null);
INSERT INTO `zone` VALUES ('1', '西校区', null);
INSERT INTO `zone` VALUES ('2', '东校区', null);
INSERT INTO `zone` VALUES ('3', '北校区', null);
