/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : aihudong_duoping

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 08/11/2018 18:04:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `truename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` int(11) NULL DEFAULT NULL,
  `telephone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `power` int(11) NULL DEFAULT NULL,
  `higher_id` int(11) NULL DEFAULT NULL,
  `screen_num` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'genji', '401ea2143ce0170af80e62520139d184', '杰克', 0, '13525846234', '123@qq.com', 0, NULL, 1000);
INSERT INTO `admin` VALUES (2, 'caozuo', '32e08f99348005a43701f1af05b81e6c', '李四', 0, '17654784752', '789@qq.com', 2, 3, 500);
INSERT INTO `admin` VALUES (3, 'yiji', '7261cde44dbc2fb8c85a5992495ab0f7', '明月', 1, '18846287456', '456@qq.com', 1, 1, 1000);
INSERT INTO `admin` VALUES (4, 'jin', 'f3563833f9611d7043fe8af7f0032382', '金金', 0, '15784562123', '152@163.com', 1, 1, 600);
INSERT INTO `admin` VALUES (13, '111', '2bca8546eca4b86893c313f6f6da9825', '111', 0, '111', '111', 1, 1, 0);
INSERT INTO `admin` VALUES (14, 'fff', '585625336500c412f2a6bd81a510678b', 'ff', 0, 'ff', 'ff', 1, 1, 0);

-- ----------------------------
-- Table structure for building
-- ----------------------------
DROP TABLE IF EXISTS `building`;
CREATE TABLE `building`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `building_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `admin_id` int(11) NULL DEFAULT NULL,
  `zone_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of building
-- ----------------------------
INSERT INTO `building` VALUES (1, '学知楼', 2, 1);
INSERT INTO `building` VALUES (2, '聚贤楼', 0, 1);
INSERT INTO `building` VALUES (3, '实验楼', NULL, 1);
INSERT INTO `building` VALUES (6, '啊啊', NULL, 2);
INSERT INTO `building` VALUES (7, '1111', NULL, 1);
INSERT INTO `building` VALUES (8, 'ABC', NULL, 3);

-- ----------------------------
-- Table structure for faculty
-- ----------------------------
DROP TABLE IF EXISTS `faculty`;
CREATE TABLE `faculty`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `faculty_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `admin_id` int(11) NULL DEFAULT NULL,
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of faculty
-- ----------------------------
INSERT INTO `faculty` VALUES (1, '数学系', 3, NULL);
INSERT INTO `faculty` VALUES (2, '化工系', 4, NULL);
INSERT INTO `faculty` VALUES (3, '电子信息系', 3, NULL);
INSERT INTO `faculty` VALUES (4, '教育系', 4, NULL);

-- ----------------------------
-- Table structure for file_record
-- ----------------------------
DROP TABLE IF EXISTS `file_record`;
CREATE TABLE `file_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `filename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `absolute_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `room_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upload_time` date NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group
-- ----------------------------
INSERT INTO `group` VALUES (1, '1组', NULL);

-- ----------------------------
-- Table structure for license
-- ----------------------------
DROP TABLE IF EXISTS `license`;
CREATE TABLE `license`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `remaining_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `screen_num` int(11) NULL DEFAULT NULL,
  `people_num` int(11) NULL DEFAULT NULL,
  `screen_num_sametime` int(11) NULL DEFAULT NULL,
  `license_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `license_size` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `license_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_current` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of license
-- ----------------------------
INSERT INTO `license` VALUES (1, '1年', 300, 5000, 200, 'v1.0', '45M', '测试', 1);
INSERT INTO `license` VALUES (2, '3年', 400, 10000, 400, 'v1.1', '50M', '正版', 0);

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `message_type` int(11) NULL DEFAULT NULL,
  `message_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `message_pic` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `admin_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `start_time` datetime(0) NULL DEFAULT NULL,
  `end_time` datetime(0) NULL DEFAULT NULL,
  `message_state` int(11) NULL DEFAULT NULL,
  `zone_id` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `building_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `room_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (1, '校庆通知', 1, NULL, NULL, '1', '2018-06-24 09:00:00', '2018-06-25 16:00:00', NULL, '1', '1', '783595');
INSERT INTO `message` VALUES (2, '放假通知', 1, NULL, '5b5a6706-f09d-4194-abee-487b69234c40.jpg,51f925e7-6f87-462d-bdbf-90c0eef49fa0.jpg,c5205e83-e7c8-4d14-98ed-79e46b3c8c09.jpg,7e5921cf-a4dd-47bf-85d3-a5269ce085ec.jpg,020deaa2-8d3a-4b15-b765-6c0093db78b3.jpg,2ed6066d-4acd-452c-918b-c3410ac08b6d.jpg,9f66db1c-ee8f-42e0-9f01-de340480a9c8.jpg,3eb91ec2-ec30-4d45-b9e3-61685a3f68d2.png', '1', '2018-06-29 10:25:58', '2018-07-20 21:00:59', NULL, '2', '9,23,15,16,17,18,19,20', '1001420,896141,896142,896143,896144,896145,1004826,888492,888496,899759,899760,899765,899766');
INSERT INTO `message` VALUES (4, 'aaaa', 2, NULL, 'd6721a3b-95c0-4770-aaac-51a317c9adcf.jpg,cd4ced04-a64d-418f-9566-275cbf695b60.jpg', '1', '2018-07-04 11:02:07', '2018-07-04 11:07:02', NULL, '1', '24,27,29,1,3,7', '1005157,1005832,1011366,783595,783618,783726,783762,876710,790650');
INSERT INTO `message` VALUES (5, '德国回家了', 2, NULL, '1.jpg,2.jpg,3.jpg,4.jpg,5.jpg', '1', '2018-07-07 02:05:00', '2018-08-16 22:00:00', NULL, '4', '8,3,2,28,30', '1010405,1013502,1014400');

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `screen_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role` int(11) NULL DEFAULT NULL,
  `system_id` int(11) NULL DEFAULT NULL,
  `start_time` datetime(0) NULL DEFAULT NULL,
  `end_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 168 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of record
