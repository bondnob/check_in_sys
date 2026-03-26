/*
 Navicat Premium Dump SQL

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : check-in-sys

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 26/03/2026 18:00:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course_members
-- ----------------------------
DROP TABLE IF EXISTS `course_members`;
CREATE TABLE `course_members`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_id` int NOT NULL,
  `student_id` int NOT NULL,
  `joined_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_course_student`(`course_id` ASC, `student_id` ASC) USING BTREE,
  INDEX `student_id`(`student_id` ASC) USING BTREE,
  CONSTRAINT `course_members_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `course_members_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of course_members
-- ----------------------------
INSERT INTO `course_members` VALUES (1, 7, 1, '2026-03-24 20:27:26');
INSERT INTO `course_members` VALUES (2, 8, 10, '2026-03-26 12:48:43');
INSERT INTO `course_members` VALUES (3, 23, 10, '2026-03-26 13:03:49');

-- ----------------------------
-- Table structure for courses
-- ----------------------------
DROP TABLE IF EXISTS `courses`;
CREATE TABLE `courses`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `teacher_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '老师工号',
  `course_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `invite_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邀请码',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上课地点',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级',
  `stu_number` int NULL DEFAULT NULL COMMENT '学生人数',
  `term` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `invite_code`(`invite_code` ASC) USING BTREE,
  INDEX `teacher_id`(`teacher_id` ASC) USING BTREE,
  CONSTRAINT `courses_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`user_number`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of courses
-- ----------------------------
INSERT INTO `courses` VALUES (1, 'T972720', '计算机网络', 'nmgMHN', '1-101', '2026-03-24 16:31:49', '信安1班', 60, '2025-2026-2');
INSERT INTO `courses` VALUES (2, 'T465261', '密码学', 'ogowRS', '2-101', '2026-03-24 02:24:46', '信安1班', 60, '2025-2026-2');
INSERT INTO `courses` VALUES (3, 'T298399', '大学英语', 'kMXtFJ', '3-301', '2026-03-29 21:03:58', '信安1班', 60, '2025-2026-2');
INSERT INTO `courses` VALUES (4, 'T300419', '计算机组成原理', 'JoYuPH', '1-101', '2026-03-30 17:32:05', '信安1班', 60, '2025-2026-2');
INSERT INTO `courses` VALUES (5, 'T796382', '数据结构', 'yZFdUF', '2-101', '2026-03-26 06:03:19', '信安1班', 60, '2025-2026-2');
INSERT INTO `courses` VALUES (6, 'T837345', '高等数学', 'TZLQCF', '3-301', '2026-03-29 07:08:55', '信安1班', 60, '2025-2026-2');
INSERT INTO `courses` VALUES (7, 'T875060', '线性代数', 'AvHCOc', '1-101', '2026-03-30 13:13:11', '信安1班', 60, '2025-2026-2');
INSERT INTO `courses` VALUES (8, 'T757109', '数据库', 'qWppqY', '2-101', '2026-03-26 18:41:33', '信安1班', 60, '2025-2026-2');
INSERT INTO `courses` VALUES (9, 'T793392', 'Java Web 开发', '8MGBJN', '3-301', '2026-03-24 18:01:52', '信安1班', 60, '2025-2026-2');
INSERT INTO `courses` VALUES (11, 'T793392', '裘波', 'S58BNG', 'ad nostrud', '2026-03-24 20:11:55', '信安1班', 60, '2025-2026-2');
INSERT INTO `courses` VALUES (12, 'T793392', '裘波', 'CBAQVJ', 'ad nostrud', '2026-03-24 20:23:51', '信安1班', 60, '2025-2026-2');
INSERT INTO `courses` VALUES (13, 'T793392', '软件工程实践（）', 'C79339203', '系统为教师  生成的示例课程', '2026-03-26 12:54:29', '软工 231', 42, '2025-2026-2');
INSERT INTO `courses` VALUES (14, 'T793392', 'Java Web 开发（）', 'C79339202', '系统为教师  生成的示例课程', '2026-03-26 12:54:29', '计科 232', 48, '2025-2026-2');
INSERT INTO `courses` VALUES (15, 'T793392', '数据库原理（）', 'C79339201', '系统为教师  生成的示例课程', '2026-03-26 12:54:29', '计科 231', 45, '2025-2026-2');
INSERT INTO `courses` VALUES (16, 'T759513', '软件工程实践（马宇宁1）', 'C75951303', '系统为教师 马宇宁1 生成的示例课程', '2026-03-26 12:54:29', '软工 231', 42, '2025-2026-2');
INSERT INTO `courses` VALUES (17, 'T759513', 'Java Web 开发（马宇宁1）', 'C75951302', '系统为教师 马宇宁1 生成的示例课程', '2026-03-26 12:54:29', '计科 232', 48, '2025-2026-2');
INSERT INTO `courses` VALUES (18, 'T759513', '数据库原理（马宇宁1）', 'C75951301', '系统为教师 马宇宁1 生成的示例课程', '2026-03-26 12:54:29', '计科 231', 45, '2025-2026-2');
INSERT INTO `courses` VALUES (19, 'T796382', '软件工程实践（朱致远）', 'C79638203', '系统为教师 朱致远 生成的示例课程', '2026-03-26 12:54:29', '软工 231', 42, '2025-2026-2');
INSERT INTO `courses` VALUES (20, 'T796382', 'Java Web 开发（朱致远）', 'C79638202', '系统为教师 朱致远 生成的示例课程', '2026-03-26 12:54:29', '计科 232', 48, '2025-2026-2');
INSERT INTO `courses` VALUES (21, 'T796382', '数据库原理（朱致远）', 'C79638201', '系统为教师 朱致远 生成的示例课程', '2026-03-26 12:54:29', '计科 231', 45, '2025-2026-2');
INSERT INTO `courses` VALUES (22, 'T757109', '软件工程实践（程睿）', 'C75710903', '系统为教师 程睿 生成的示例课程', '2026-03-26 12:54:29', '软工 231', 42, '2025-2026-2');
INSERT INTO `courses` VALUES (23, 'T757109', 'Java Web 开发（程睿）', 'C75710902', '系统为教师 程睿 生成的示例课程', '2026-03-26 12:54:29', '计科 232', 48, '2025-2026-2');
INSERT INTO `courses` VALUES (24, 'T757109', '数据库原理（程睿）', 'C75710901', '系统为教师 程睿 生成的示例课程', '2026-03-26 12:54:29', '计科 231', 45, '2025-2026-2');
INSERT INTO `courses` VALUES (25, 'T300419', '软件工程实践（傅震南）', 'C30041903', '系统为教师 傅震南 生成的示例课程', '2026-03-26 12:54:29', '软工 231', 42, '2025-2026-2');
INSERT INTO `courses` VALUES (26, 'T300419', 'Java Web 开发（傅震南）', 'C30041902', '系统为教师 傅震南 生成的示例课程', '2026-03-26 12:54:29', '计科 232', 48, '2025-2026-2');
INSERT INTO `courses` VALUES (27, 'T300419', '数据库原理（傅震南）', 'C30041901', '系统为教师 傅震南 生成的示例课程', '2026-03-26 12:54:29', '计科 231', 45, '2025-2026-2');
INSERT INTO `courses` VALUES (28, 'T161896', '软件工程实践（廖子韬）', 'C16189603', '系统为教师 廖子韬 生成的示例课程', '2026-03-26 12:54:29', '软工 231', 42, '2025-2026-2');
INSERT INTO `courses` VALUES (29, 'T161896', 'Java Web 开发（廖子韬）', 'C16189602', '系统为教师 廖子韬 生成的示例课程', '2026-03-26 12:54:29', '计科 232', 48, '2025-2026-2');
INSERT INTO `courses` VALUES (30, 'T161896', '数据库原理（廖子韬）', 'C16189601', '系统为教师 廖子韬 生成的示例课程', '2026-03-26 12:54:29', '计科 231', 45, '2025-2026-2');
INSERT INTO `courses` VALUES (31, 'T456222', '软件工程实践（史宇宁）', 'C45622203', '系统为教师 史宇宁 生成的示例课程', '2026-03-26 12:54:29', '软工 231', 42, '2025-2026-2');
INSERT INTO `courses` VALUES (32, 'T456222', 'Java Web 开发（史宇宁）', 'C45622202', '系统为教师 史宇宁 生成的示例课程', '2026-03-26 12:54:29', '计科 232', 48, '2025-2026-2');
INSERT INTO `courses` VALUES (33, 'T456222', '数据库原理（史宇宁）', 'C45622201', '系统为教师 史宇宁 生成的示例课程', '2026-03-26 12:54:29', '计科 231', 45, '2025-2026-2');
INSERT INTO `courses` VALUES (34, 'T298399', '软件工程实践（姜睿）', 'C29839903', '系统为教师 姜睿 生成的示例课程', '2026-03-26 12:54:29', '软工 231', 42, '2025-2026-2');
INSERT INTO `courses` VALUES (35, 'T298399', 'Java Web 开发（姜睿）', 'C29839902', '系统为教师 姜睿 生成的示例课程', '2026-03-26 12:54:29', '计科 232', 48, '2025-2026-2');
INSERT INTO `courses` VALUES (36, 'T298399', '数据库原理（姜睿）', 'C29839901', '系统为教师 姜睿 生成的示例课程', '2026-03-26 12:54:29', '计科 231', 45, '2025-2026-2');
INSERT INTO `courses` VALUES (37, 'T875060', '软件工程实践（严子韬）', 'C87506003', '系统为教师 严子韬 生成的示例课程', '2026-03-26 12:54:29', '软工 231', 42, '2025-2026-2');
INSERT INTO `courses` VALUES (38, 'T875060', 'Java Web 开发（严子韬）', 'C87506002', '系统为教师 严子韬 生成的示例课程', '2026-03-26 12:54:29', '计科 232', 48, '2025-2026-2');
INSERT INTO `courses` VALUES (39, 'T875060', '数据库原理（严子韬）', 'C87506001', '系统为教师 严子韬 生成的示例课程', '2026-03-26 12:54:29', '计科 231', 45, '2025-2026-2');
INSERT INTO `courses` VALUES (40, 'T535404', '软件工程实践（曹子韬）', 'C53540403', '系统为教师 曹子韬 生成的示例课程', '2026-03-26 12:54:29', '软工 231', 42, '2025-2026-2');
INSERT INTO `courses` VALUES (41, 'T535404', 'Java Web 开发（曹子韬）', 'C53540402', '系统为教师 曹子韬 生成的示例课程', '2026-03-26 12:54:29', '计科 232', 48, '2025-2026-2');
INSERT INTO `courses` VALUES (42, 'T535404', '数据库原理（曹子韬）', 'C53540401', '系统为教师 曹子韬 生成的示例课程', '2026-03-26 12:54:29', '计科 231', 45, '2025-2026-2');
INSERT INTO `courses` VALUES (43, 'T972720', '软件工程实践（邱致远）', 'C97272003', '系统为教师 邱致远 生成的示例课程', '2026-03-26 12:54:29', '软工 231', 42, '2025-2026-2');
INSERT INTO `courses` VALUES (44, 'T972720', 'Java Web 开发（邱致远）', 'C97272002', '系统为教师 邱致远 生成的示例课程', '2026-03-26 12:54:29', '计科 232', 48, '2025-2026-2');
INSERT INTO `courses` VALUES (45, 'T972720', '数据库原理（邱致远）', 'C97272001', '系统为教师 邱致远 生成的示例课程', '2026-03-26 12:54:29', '计科 231', 45, '2025-2026-2');
INSERT INTO `courses` VALUES (46, 'T456164', '软件工程实践（宋璐）', 'C45616403', '系统为教师 宋璐 生成的示例课程', '2026-03-26 12:54:29', '软工 231', 42, '2025-2026-2');
INSERT INTO `courses` VALUES (47, 'T456164', 'Java Web 开发（宋璐）', 'C45616402', '系统为教师 宋璐 生成的示例课程', '2026-03-26 12:54:29', '计科 232', 48, '2025-2026-2');
INSERT INTO `courses` VALUES (48, 'T456164', '数据库原理（宋璐）', 'C45616401', '系统为教师 宋璐 生成的示例课程', '2026-03-26 12:54:29', '计科 231', 45, '2025-2026-2');
INSERT INTO `courses` VALUES (49, 'T837345', '软件工程实践（王睿）', 'C83734503', '系统为教师 王睿 生成的示例课程', '2026-03-26 12:54:29', '软工 231', 42, '2025-2026-2');
INSERT INTO `courses` VALUES (50, 'T837345', 'Java Web 开发（王睿）', 'C83734502', '系统为教师 王睿 生成的示例课程', '2026-03-26 12:54:29', '计科 232', 48, '2025-2026-2');
INSERT INTO `courses` VALUES (51, 'T837345', '数据库原理（王睿）', 'C83734501', '系统为教师 王睿 生成的示例课程', '2026-03-26 12:54:29', '计科 231', 45, '2025-2026-2');
INSERT INTO `courses` VALUES (52, 'T111150', '软件工程实践（郑晓明）', 'C11115003', '系统为教师 郑晓明 生成的示例课程', '2026-03-26 12:54:29', '软工 231', 42, '2025-2026-2');
INSERT INTO `courses` VALUES (53, 'T111150', 'Java Web 开发（郑晓明）', 'C11115002', '系统为教师 郑晓明 生成的示例课程', '2026-03-26 12:54:29', '计科 232', 48, '2025-2026-2');
INSERT INTO `courses` VALUES (54, 'T111150', '数据库原理（郑晓明）', 'C11115001', '系统为教师 郑晓明 生成的示例课程', '2026-03-26 12:54:29', '计科 231', 45, '2025-2026-2');
INSERT INTO `courses` VALUES (55, 'T465261', '软件工程实践（卢致远）', 'C46526103', '系统为教师 卢致远 生成的示例课程', '2026-03-26 12:54:29', '软工 231', 42, '2025-2026-2');
INSERT INTO `courses` VALUES (56, 'T465261', 'Java Web 开发（卢致远）', 'C46526102', '系统为教师 卢致远 生成的示例课程', '2026-03-26 12:54:29', '计科 232', 48, '2025-2026-2');
INSERT INTO `courses` VALUES (57, 'T465261', '数据库原理（卢致远）', 'C46526101', '系统为教师 卢致远 生成的示例课程', '2026-03-26 12:54:29', '计科 231', 45, '2025-2026-2');

-- ----------------------------
-- Table structure for sign_records
-- ----------------------------
DROP TABLE IF EXISTS `sign_records`;
CREATE TABLE `sign_records`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `task_id` int NOT NULL,
  `student_id` int NOT NULL,
  `sign_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `status` tinyint NULL DEFAULT 1 COMMENT '1-正常, 2-迟到等',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '签到地理位置(可选)',
  `latitude` decimal(10, 6) NULL DEFAULT NULL COMMENT '签到时纬度',
  `longitude` decimal(10, 6) NULL DEFAULT NULL COMMENT '签到时经度',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_task_student`(`task_id` ASC, `student_id` ASC) USING BTREE,
  INDEX `student_id`(`student_id` ASC) USING BTREE,
  CONSTRAINT `sign_records_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `sign_tasks` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `sign_records_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sign_records
-- ----------------------------
INSERT INTO `sign_records` VALUES (1, 4, 10, '2026-03-26 13:19:55', 1, 'dolor sed Lorem voluptate ad', 28.000000, 44.000000);
INSERT INTO `sign_records` VALUES (2, 5, 10, '2026-03-26 13:27:08', 1, 'dolor sed Lorem voluptate ad', 28.000000, 44.000000);

-- ----------------------------
-- Table structure for sign_tasks
-- ----------------------------
DROP TABLE IF EXISTS `sign_tasks`;
CREATE TABLE `sign_tasks`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_id` int NOT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '签到标题',
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `sign_type` tinyint NULL DEFAULT 1 COMMENT '1-二维码签到, 0-定位签到',
  `qr_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '二维码签到口令',
  `latitude` decimal(10, 6) NULL DEFAULT NULL COMMENT '签到中心纬度',
  `longitude` decimal(10, 6) NULL DEFAULT NULL COMMENT '签到中心经度',
  `radius` int NULL DEFAULT NULL COMMENT '允许签到半径(米)',
  `late_time` datetime NULL DEFAULT NULL COMMENT '迟到判定时间',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int NULL DEFAULT NULL COMMENT '签到是否完成，0是进行中，1是已完成',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `course_id`(`course_id` ASC) USING BTREE,
  CONSTRAINT `sign_tasks_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sign_tasks
-- ----------------------------
INSERT INTO `sign_tasks` VALUES (1, 8, '牛牛的测试', '2026-03-25 10:59:14', '2026-03-25 12:14:55', 1, '40', 3.000000, 66.000000, 81, '2026-03-25 12:00:00', '2026-03-25 16:04:01', 1);
INSERT INTO `sign_tasks` VALUES (2, 8, '牛牛的测试', '2026-03-25 10:59:14', '2026-03-25 12:14:55', 1, '40', 3.000000, 66.000000, 81, NULL, '2026-03-25 16:22:38', NULL);
INSERT INTO `sign_tasks` VALUES (3, 8, '牛牛的测试', '2026-03-25 10:59:14', '2026-03-25 12:14:55', 0, NULL, 3.000000, 66.000000, 81, '2026-03-25 12:00:00', '2026-03-25 16:23:03', NULL);
INSERT INTO `sign_tasks` VALUES (4, 23, '如何骨碌碌像肾上腺素俄罗斯传统头巾', '2026-03-26 09:35:22', '2026-04-21 13:52:03', 1, '6', 57.000000, 80.000000, 78, '2026-04-20 04:39:23', '2026-03-26 13:02:12', 0);
INSERT INTO `sign_tasks` VALUES (5, 23, '噜噜的测试', '2026-03-26 09:35:22', '2026-04-21 13:52:03', 1, '600', 57.000000, 80.000000, 78, '2026-04-20 04:39:23', '2026-03-26 13:25:45', 0);
INSERT INTO `sign_tasks` VALUES (6, 24, '签到了', '2026-03-26 00:00:00', '2026-03-26 02:00:00', 1, '66', NULL, NULL, NULL, '2026-03-26 01:00:00', '2026-03-26 13:43:57', 1);
INSERT INTO `sign_tasks` VALUES (7, 24, '123', '2026-03-26 00:00:00', '2026-03-26 02:00:00', 1, '97', NULL, NULL, NULL, '2026-03-26 01:00:00', '2026-03-26 13:47:14', 1);
INSERT INTO `sign_tasks` VALUES (8, 23, '123', '2026-03-26 00:00:00', '2026-03-26 02:00:00', 1, '03', NULL, NULL, NULL, '2026-03-26 01:00:00', '2026-03-26 13:51:35', 1);
INSERT INTO `sign_tasks` VALUES (9, 24, '测试1', '2026-03-26 14:03:00', '2026-03-26 16:02:00', 1, '43', NULL, NULL, NULL, '2026-03-26 15:05:00', '2026-03-26 14:02:23', 1);
INSERT INTO `sign_tasks` VALUES (10, 24, '测试1', '2026-03-26 14:03:00', '2026-03-26 16:02:00', 1, '05', NULL, NULL, NULL, '2026-03-26 15:05:00', '2026-03-26 14:05:04', 1);
INSERT INTO `sign_tasks` VALUES (11, 22, '1231签到', '2026-03-26 14:13:00', '2026-03-26 17:01:00', 1, '23', NULL, NULL, NULL, '2026-03-26 16:00:00', '2026-03-26 14:14:48', 1);

-- ----------------------------
-- Table structure for students
-- ----------------------------
DROP TABLE IF EXISTS `students`;
CREATE TABLE `students`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `openid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信openid',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `user_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学号',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学生班级',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `openid`(`openid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1001 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of students
-- ----------------------------
INSERT INTO `students` VALUES (1, 'o6eoVP', '夏詩涵', '20257420', '14981850652', '信安1班', 'https://image1.com', '2026-03-30 13:26:22');
INSERT INTO `students` VALUES (2, '3prCBI', '贾安琪', '20245068', '15574166903', '信安1班', 'https://image1.com', '2026-03-31 08:42:31');
INSERT INTO `students` VALUES (3, 'yaegMD', '阎震南', '20257349', '14867770372', '信安1班', 'https://image1.com', '2026-03-27 18:36:58');
INSERT INTO `students` VALUES (4, 'dQDgzd', '秦子韬', '20241721', '19402140662', '信安1班', 'https://iamge2.com', '2026-03-30 10:47:13');
INSERT INTO `students` VALUES (5, '8KrSTp', '孔岚', '20253222', '19322085561', '信安1班', 'https://image1.com', '2026-03-24 06:11:10');
INSERT INTO `students` VALUES (6, 'cKvBWL', '姜震南', '20228774', '19575890154', '信安1班', 'https://image1.com', '2026-03-29 03:37:15');
INSERT INTO `students` VALUES (7, 'zp7aPr', '余睿', '20233623', '19924554459', '信安1班', 'https://iamge2.com', '2026-03-31 04:53:18');
INSERT INTO `students` VALUES (8, 'JhV8pU', '唐云熙', '20237442', '16792510461', '信安1班', 'https://iamge2.com', '2026-03-29 07:15:42');
INSERT INTO `students` VALUES (9, 'IUmgWr', '史詩涵', '20236446', '17906485246', '信安1班', 'https://iamge2.com', '2026-03-26 04:10:09');
INSERT INTO `students` VALUES (10, '0gCfOZ', '钟致远', '20249377', '18786635220', '信安1班', 'https://iamge2.com', '2026-03-29 01:07:27');
INSERT INTO `students` VALUES (11, 'vJ2vOH', '程秀英', '20224354', '17676410121', '信安1班', 'https://image1.com', '2026-03-29 17:03:17');
INSERT INTO `students` VALUES (12, '1Qzaqp', '段睿', '20243723', '15356836177', '信安1班', 'https://iamge2.com', '2026-03-29 05:06:40');
INSERT INTO `students` VALUES (13, 'Ivm2uK', '王震南', '20242805', '17581432003', '信安1班', 'https://iamge2.com', '2026-03-28 15:05:53');
INSERT INTO `students` VALUES (14, '5aEd6d', '丁宇宁', '20258712', '14516409936', '信安1班', 'https://image1.com', '2026-03-25 08:02:15');
INSERT INTO `students` VALUES (15, 'HOuEZO', '丁震南', '20266945', '14753353332', '信安1班', 'https://image1.com', '2026-03-28 04:02:38');
INSERT INTO `students` VALUES (16, 'zUX2an', '贾嘉伦', '20249438', '14656242643', '信安1班', 'https://image1.com', '2026-03-29 03:38:32');
INSERT INTO `students` VALUES (17, 'IEzcvK', '孔詩涵', '20238679', '19868773163', '信安1班', 'https://image1.com', '2026-03-30 10:10:25');
INSERT INTO `students` VALUES (18, 'nRNwc9', '秦安琪', '20253502', '15989187477', '信安1班', 'https://image1.com', '2026-03-28 20:44:23');
INSERT INTO `students` VALUES (19, 'e2HTZm', '何岚', '20268139', '15862680077', '信安1班', 'https://image1.com', '2026-03-30 06:15:08');
INSERT INTO `students` VALUES (20, 'HcmUem', '杨岚', '20250496', '15636281884', '信安1班', 'https://image1.com', '2026-03-29 22:17:11');

-- ----------------------------
-- Table structure for teachers
-- ----------------------------
DROP TABLE IF EXISTS `teachers`;
CREATE TABLE `teachers`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `openid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信openid',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `user_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `openid`(`openid` ASC) USING BTREE,
  INDEX `user_number`(`user_number` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1001 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teachers
-- ----------------------------
INSERT INTO `teachers` VALUES (1, 'GWCvvx', '', 'T793392', '', '2026-03-30 17:06:14', '');
INSERT INTO `teachers` VALUES (2, 'kF8o4X', '马宇宁1', 'T759513', '', '2026-03-24 17:21:37', '');
INSERT INTO `teachers` VALUES (3, '0eNT6B', '朱致远', 'T796382', 'https://image1.com', '2026-03-28 23:20:45', '15366199999');
INSERT INTO `teachers` VALUES (4, 'iA8zbw', '程睿', 'T757109', 'https://image1.com', '2026-03-28 21:39:18', '13413221624');
INSERT INTO `teachers` VALUES (5, 'penwIT', '傅震南', 'T300419', 'https://image1.com', '2026-03-30 23:01:28', '15477251748');
INSERT INTO `teachers` VALUES (6, '0MCQkO', '廖子韬', 'T161896', 'https://image1.com', '2026-03-27 02:36:47', '17420358908');
INSERT INTO `teachers` VALUES (7, 'qxnzG4', '史宇宁', 'T456222', 'https://image1.com', '2026-03-28 22:18:39', '18538357350');
INSERT INTO `teachers` VALUES (8, 'Fau4mW', '姜睿', 'T298399', 'https://image1.com', '2026-03-31 02:25:28', '15500183959');
INSERT INTO `teachers` VALUES (9, 'TclhSH', '严子韬', 'T875060', 'https://image1.com', '2026-03-26 21:10:19', '18628765947');
INSERT INTO `teachers` VALUES (10, 'hEcsy9', '曹子韬', 'T535404', 'https://image1.com', '2026-03-26 13:03:56', '15888141196');
INSERT INTO `teachers` VALUES (11, '5wxNZI', '邱致远', 'T972720', 'https://image1.com', '2026-03-26 21:49:33', '16452603566');
INSERT INTO `teachers` VALUES (12, 'iY4ivx', '宋璐', 'T456164', 'https://image1.com', '2026-03-29 23:53:57', '18610125694');
INSERT INTO `teachers` VALUES (13, 'oAOMWO', '王睿', 'T837345', 'https://image1.com', '2026-03-24 01:00:34', '16885695893');
INSERT INTO `teachers` VALUES (14, 'Hpeyvf', '郑晓明', 'T111150', 'https://image1.com', '2026-03-28 23:17:27', '13776945517');
INSERT INTO `teachers` VALUES (15, 'y5IDkT', '卢致远', 'T465261', 'https://image1.com', '2026-03-31 01:13:08', '14530033397');

SET FOREIGN_KEY_CHECKS = 1;
