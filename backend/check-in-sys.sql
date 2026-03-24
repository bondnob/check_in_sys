/*
 Navicat Premium Dump SQL

 Source Server         : xiaok
 Source Server Type    : MySQL
 Source Server Version : 80037 (8.0.37)
 Source Host           : localhost:3306
 Source Schema         : check-in-sys

 Target Server Type    : MySQL
 Target Server Version : 80037 (8.0.37)
 File Encoding         : 65001

 Date: 24/03/2026 13:52:28
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_members
-- ----------------------------

-- ----------------------------
-- Table structure for courses
-- ----------------------------
DROP TABLE IF EXISTS `courses`;
CREATE TABLE `courses`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `teacher_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '老师工号',
  `course_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `invite_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邀请码',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程简介',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级',
  `stu_number` int NULL DEFAULT NULL COMMENT '学生人数',
  `term` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `invite_code`(`invite_code` ASC) USING BTREE,
  INDEX `teacher_id`(`teacher_id` ASC) USING BTREE,
  CONSTRAINT `courses_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`user_number`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of courses
-- ----------------------------
INSERT INTO `courses` VALUES (1, 'T972720', '计算机网络', 'nmgMHN', '简介', '2026-03-24 16:31:49', '', NULL, '');
INSERT INTO `courses` VALUES (2, 'T465261', '密码学', 'ogowRS', '简介', '2026-03-24 02:24:46', '', NULL, '');
INSERT INTO `courses` VALUES (3, 'T298399', '大学英语', 'kMXtFJ', '简介', '2026-03-29 21:03:58', '', NULL, '');
INSERT INTO `courses` VALUES (4, 'T300419', '计算机组成员咯', 'JoYuPH', '简介', '2026-03-30 17:32:05', '', NULL, '');
INSERT INTO `courses` VALUES (5, 'T796382', '数据结构', 'yZFdUF', '简介', '2026-03-26 06:03:19', '', NULL, '');
INSERT INTO `courses` VALUES (6, 'T837345', '高等数学', 'TZLQCF', '简介', '2026-03-29 07:08:55', '', NULL, '');
INSERT INTO `courses` VALUES (7, 'T875060', '线性代数', 'AvHCOc', '简介', '2026-03-30 13:13:11', '', NULL, '');
INSERT INTO `courses` VALUES (8, 'T757109', '数据库', 'qWppqY', '简介', '2026-03-26 18:41:33', '', NULL, '');

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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_task_student`(`task_id` ASC, `student_id` ASC) USING BTREE,
  INDEX `student_id`(`student_id` ASC) USING BTREE,
  CONSTRAINT `sign_records_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `sign_tasks` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `sign_records_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sign_records
-- ----------------------------

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
  `status` tinyint NULL DEFAULT 1 COMMENT '1-扫码签到, 0-定位签到',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `course_id`(`course_id` ASC) USING BTREE,
  CONSTRAINT `sign_tasks_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sign_tasks
-- ----------------------------

-- ----------------------------
-- Table structure for students
-- ----------------------------
DROP TABLE IF EXISTS `students`;
CREATE TABLE `students`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `openid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信openid',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `user_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `openid`(`openid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1001 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of students
-- ----------------------------
INSERT INTO `students` VALUES (1, 'o6eoVP', '夏詩涵', '20257420', 'https://image1.com', '2026-03-30 13:26:22', '14981850652');
INSERT INTO `students` VALUES (2, '3prCBI', '贾安琪', '20245068', 'https://image1.com', '2026-03-31 08:42:31', '15574166903');
INSERT INTO `students` VALUES (3, 'yaegMD', '阎震南', '20257349', 'https://image1.com', '2026-03-27 18:36:58', '14867770372');
INSERT INTO `students` VALUES (4, 'dQDgzd', '秦子韬', '20241721', 'https://iamge2.com', '2026-03-30 10:47:13', '19402140662');
INSERT INTO `students` VALUES (5, '8KrSTp', '孔岚', '20253222', 'https://image1.com', '2026-03-24 06:11:10', '19322085561');
INSERT INTO `students` VALUES (6, 'cKvBWL', '姜震南', '20228774', 'https://image1.com', '2026-03-29 03:37:15', '19575890154');
INSERT INTO `students` VALUES (7, 'zp7aPr', '余睿', '20233623', 'https://iamge2.com', '2026-03-31 04:53:18', '19924554459');
INSERT INTO `students` VALUES (8, 'JhV8pU', '唐云熙', '20237442', 'https://iamge2.com', '2026-03-29 07:15:42', '16792510461');
INSERT INTO `students` VALUES (9, 'IUmgWr', '史詩涵', '20236446', 'https://iamge2.com', '2026-03-26 04:10:09', '17906485246');
INSERT INTO `students` VALUES (10, '0gCfOZ', '钟致远', '20249377', 'https://iamge2.com', '2026-03-29 01:07:27', '18786635220');
INSERT INTO `students` VALUES (11, 'vJ2vOH', '程秀英', '20224354', 'https://image1.com', '2026-03-29 17:03:17', '17676410121');
INSERT INTO `students` VALUES (12, '1Qzaqp', '段睿', '20243723', 'https://iamge2.com', '2026-03-29 05:06:40', '15356836177');
INSERT INTO `students` VALUES (13, 'Ivm2uK', '王震南', '20242805', 'https://iamge2.com', '2026-03-28 15:05:53', '17581432003');
INSERT INTO `students` VALUES (14, '5aEd6d', '丁宇宁', '20258712', 'https://image1.com', '2026-03-25 08:02:15', '14516409936');
INSERT INTO `students` VALUES (15, 'HOuEZO', '丁震南', '20266945', 'https://image1.com', '2026-03-28 04:02:38', '14753353332');
INSERT INTO `students` VALUES (16, 'zUX2an', '贾嘉伦', '20249438', 'https://image1.com', '2026-03-29 03:38:32', '14656242643');
INSERT INTO `students` VALUES (17, 'IEzcvK', '孔詩涵', '20238679', 'https://image1.com', '2026-03-30 10:10:25', '19868773163');
INSERT INTO `students` VALUES (18, 'nRNwc9', '秦安琪', '20253502', 'https://image1.com', '2026-03-28 20:44:23', '15989187477');
INSERT INTO `students` VALUES (19, 'e2HTZm', '何岚', '20268139', 'https://image1.com', '2026-03-30 06:15:08', '15862680077');
INSERT INTO `students` VALUES (20, 'HcmUem', '杨岚', '20250496', 'https://image1.com', '2026-03-29 22:17:11', '15636281884');

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
) ENGINE = InnoDB AUTO_INCREMENT = 1001 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teachers
-- ----------------------------
INSERT INTO `teachers` VALUES (1, 'GWCvvx', '杜璐', 'T793392', 'https://image1.com', '2026-03-30 17:06:14', '18621034422');
INSERT INTO `teachers` VALUES (2, 'kF8o4X', '马宇宁', 'T759513', 'https://image1.com', '2026-03-24 17:21:37', '15662252864');
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