-- ----------------------------
INSERT INTO `record` VALUES (1, 'te10', 'sc9', 1, NULL, '2018-03-21 16:36:33', '2018-03-21 16:38:56');
INSERT INTO `record` VALUES (2, 'sc9', NULL, 4, NULL, '2018-03-21 16:37:39', '2018-03-21 16:38:55');
INSERT INTO `record` VALUES (3, 'te10', 'sc9', 1, NULL, '2018-03-21 16:39:18', '2018-03-21 16:39:30');
INSERT INTO `record` VALUES (9, 'sc5', NULL, 4, NULL, '2018-03-28 10:47:53', '2018-03-28 10:48:21');
INSERT INTO `record` VALUES (11, 'sc5', NULL, 4, NULL, '2018-03-28 11:36:26', '2018-03-28 11:36:51');
INSERT INTO `record` VALUES (33, 'te18', 'sc1', 1, NULL, '2018-03-28 15:35:26', '2018-03-28 15:35:52');
INSERT INTO `record` VALUES (35, 'te18', 'sc4', 1, NULL, '2018-03-28 15:41:40', '2018-03-28 15:42:27');
INSERT INTO `record` VALUES (36, 'te18', 'sc4', 1, NULL, '2018-03-28 15:45:22', '2018-03-28 15:46:37');
INSERT INTO `record` VALUES (37, 'te18', NULL, 1, NULL, '2018-03-28 15:49:19', '2018-03-28 15:49:56');
INSERT INTO `record` VALUES (38, 'te18', 'sc5', 1, NULL, '2018-03-28 15:50:06', '2018-03-28 15:50:26');
INSERT INTO `record` VALUES (39, 'te18', 'sc5', 1, NULL, '2018-03-28 15:53:24', '2018-03-28 15:54:15');
INSERT INTO `record` VALUES (40, 'te18', 'sc5', 1, NULL, '2018-03-28 15:56:26', '2018-03-28 15:56:57');
INSERT INTO `record` VALUES (41, 'te18', 'sc4', 1, NULL, '2018-03-28 16:05:38', '2018-03-28 16:06:49');
INSERT INTO `record` VALUES (42, 'te18', 'sc5', 1, NULL, '2018-03-28 16:08:23', '2018-03-28 16:09:25');
INSERT INTO `record` VALUES (43, 'te18', 'sc4', 1, NULL, '2018-03-28 16:10:32', '2018-03-28 16:12:08');
INSERT INTO `record` VALUES (44, 'te18', 'sc3', 1, NULL, '2018-03-28 16:17:18', '2018-03-28 16:19:25');
INSERT INTO `record` VALUES (45, 'te18', 'sc8', 1, NULL, '2018-03-28 16:20:56', '2018-03-28 16:24:08');
INSERT INTO `record` VALUES (46, 'te18', 'sc9', 1, NULL, '2018-03-28 16:25:06', '2018-03-28 16:25:52');
INSERT INTO `record` VALUES (47, 'te18', 'sc9', 1, NULL, '2018-03-28 16:34:32', '2018-03-28 16:35:09');
INSERT INTO `record` VALUES (48, 'te18', 'sc9', 1, NULL, '2018-03-28 16:37:22', '2018-03-28 16:38:03');
INSERT INTO `record` VALUES (49, 'te18', 'sc10', 1, NULL, '2018-03-28 16:40:03', '2018-03-28 16:40:45');
INSERT INTO `record` VALUES (50, 'te18', 'sc10', 1, NULL, '2018-03-28 16:43:44', '2018-03-28 16:44:20');
INSERT INTO `record` VALUES (51, 'te18', 'sc10', 1, NULL, '2018-03-28 16:46:32', '2018-03-28 16:47:33');
INSERT INTO `record` VALUES (52, 'te18', 'sc10', 1, NULL, '2018-03-28 16:49:43', '2018-03-28 16:50:35');
INSERT INTO `record` VALUES (53, 'te18', 'sc12', 1, NULL, '2018-03-28 17:13:40', '2018-03-28 17:15:05');
INSERT INTO `record` VALUES (54, 'te18', 'sc10', 1, NULL, '2018-03-28 17:35:21', '2018-03-28 17:38:51');
INSERT INTO `record` VALUES (55, 'te18', 'sc10', 1, NULL, '2018-03-29 09:03:27', '2018-03-29 09:15:24');
INSERT INTO `record` VALUES (56, 'te6', 'sc10', 1, NULL, '2018-03-29 10:11:17', '2018-03-29 10:16:30');
INSERT INTO `record` VALUES (57, 'te6', 'sc10', 1, NULL, '2018-03-29 10:18:02', '2018-03-29 10:18:41');
INSERT INTO `record` VALUES (58, 'te6', 'sc10', 1, NULL, '2018-03-29 10:35:15', '2018-03-29 10:37:54');
INSERT INTO `record` VALUES (59, 'te6', 'sc10', 1, NULL, '2018-03-29 11:16:54', '2018-03-29 11:19:57');
INSERT INTO `record` VALUES (60, 'te6', 'sc10', 1, NULL, '2018-03-29 11:41:14', '2018-03-29 11:47:03');
INSERT INTO `record` VALUES (62, 'te6', 'sc10', 1, NULL, '2018-03-29 14:19:35', '2018-03-29 14:20:15');
INSERT INTO `record` VALUES (63, 'te6', 'sc10', 1, NULL, '2018-03-29 14:21:07', '2018-03-29 14:22:09');
INSERT INTO `record` VALUES (64, 'te6', 'sc12', 1, NULL, '2018-03-29 14:23:04', '2018-03-29 14:27:00');
INSERT INTO `record` VALUES (65, 'te6', 'sc10', 1, NULL, '2018-03-29 14:29:55', '2018-03-29 14:33:54');
INSERT INTO `record` VALUES (66, 'te6', 'sc10', 1, NULL, '2018-03-29 14:34:26', '2018-03-29 15:11:55');
INSERT INTO `record` VALUES (67, 'te6', 'sc10', 1, NULL, '2018-03-29 15:13:12', '2018-03-29 15:23:36');
INSERT INTO `record` VALUES (68, 'te6', 'sc10', 1, NULL, '2018-03-29 15:23:56', '2018-03-29 15:46:53');
INSERT INTO `record` VALUES (69, 'te6', 'sc10', 1, NULL, '2018-03-29 15:47:14', '2018-03-29 15:47:42');
INSERT INTO `record` VALUES (70, 'te6', 'sc10', 1, NULL, '2018-03-29 15:47:58', '2018-03-29 16:31:23');
INSERT INTO `record` VALUES (71, 'te6', 'sc10', 1, NULL, '2018-03-29 16:37:10', '2018-03-29 16:48:12');
INSERT INTO `record` VALUES (72, 'te6', 'sc10', 1, NULL, '2018-03-29 17:05:38', '2018-03-29 17:32:20');
INSERT INTO `record` VALUES (73, 'te6', 'sc10', 1, NULL, '2018-03-29 17:32:58', '2018-03-29 17:34:49');
INSERT INTO `record` VALUES (75, 'te6', 'sc10', 1, NULL, '2018-03-29 17:46:38', '2018-03-29 18:05:10');
INSERT INTO `record` VALUES (76, 'te6', 'sc10', 1, NULL, '2018-03-29 18:06:14', '2018-03-29 18:10:33');
INSERT INTO `record` VALUES (77, 'te18', 'sc5', 1, NULL, '2018-03-29 18:07:16', '2018-03-29 18:08:55');
INSERT INTO `record` VALUES (78, 'te18', 'sc5', 1, NULL, '2018-03-29 18:09:13', '2018-03-29 18:10:01');
INSERT INTO `record` VALUES (79, 'te18', 'sc5', 1, NULL, '2018-03-29 18:10:17', '2018-03-29 18:10:30');
INSERT INTO `record` VALUES (80, 'te18', 'sc5', 1, NULL, '2018-03-29 18:12:09', '2018-03-29 18:12:16');
INSERT INTO `record` VALUES (81, 'te18', 'sc5', 1, NULL, '2018-03-29 18:14:45', '2018-03-29 18:15:46');
INSERT INTO `record` VALUES (82, 'te18', 'sc5', 1, NULL, '2018-03-29 18:17:22', '2018-03-29 18:18:43');
INSERT INTO `record` VALUES (84, 'te18', 'sc5', 1, NULL, '2018-03-29 18:21:35', '2018-03-29 18:21:58');
INSERT INTO `record` VALUES (85, 'te18', 'sc5', 1, NULL, '2018-03-29 18:22:28', '2018-03-29 18:22:45');
INSERT INTO `record` VALUES (86, 'te18', 'sc5', 1, NULL, '2018-03-29 18:25:47', '2018-03-29 18:27:38');
INSERT INTO `record` VALUES (87, 'te18', 'sc5', 1, NULL, '2018-03-29 18:28:32', '2018-03-29 18:29:33');
INSERT INTO `record` VALUES (89, 'te18', 'sc5', 1, NULL, '2018-03-29 18:30:01', '2018-03-29 18:31:54');
INSERT INTO `record` VALUES (90, 'te18', 'sc5', 1, NULL, '2018-03-29 18:32:40', '2018-03-29 18:38:40');
INSERT INTO `record` VALUES (91, 'te6', 'sc10', 1, NULL, '2018-03-30 10:03:17', '2018-03-30 10:03:59');
INSERT INTO `record` VALUES (92, 'te6', 'sc10', 1, NULL, '2018-03-30 10:05:08', '2018-03-30 10:27:30');
INSERT INTO `record` VALUES (93, 'te6', 'sc10', 1, NULL, '2018-03-30 10:34:04', '2018-03-30 10:34:40');
INSERT INTO `record` VALUES (94, 'te6', 'sc10', 1, NULL, '2018-03-30 10:34:54', '2018-03-30 10:54:13');
INSERT INTO `record` VALUES (95, 'te6', 'sc10', 1, NULL, '2018-03-30 10:57:09', '2018-03-30 11:06:58');
INSERT INTO `record` VALUES (96, 'te6', 'sc10', 1, NULL, '2018-03-30 11:10:07', '2018-03-30 11:12:38');
INSERT INTO `record` VALUES (97, 'te18', 'sc5', 1, NULL, '2018-03-30 11:14:33', '2018-03-30 11:15:48');
INSERT INTO `record` VALUES (99, 'te18', 'sc5', 1, NULL, '2018-03-30 12:07:42', '2018-03-30 12:42:34');
INSERT INTO `record` VALUES (100, 'te18', 'sc5', 1, NULL, '2018-03-30 12:47:52', '2018-03-30 13:30:27');
INSERT INTO `record` VALUES (102, 'te18', 'sc5', 1, NULL, '2018-04-02 10:36:28', '2018-04-02 10:41:04');
INSERT INTO `record` VALUES (103, 'te6', 'sc10', 1, NULL, '2018-04-02 10:37:08', '2018-04-02 10:57:19');
INSERT INTO `record` VALUES (104, 'te18', NULL, 1, NULL, '2018-04-30 15:45:55', NULL);
INSERT INTO `record` VALUES (105, 'te18', NULL, 1, NULL, '2018-04-30 15:48:23', NULL);
INSERT INTO `record` VALUES (106, 'te18', NULL, 1, NULL, '2018-04-30 16:00:13', NULL);
INSERT INTO `record` VALUES (107, 'te18', NULL, 1, NULL, '2018-04-30 16:11:54', NULL);
INSERT INTO `record` VALUES (108, 'te18', NULL, 1, NULL, '2018-04-30 16:15:47', NULL);
INSERT INTO `record` VALUES (109, 'te18', NULL, 1, NULL, '2018-04-30 16:16:23', NULL);
INSERT INTO `record` VALUES (110, 'te18', NULL, 1, NULL, '2018-04-30 16:19:02', NULL);
INSERT INTO `record` VALUES (111, 'te18', NULL, 1, NULL, '2018-04-30 16:19:56', NULL);
INSERT INTO `record` VALUES (112, 'te18', NULL, 1, NULL, '2018-04-30 16:22:28', NULL);
INSERT INTO `record` VALUES (113, 'te18', NULL, 1, NULL, '2018-04-30 16:27:19', NULL);
INSERT INTO `record` VALUES (114, 'te10', NULL, 1, NULL, '2018-04-30 16:31:16', NULL);
INSERT INTO `record` VALUES (115, 'te18', 'sc1', 1, NULL, '2018-07-02 15:08:57', NULL);
INSERT INTO `record` VALUES (116, 'te18', NULL, 1, NULL, '2018-07-02 15:19:38', NULL);
INSERT INTO `record` VALUES (117, 'sc10', 'sc10', 4, NULL, '2018-07-02 15:19:59', NULL);
INSERT INTO `record` VALUES (118, 'sc10', NULL, 4, NULL, '2018-07-02 15:27:17', NULL);
INSERT INTO `record` VALUES (119, 'te18', 'sc10', 1, NULL, '2018-07-02 15:27:38', NULL);
INSERT INTO `record` VALUES (120, 'sc10', 'sc10', 4, NULL, '2018-07-02 15:28:22', NULL);
INSERT INTO `record` VALUES (121, 'te18', NULL, 1, NULL, '2018-07-26 16:30:21', NULL);
INSERT INTO `record` VALUES (122, 'sc10', NULL, 4, NULL, '2018-07-27 11:35:10', NULL);
INSERT INTO `record` VALUES (123, 'sc10', NULL, 4, NULL, '2018-07-27 11:40:07', NULL);
INSERT INTO `record` VALUES (124, 'sc10', NULL, 4, NULL, '2018-07-27 11:46:57', NULL);
INSERT INTO `record` VALUES (125, 'sc10', NULL, 4, NULL, '2018-07-27 11:47:35', NULL);
INSERT INTO `record` VALUES (126, 'te18', NULL, 1, NULL, '2018-07-27 11:47:52', NULL);
INSERT INTO `record` VALUES (127, 'te18', NULL, 1, NULL, '2018-07-30 10:48:09', NULL);
INSERT INTO `record` VALUES (128, 'sc2', NULL, 4, NULL, '2018-07-30 10:48:49', NULL);
INSERT INTO `record` VALUES (129, 'sc2', NULL, 4, NULL, '2018-07-30 10:53:31', NULL);
INSERT INTO `record` VALUES (130, 'te18', NULL, 1, NULL, '2018-07-30 10:54:20', NULL);
INSERT INTO `record` VALUES (131, 'st5', NULL, 2, NULL, '2018-07-30 10:54:29', NULL);
INSERT INTO `record` VALUES (132, 'te18', NULL, 1, NULL, '2018-08-06 09:24:43', NULL);
INSERT INTO `record` VALUES (133, 'te18', NULL, 1, NULL, '2018-08-06 09:53:54', NULL);
INSERT INTO `record` VALUES (134, 'sc9', NULL, 4, NULL, '2018-08-08 18:26:26', NULL);
INSERT INTO `record` VALUES (135, 'sc9', NULL, 4, NULL, '2018-08-09 18:38:41', NULL);
INSERT INTO `record` VALUES (136, 'sc9', NULL, 4, NULL, '2018-08-09 19:06:59', NULL);
INSERT INTO `record` VALUES (137, 'sc9', NULL, 4, NULL, '2018-08-09 19:23:08', NULL);
INSERT INTO `record` VALUES (138, 'sc9', NULL, 4, NULL, '2018-08-09 19:31:48', NULL);
INSERT INTO `record` VALUES (139, 'te18', NULL, 1, NULL, '2018-08-09 19:43:11', NULL);
INSERT INTO `record` VALUES (140, 'st5', NULL, 2, NULL, '2018-08-13 09:20:04', NULL);
INSERT INTO `record` VALUES (141, 'st5', NULL, 2, NULL, '2018-08-23 17:07:36', NULL);
INSERT INTO `record` VALUES (142, 'st5', NULL, 2, NULL, '2018-08-23 17:10:37', NULL);
INSERT INTO `record` VALUES (143, 'st5', NULL, 2, NULL, '2018-08-23 17:11:11', NULL);
INSERT INTO `record` VALUES (144, 'st5', NULL, 2, NULL, '2018-08-23 17:13:37', NULL);
INSERT INTO `record` VALUES (145, NULL, NULL, NULL, NULL, '2018-09-12 09:33:00', NULL);
INSERT INTO `record` VALUES (146, 'sc9', NULL, 4, NULL, '2018-09-12 09:39:12', NULL);
INSERT INTO `record` VALUES (147, 'sc9', NULL, 4, NULL, '2018-09-12 09:40:56', NULL);
INSERT INTO `record` VALUES (148, 'sc9', NULL, 4, NULL, '2018-09-12 09:42:24', NULL);
INSERT INTO `record` VALUES (149, 'sc8', NULL, 4, NULL, '2018-09-12 09:42:46', NULL);
INSERT INTO `record` VALUES (150, NULL, NULL, NULL, NULL, '2018-09-12 09:46:14', NULL);
INSERT INTO `record` VALUES (151, 'sc9', NULL, 4, NULL, '2018-09-12 09:49:58', NULL);
INSERT INTO `record` VALUES (152, 'st5', NULL, 2, NULL, '2018-09-20 15:57:10', NULL);
INSERT INTO `record` VALUES (153, 'st5', NULL, 2, NULL, '2018-09-20 15:58:10', '2018-09-20 15:58:49');
INSERT INTO `record` VALUES (154, 'te20', NULL, 1, NULL, '2018-09-20 16:04:55', '2018-09-20 16:05:17');
INSERT INTO `record` VALUES (155, 'te18', NULL, 1, NULL, '2018-09-20 16:08:42', '2018-09-20 16:08:59');
INSERT INTO `record` VALUES (156, 'te20', NULL, 1, NULL, '2018-09-20 16:14:25', '2018-09-20 16:14:40');
INSERT INTO `record` VALUES (157, 'te20', NULL, 1, NULL, '2018-09-20 16:17:39', '2018-09-20 16:17:47');
INSERT INTO `record` VALUES (158, 'te20', NULL, 1, NULL, '2018-09-20 16:19:21', '2018-09-20 16:19:35');
INSERT INTO `record` VALUES (159, 'st6', NULL, 2, NULL, '2018-10-19 09:58:45', NULL);
INSERT INTO `record` VALUES (160, 'st6', NULL, 2, NULL, '2018-11-02 11:55:45', NULL);
INSERT INTO `record` VALUES (161, 'st6', NULL, 2, NULL, '2018-11-02 11:58:53', NULL);
INSERT INTO `record` VALUES (162, 'st6', NULL, 2, NULL, '2018-11-05 17:22:26', NULL);
INSERT INTO `record` VALUES (163, 'sc2', NULL, 4, NULL, '2018-11-06 10:38:17', NULL);
INSERT INTO `record` VALUES (164, 'sc2', NULL, 4, NULL, '2018-11-06 10:39:03', NULL);
INSERT INTO `record` VALUES (165, 'sc2', NULL, 4, NULL, '2018-11-06 10:40:20', NULL);
INSERT INTO `record` VALUES (166, 'te20', NULL, 1, NULL, '2018-11-08 11:58:37', NULL);
INSERT INTO `record` VALUES (167, 'te18', NULL, 1, NULL, '2018-11-08 11:59:35', NULL);

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `building_id` int(11) NULL DEFAULT NULL,
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room
-- ----------------------------
INSERT INTO `room` VALUES ('1001407', 'kkk', NULL, 3, NULL, NULL);
INSERT INTO `room` VALUES ('1014400', 'abc', NULL, 7, NULL, NULL);
INSERT INTO `room` VALUES ('1793', '1793', NULL, 2, NULL, NULL);
INSERT INTO `room` VALUES ('4065', '4065', NULL, 2, NULL, NULL);
INSERT INTO `room` VALUES ('4733', '4733', NULL, 2, NULL, NULL);
INSERT INTO `room` VALUES ('666666', 'konka\'s', '123', NULL, NULL, 'te20');
INSERT INTO `room` VALUES ('7635', '7635', NULL, 2, NULL, NULL);
INSERT INTO `room` VALUES ('783595', '1', NULL, 2, NULL, NULL);
INSERT INTO `room` VALUES ('783618', '155', NULL, 2, NULL, NULL);
INSERT INTO `room` VALUES ('783726', '456', NULL, 3, NULL, NULL);
INSERT INTO `room` VALUES ('783762', '78412', NULL, 3, NULL, NULL);
INSERT INTO `room` VALUES ('790650', '444', NULL, 7, NULL, NULL);
INSERT INTO `room` VALUES ('8233', '8233', NULL, 2, NULL, NULL);
INSERT INTO `room` VALUES ('840001', '7777', NULL, 6, NULL, NULL);
INSERT INTO `room` VALUES ('840032', '777', NULL, 8, NULL, NULL);
INSERT INTO `room` VALUES ('9387', '9387', NULL, 2, NULL, NULL);
INSERT INTO `room` VALUES ('aaa123', 'xuni2', NULL, NULL, '呵呵', 'te20');

