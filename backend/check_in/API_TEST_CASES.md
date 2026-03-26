# 接口测试用例

## 1. 使用说明

导入测试数据：

```sql
source test_data.sql;
```

如果你已经升级过表结构，再执行：

```sql
source db_upgrade_v2.sql;
```

服务默认地址：

```http
http://localhost:8080
```

## 2. 测试账号

教师：

- 工号：`T2026001`
- 姓名：`张老师`
- 登录 code：`teacher_001`

学生：

- 学号：`S2026001`
- 姓名：`王小明`
- 登录 code：`student_001`

- 学号：`S2026002`
- 姓名：`李小红`
- 登录 code：`student_002`

说明：

- 当前登录逻辑仍是开发态微信登录模拟
- `code` 会映射为 `mock_openid_{code}`

## 3. 登录用例

### 3.1 教师登录

`POST /api/v1/auth/wx-login`

请求体：

```json
{
  "code": "teacher_001",
  "userType": "teacher",
  "userNumber": "T2026001",
  "name": "张老师",
  "avatar": "https://example.com/avatar/teacher1.png",
  "phone": "13800000001"
}
```

预期：

- 返回 `200`
- 返回 `token`
- `userType=teacher`

### 3.2 学生登录

`POST /api/v1/auth/wx-login`

请求体：

```json
{
  "code": "student_001",
  "userType": "student",
  "userNumber": "S2026001",
  "name": "王小明",
  "avatar": "https://example.com/avatar/student1.png",
  "phone": "13800010001"
}
```

预期：

- 返回 `200`
- 返回 `token`
- `userType=student`

## 4. 教师端测试用例

所有教师接口请求头带：

```http
Authorization: Bearer {teacherToken}
```

### 4.1 查询我教授的课程

`GET /api/v1/courses/teaching?pageNum=1&pageSize=10`

预期：

- 返回课程 1 和课程 2
- 课程 1 名称为 `Java Web 开发`

### 4.2 查看课程详情

`GET /api/v1/courses/1`

预期：

- `courseName=Java Web 开发`
- `teacherName=张老师`
- `memberCount=4`

### 4.3 查看课程成员

`GET /api/v1/courses/1/members?pageNum=1&pageSize=10`

预期：

- 返回 4 名学生

### 4.4 创建二维码签到任务

`POST /api/v1/sign-tasks`

请求体：

```json
{
  "courseId": 1,
  "title": "临时二维码签到",
  "startTime": "2026-03-24 13:00:00",
  "endTime": "2026-03-24 13:20:00",
  "signType": 1,
  "qrCode": "QR_TEMP_001",
  "lateTime": "2026-03-24 13:10:00"
}
```

预期：

- 创建成功
- 返回 `signType=1`
- 返回 `qrCode=QR_TEMP_001`

### 4.5 创建定位签到任务

`POST /api/v1/sign-tasks`

请求体：

```json
{
  "courseId": 1,
  "title": "临时定位签到",
  "startTime": "2026-03-24 14:00:00",
  "endTime": "2026-03-24 14:20:00",
  "signType": 0,
  "latitude": 30.274150,
  "longitude": 120.155150,
  "radius": 300,
  "lateTime": "2026-03-24 14:10:00"
}
```

预期：

- 创建成功
- 返回 `signType=0`
- 返回 `radius=300`

### 4.6 查看签到任务列表

`GET /api/v1/sign-tasks?courseId=1&pageNum=1&pageSize=10`

预期：

- 能看到已有任务
- `taskState` 正常返回 `not_started / ongoing / finished`

### 4.7 查看签到记录

`GET /api/v1/sign-records?taskId=1&pageNum=1&pageSize=10`

预期：

- 返回 2 条记录
- 包含 `王小明` 和 `李小红`

### 4.8 查看未签到名单

`GET /api/v1/sign-records/unsigned?taskId=1&pageNum=1&pageSize=10`

预期：

- 返回 `赵小军` 和 `陈小雨`

### 4.9 查询课程统计

`GET /api/v1/statistics/courses/1`

预期：

