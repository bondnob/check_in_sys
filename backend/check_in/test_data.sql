SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM sign_records;
DELETE FROM sign_tasks;
DELETE FROM course_members;
DELETE FROM courses;
DELETE FROM students;
DELETE FROM teachers;

ALTER TABLE teachers AUTO_INCREMENT = 1001;
ALTER TABLE students AUTO_INCREMENT = 1001;
ALTER TABLE courses AUTO_INCREMENT = 1;
ALTER TABLE course_members AUTO_INCREMENT = 1;
ALTER TABLE sign_tasks AUTO_INCREMENT = 1;
ALTER TABLE sign_records AUTO_INCREMENT = 1;

INSERT INTO teachers (id, openid, name, user_number, avatar, phone, created_at) VALUES
(1001, 'mock_openid_teacher_001', '张老师', 'T2026001', 'https://example.com/avatar/teacher1.png', '13800000001', NOW()),
(1002, 'mock_openid_teacher_002', '李老师', 'T2026002', 'https://example.com/avatar/teacher2.png', '13800000002', NOW());

INSERT INTO students (id, openid, name, user_number, avatar, phone, created_at) VALUES
(1001, 'mock_openid_student_001', '王小明', 'S2026001', 'https://example.com/avatar/student1.png', '13800010001', NOW()),
(1002, 'mock_openid_student_002', '李小红', 'S2026002', 'https://example.com/avatar/student2.png', '13800010002', NOW()),
(1003, 'mock_openid_student_003', '赵小军', 'S2026003', 'https://example.com/avatar/student3.png', '13800010003', NOW()),
(1004, 'mock_openid_student_004', '陈小雨', 'S2026004', 'https://example.com/avatar/student4.png', '13800010004', NOW());

INSERT INTO courses (id, teacher_id, course_name, invite_code, description, created_at, class_name, stu_number, term) VALUES
(1, 'T2026001', 'Java Web 开发', 'JAVA66', 'Java Web 课堂签到测试课程', NOW(), '计科 231', 4, '2025-2026-2'),
(2, 'T2026001', '数据库原理', 'DB8888', '数据库课程测试', NOW(), '计科 232', 3, '2025-2026-2'),
(3, 'T2026002', '软件工程', 'SE2026', '软件工程课程测试', NOW(), '软工 231', 2, '2025-2026-2');

INSERT INTO course_members (id, course_id, student_id, joined_at) VALUES
(1, 1, 1001, NOW()),
(2, 1, 1002, NOW()),
(3, 1, 1003, NOW()),
(4, 1, 1004, NOW()),
(5, 2, 1001, NOW()),
(6, 2, 1002, NOW()),
(7, 2, 1003, NOW()),
(8, 3, 1004, NOW());

INSERT INTO sign_tasks (id, course_id, title, start_time, end_time, sign_type, qr_code, latitude, longitude, radius, late_time, created_at) VALUES
(1, 1, '第1周课堂二维码签到', '2026-03-24 08:00:00', '2026-03-24 08:20:00', 1, 'QR_JAVA_001', NULL, NULL, NULL, '2026-03-24 08:10:00', NOW()),
(2, 1, '第2周课堂定位签到', '2026-03-24 09:00:00', '2026-03-24 09:20:00', 0, NULL, 30.274150, 120.155150, 300, '2026-03-24 09:10:00', NOW()),
(3, 2, '数据库导论签到', '2026-03-25 10:00:00', '2026-03-25 10:15:00', 1, 'QR_DB_001', NULL, NULL, NULL, '2026-03-25 10:08:00', NOW());

INSERT INTO sign_records (id, task_id, student_id, sign_time, status, location, latitude, longitude) VALUES
(1, 1, 1001, '2026-03-24 08:03:00', 1, '教学楼A-101', NULL, NULL),
(2, 1, 1002, '2026-03-24 08:12:00', 2, '教学楼A-101', NULL, NULL),
(3, 2, 1001, '2026-03-24 09:05:00', 1, '教学楼A-101', 30.274100, 120.155100),
(4, 2, 1002, '2026-03-24 09:12:00', 2, '教学楼A-101', 30.274180, 120.155160);

SET FOREIGN_KEY_CHECKS = 1;