-- ----------------------------
-- Table structure for screen
-- ----------------------------
DROP TABLE IF EXISTS `screen`;
CREATE TABLE `screen`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `room_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `admin_id` int(11) NULL DEFAULT NULL,
  `duration` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `number` int(11) NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `resolution` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ip_addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mac_addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of screen
-- ----------------------------
INSERT INTO `screen` VALUES ('sc1', '0001', '6ccdbaf84ddba5eb72c3d1eb7bd4b8a8', '783595', 1, '00:00:00', 0, '2', NULL, NULL, '127.0.0.1', NULL);
INSERT INTO `screen` VALUES ('sc10', '0010', 'e227981f565a7f3a97efd10126797113', '1001407', 1, '00:00:00', 0, '2', NULL, 'S-1-5-21-2392787687-971683131-3325951360-1001', '127.0.0.1', NULL);
INSERT INTO `screen` VALUES ('sc11', '0011', '79a3ef9588b1c859b8ddee341548497e', '1001407', 1, '00:00:00', 0, '2', NULL, NULL, '127.0.0.1', NULL);
INSERT INTO `screen` VALUES ('sc12', '0012', '60c1e36c8561284a3bc379a8126663d4', '783726', 1, '00:00:00', 0, '1', NULL, NULL, '127.0.0.1', NULL);
INSERT INTO `screen` VALUES ('sc13', '0013', 'fa58b54aa048d693977387bf258a1820', '840032', 1, '00:00:00', 0, '1', NULL, NULL, '127.0.0.1', NULL);
INSERT INTO `screen` VALUES ('sc14', '0014', '548e55a10b7aef8496174c47b276c716', '840032', 1, '00:00:00', 0, '1', NULL, NULL, '127.0.0.1', NULL);
INSERT INTO `screen` VALUES ('sc15', '0015', '7111fc53f7a8f8ca8fe48b870a13b91f', '840032', 1, '00:00:00', 0, '1', NULL, NULL, '127.0.0.1', NULL);
INSERT INTO `screen` VALUES ('sc16', '0016', 'd7865a3cca903c133d19a9368fed8b93', '840032', 1, '00:00:00', 0, '1', NULL, NULL, '127.0.0.1', NULL);
INSERT INTO `screen` VALUES ('sc17', '0017', 'ea86bb6ddec9c18cd45e762d1d3495e9', '840032', 1, '00:00:00', 0, '1', NULL, NULL, '127.0.0.1', NULL);
INSERT INTO `screen` VALUES ('sc18', '0018', '78ade81873d9f00df31ba1b5159457ce', '840032', 1, '00:00:00', 0, '1', NULL, NULL, '127.0.0.1', NULL);
INSERT INTO `screen` VALUES ('sc19', '0019', '6558e2db77ca065b0aff417fe510e688', '4733', 1, '00:00:00', 0, '1', NULL, NULL, NULL, NULL);
INSERT INTO `screen` VALUES ('sc2', '0002', '53108afc8c39528d758f782d4048b850', '783726', 1, '00:00:00', 0, '1', NULL, 'asdf45841xxx', '0:0:0:0:0:0:0:1', NULL);
INSERT INTO `screen` VALUES ('sc20', '0020', 'a36b4ebcff0c775b745ea9d51a7b9e8b', '4733', 1, '00:00:00', 0, '1', NULL, NULL, NULL, NULL);
INSERT INTO `screen` VALUES ('sc21', '0021', 'fca8389204eb8adb0f966d6d9665a41c', '7635', 1, '00:00:00', 0, '1', NULL, NULL, NULL, NULL);
INSERT INTO `screen` VALUES ('sc22', '0022', '7d0de9b7fce039c13ec2302572dd74cb', '7635', 1, '00:00:00', 0, '1', NULL, NULL, NULL, NULL);
INSERT INTO `screen` VALUES ('sc3', '0003', 'eb521c182bd4cbbd0418a8063ca3cb9a', '783595', 1, '00:00:00', 0, '1', NULL, NULL, NULL, NULL);
INSERT INTO `screen` VALUES ('sc4', '0004', '1a2e4da9f470489c725fd7a3672876b3', '783595', 1, '00:00:00', 0, '1', NULL, NULL, NULL, NULL);
INSERT INTO `screen` VALUES ('sc5', '0005', 'd99d69b9634e869a1bb63069162fa130', '783726', 1, '0:0:52', 2, '1', NULL, NULL, NULL, NULL);
INSERT INTO `screen` VALUES ('sc6', '0006', 'ee335e659bb7a205c62584945e6db5a1', '783726', 1, '00:00:00', 0, '1', NULL, NULL, NULL, NULL);
INSERT INTO `screen` VALUES ('sc7', '0007', '1f15473510f235b3eebaa256793ba1de', '783726', 1, '00:00:00', 0, '1', NULL, NULL, NULL, NULL);
INSERT INTO `screen` VALUES ('sc8', '0008', 'f339352bc359111fab34eac7dc8133ab', '783726', 1, '00:00:00', 0, '1', NULL, '123', NULL, NULL);
INSERT INTO `screen` VALUES ('sc9', '0009', '95100c74eb80ee48bd68eefe21f9eb73', '1001407', 1, '0:1:15', 1, '2', NULL, '123', NULL, NULL);

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `open_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `truename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` int(11) NULL DEFAULT NULL,
  `telephone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `subject_id` int(11) NULL DEFAULT NULL,
  `duration` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` int(11) NULL DEFAULT NULL,
  `remake` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `group_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('st1', '111', '2bca8546eca4b86893c313f6f6da9825', NULL, '111', NULL, 1, '111', '111', 4, NULL, 0, NULL, NULL);
INSERT INTO `student` VALUES ('st2', 'username1', 'd9c6e732a439d6d3f2ebed17ed41f376', NULL, 'truename', NULL, 1, 'telephone', 'email', 1, '00:00:00', 0, 'remake', NULL);
INSERT INTO `student` VALUES ('st3', 'username2', '8f3c4bbccbcf6c448173e115c7936481', NULL, 'truename', NULL, 1, 'telephone', 'email', 1, '00:00:00', 0, 'remake', NULL);
INSERT INTO `student` VALUES ('st4', 'username3', 'a4dfdbd3e2514c9da254b1f7101c939b', NULL, 'truename', NULL, 1, 'telephone', 'email', 1, '00:00:00', 0, 'remake', NULL);
INSERT INTO `student` VALUES ('st5', 'konka4', '8c8f6f7423696a084ee8a5bd434af1d5', 'asdfqwe123456', 'konka', NULL, 0, '157', '111', 1, '0:0:38', 1, NULL, NULL);
INSERT INTO `student` VALUES ('st6', 'konka2', '3d5c27a722be0506a7fdc2bd36220674', NULL, 'konka2', NULL, 0, 'konka111', 'itheima14@163.com', 4, '00:00:00', 0, NULL, NULL);

-- ----------------------------
-- Table structure for subject
-- ----------------------------
DROP TABLE IF EXISTS `subject`;
CREATE TABLE `subject`  (
  `id` int(11) NOT NULL,
  `subject_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `faculty_id` int(11) NULL DEFAULT NULL,
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of subject
-- ----------------------------
INSERT INTO `subject` VALUES (1, '数学与应用数学', 1, NULL);
INSERT INTO `subject` VALUES (2, '计算机科学与技术', 1, NULL);
INSERT INTO `subject` VALUES (3, '应用化学', 2, NULL);
INSERT INTO `subject` VALUES (4, '高分子材料', 2, NULL);
INSERT INTO `subject` VALUES (5, '物理专业', 3, NULL);
INSERT INTO `subject` VALUES (6, '学前教育', 4, NULL);
INSERT INTO `subject` VALUES (7, '心理学专业', 4, NULL);
INSERT INTO `subject` VALUES (8, '电子信息工程', 3, NULL);
INSERT INTO `subject` VALUES (9, '数字媒体专业', 3, NULL);