- `memberCount=4`
- `taskCount>=2`
- `totalSignedCount>=4`

### 4.10 查询单次签到统计

`GET /api/v1/statistics/sign-tasks/1`

预期：

- `signedCount=2`
- `unsignedCount=2`
- `lateCount=1`

### 4.11 按单次签到任务查询学生出勤统计

`GET /api/v1/statistics/courses/1/students?taskId=1&pageNum=1&pageSize=10`

预期：

- 每个学生的 `signedCount` 只统计任务 1
- 已签到学生 `attendanceRate=100`
- 未签到学生 `attendanceRate=0`

### 4.12 教师存在未结束签到任务时再次创建签到

前提：

- 当前教师已有一个 `endTime` 仍未到达的签到任务

请求：

`POST /api/v1/sign-tasks`

预期：

- 返回 `409`
- message 为 `当前教师已有未结束的签到任务，无法发起新的签到`

## 5. 学生端测试用例

所有学生接口请求头带：

```http
Authorization: Bearer {studentToken}
```

### 5.1 查询已加入课程

`GET /api/v1/courses/joined?pageNum=1&pageSize=10`

预期：

- `王小明` 可看到课程 1 和课程 2

### 5.2 学生通过邀请码加入课程

准备一个未加入课程的学生，例如 `陈小雨`

`POST /api/v1/courses/join`

请求体：

```json
{
  "inviteCode": "DB8888"
}
```

预期：

- 加入成功

### 5.3 查询自己的签到状态

`GET /api/v1/sign-records/my-status/1`

预期：

- `王小明` 为已签到
- `signed=true`

### 5.4 查询个人签到记录

`GET /api/v1/sign-records/mine?pageNum=1&pageSize=10`

预期：

- `王小明` 至少返回任务 1、任务 2 的签到记录

## 6. 二维码签到专项用例

### 6.1 正确二维码签到

前提：

- 教师先创建一个当前时间有效的二维码签到任务

学生请求：

`POST /api/v1/sign-records`

```json
{
  "taskId": 4,
  "qrCode": "QR_TEMP_001",
  "location": "教学楼A-101"
}
```

预期：

- 返回签到成功

### 6.2 错误二维码签到

```json
{
  "taskId": 4,
  "qrCode": "WRONG_CODE",
  "location": "教学楼A-101"
}
```

预期：

- 返回业务错误
- message 为 `二维码无效`

### 6.3 重复签到

同一学生对同一 `taskId` 再提交一次

预期：

- 返回业务错误
- message 为 `当前签到任务已签到`

## 7. 定位签到专项用例

### 7.1 范围内定位签到

前提：

- 教师创建当前时间有效的定位签到任务

学生请求：

```json
{
  "taskId": 5,
  "location": "教学楼A-101",
  "latitude": 30.274200,
  "longitude": 120.155200
}
```

预期：

- 签到成功

### 7.2 超出范围定位签到

```json
{
  "taskId": 5,
  "location": "校外",
  "latitude": 30.280000,
  "longitude": 120.160000
}
```

预期：

- 返回业务错误
- message 为 `超出允许签到范围`

### 7.3 缺少经纬度

```json
{
  "taskId": 5,
  "location": "教学楼A-101"
}
```

预期：

- 返回参数错误或业务错误
- message 为 `定位签到必须提交经纬度`

## 8. 迟到判定用例

前提：

- 签到任务配置了 `lateTime`

测试方法：

- 在 `lateTime` 之前签到，预期 `status=1`
- 在 `lateTime` 之后签到，预期 `status=2`

## 9. 异常用例

### 9.1 未带 token

请求任意受保护接口，不带 `Authorization`

预期：

- 返回 `401`

### 9.2 token 非法

```http
Authorization: Bearer abcdefg
```

预期：

- 返回 `401`

### 9.3 学生访问教师接口

学生调用：

`GET /api/v1/courses/1/members`

预期：

- 返回 `403`

### 9.4 教师操作他人课程

`李老师` 调用 `张老师` 的课程管理接口

预期：

- 返回 `403`