-- ----------------------------
-- Table structure for system
-- ----------------------------
DROP TABLE IF EXISTS `system`;
CREATE TABLE `system`  (
  `sid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `osver` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cpu` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `memory` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `graphicscard` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `open_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `truename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` int(11) NULL DEFAULT NULL,
  `telephone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `job` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `subject_id` int(11) NULL DEFAULT NULL,
  `screen_num_sametime` int(11) NULL DEFAULT NULL,
  `duration` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `times` int(11) NULL DEFAULT NULL,
  `remake` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES ('te1', '1', '44b65e5a7437d9b51839107ad0fe3dfe', NULL, '123', NULL, 0, '123', '123', '1', 2, 0, '00:00:00', 0, NULL);
INSERT INTO `teacher` VALUES ('te10', '1234', 'aa73cc3646cd55ad0686d6fe414b9dc2', NULL, '王守忠', NULL, 0, '13845721684', '123@qq.com', '教授', 1, 100, '0:8:43', 3, NULL);
INSERT INTO `teacher` VALUES ('te11', '3', '7ac9afa28454aeaabc4b02f648c4bfcd', NULL, '3', NULL, 0, '3', '3', '3', 4, 0, '00:00:00', 0, NULL);
INSERT INTO `teacher` VALUES ('te12', 'username', 'adfd33030eee28ebc7ec4ce1d8f50063', NULL, 'truename', NULL, 1, 'telephone', 'email', 'job', 1, 0, '00:00:00', 0, 'remake');
INSERT INTO `teacher` VALUES ('te13', 'konka22212', '1b76bb04198ada27ff4c022f14e4d567', NULL, 'konka2', NULL, 0, '123', 'itheima14@163.com', '11111', 3, 0, '00:00:00', 0, NULL);
INSERT INTO `teacher` VALUES ('te14', '1234123123', 'b0cd8f51263b2d5d6863347f28043584', NULL, '123213', NULL, 0, '123213', '1321321', '123213', 1, 0, '00:00:00', 0, NULL);
INSERT INTO `teacher` VALUES ('te15', '2132321', '6c0abaf7dab83c4940c451a99b71062a', NULL, '213213', NULL, 0, '1321', '132', '132', 1, 0, '00:00:00', 0, NULL);
INSERT INTO `teacher` VALUES ('te16', '31232', 'f7094b43574fd456f3da862aab370027', NULL, '213', NULL, 0, '123', '123', '132', 1, 0, '2:5:12', 0, NULL);
INSERT INTO `teacher` VALUES ('te17', '555666', '1b8cc63a125b21ff4dc067205d89725b', NULL, '123', NULL, 0, '123', '123', '222', 7, 0, '00:00:00', 0, NULL);
INSERT INTO `teacher` VALUES ('te18', 'zyl', '4599c153d10a69a23d773852355ffd30', NULL, '张勇良', NULL, 0, NULL, NULL, NULL, 1, 100, '2:15:53', 39, NULL);
INSERT INTO `teacher` VALUES ('te2', '5678', '558eb0b0bdedf0f17dc0c48eaf39e6ab', NULL, '孙素华', NULL, 1, '15675458478', '123@qq.com', '助教', 1, 100, '00:00:00', 0, NULL);
INSERT INTO `teacher` VALUES ('te20', 'konka', '1ea396074f96ac0ea98226ba8ec39bc7', 'oVa9T0dts63dkRlfIZ3QPXsECt6g', '康佳', '庚仁仕', 0, '157', '123@qq.com', '22', 1, 0, '0:0:55', 4, NULL);
INSERT INTO `teacher` VALUES ('te3', 'a123', '0f95e45f635966f39908e8f7c70fd0a3', NULL, '杨金山', NULL, 0, '18254568741', '456@qq.com', '讲师', 2, 100, '00:00:00', 0, NULL);
INSERT INTO `teacher` VALUES ('te4', 'f123', 'fcb797686dac375961ee235dee5a782f', NULL, '冯俊丽', NULL, 1, '18354796547', '789652@qq.com', '讲师', 6, 0, '00:00:00', 0, NULL);
INSERT INTO `teacher` VALUES ('te5', 'i789', '7dc01034ca5d73b4e65e69feb8a3b96e', NULL, '李亚辉', NULL, 1, '15587462541', '451@qq.com', '讲师', 8, 0, '00:00:00', 0, NULL);
INSERT INTO `teacher` VALUES ('te6', 's123', 'df86f9a593a465a421010ffd68e9701d', NULL, '宋博文', NULL, 0, '16532547852', '154@qq.com', '教授', 3, 0, '4:45:12', 26, NULL);
INSERT INTO `teacher` VALUES ('te7', 'l456', '9f80c8768c92658b2792bae52c5c3662', NULL, '李启超', NULL, 1, '18465475412', '789@qq.com', '副教授', 3, 0, '00:00:00', 0, NULL);
INSERT INTO `teacher` VALUES ('te8', 'ff789', 'ddc26ec8139cbfa92fa696bd9b2e7b81', NULL, '冯娜', NULL, 1, '15865475412', '157@163.com', '导员', 6, 0, '00:00:00', 0, NULL);
INSERT INTO `teacher` VALUES ('te9', '2', '5b5d01609b34664aafefc4bde5f0892f', NULL, '2', NULL, 0, '2', '2', '2', 3, 0, '00:00:00', 0, NULL);

-- ----------------------------
-- Table structure for virtual_room_record
-- ----------------------------
DROP TABLE IF EXISTS `virtual_room_record`;
CREATE TABLE `virtual_room_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `room_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role` int(255) NULL DEFAULT NULL,
  `start_time` datetime(0) NULL DEFAULT NULL,
  `end_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of virtual_room_record
-- ----------------------------
INSERT INTO `virtual_room_record` VALUES (1, 'te20', 'aaa123', 1, '2018-10-12 14:25:54', '2018-10-12 15:25:57');
INSERT INTO `virtual_room_record` VALUES (2, 'te20', 'aaa123', 1, '2018-10-12 17:37:38', NULL);
INSERT INTO `virtual_room_record` VALUES (3, 'te20', 'aaa123', 1, '2018-10-12 17:41:31', NULL);
INSERT INTO `virtual_room_record` VALUES (4, 'te20', 'aaa123', 1, '2018-10-12 17:43:28', NULL);
INSERT INTO `virtual_room_record` VALUES (5, 'te20', 'aaa123', 1, '2018-10-12 18:17:58', '2018-10-12 18:18:37');
INSERT INTO `virtual_room_record` VALUES (6, 'te20', '666666', 1, '2018-11-08 16:43:26', NULL);
INSERT INTO `virtual_room_record` VALUES (7, 'te20', '666666', 1, '2018-11-08 16:44:31', NULL);

-- ----------------------------
-- Table structure for zone
-- ----------------------------
DROP TABLE IF EXISTS `zone`;
CREATE TABLE `zone`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `zone_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zone
-- ----------------------------
INSERT INTO `zone` VALUES (1, '西校区', NULL);
INSERT INTO `zone` VALUES (2, '东校区', NULL);
INSERT INTO `zone` VALUES (3, '北校区', NULL);

SET FOREIGN_KEY_CHECKS = 1;
