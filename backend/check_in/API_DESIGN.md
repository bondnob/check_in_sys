# 当前后端接口文档

本文档基于当前 Spring Boot 代码生成，和实际接口保持一致。

## 1. 基础信息

基础路径：

```http
/api/v1
```

统一返回格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

失败示例：

```json
{
  "code": 401,
  "message": "未登录或 token 缺失",
  "data": null
}
```

## 2. 鉴权方式

登录成功后返回 JWT。

请求头支持两种写法：

```http
Authorization: Bearer {token}
```

或：

```http
Authorization: {token}
```

token 有效期配置：

```properties
auth.jwt.expire-seconds=604800
```

默认 7 天。

## 3. 登录接口

### 3.1 手机号登录

`POST /api/v1/auth/wx-login`

说明：

- 当前代码虽然路径名还是 `wx-login`
- 但实际登录逻辑已经改成“手机号登录”
- 后端先查 `teachers.phone`
- 未查到再查 `students.phone`
- 两边都没有则返回 `404`

请求体：

```json
{
  "phone": "13800000001"
}
```

成功返回：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "jwt-token",
    "userType": "teacher",
    "userId": 1001,
    "userNumber": "T2026001",
    "name": "张老师",
    "avatar": "https://example.com/avatar/teacher1.png",
    "newUser": false
  }
}
```

失败返回：

```json
{
  "code": 404,
  "message": "查无此人",
  "data": null
}
```

## 4. 用户接口

### 4.1 获取当前用户信息

`GET /api/v1/users/me`

权限：

- 已登录用户

返回字段：

- `id`
- `userType`
- `userNumber`
- `name`
- `avatar`
- `phone`
- `createdAt`

### 4.2 修改当前用户信息

`PUT /api/v1/users/me`

权限：

- 已登录用户

请求体：

```json
{
  "name": "王小明",
  "avatar": "https://example.com/avatar/new.png",
  "phone": "13800010001"
}
```

可修改字段：

- `name`
- `avatar`
- `phone`

## 5. 课程接口

### 5.1 创建课程

`POST /api/v1/courses`

权限：

- 教师

请求体：

```json
{
  "courseName": "Java Web 开发",
  "className": "计科231",
  "term": "2025-2026-2",
  "location": "教学楼A-101",
  "stuNumber": 60
}
```

说明：

- `courseName`、`className`、`term` 必填
- `inviteCode` 由后端自动生成

返回：

```json
{
  "id": 1,
  "courseName": "Java Web 开发",
  "inviteCode": "AB12CD",
  "createdAt": "2026-03-24 13:00:00"
}
```

### 5.2 查询教师授课课程列表

`GET /api/v1/courses/teaching`

权限：

- 教师

查询参数：

- `pageNum`
- `pageSize`
- `keyword` 可选

返回分页字段：

- `id`
- `courseName`
- `className`
- `term`
- `location`
- `inviteCode`
- `stuNumber`
- `actualMemberCount`
- `createdAt`

### 5.3 查询学生已加入课程列表

`GET /api/v1/courses/joined`

权限：

- 学生

查询参数：

- `pageNum`
- `pageSize`

返回分页字段：

- `id`
- `courseName`
- `className`
- `term`
- `location`
- `teacherName`
- `joinedAt`
- `createdAt`

### 5.4 课程详情

`GET /api/v1/courses/{courseId}`

权限：

- 该课程教师
- 或该课程学生

返回字段：

- `id`
- `courseName`
- `className`
- `term`
- `location`
- `inviteCode`
- `stuNumber`
- `teacherId`
- `teacherName`
- `memberCount`
- `createdAt`

### 5.5 更新课程

`PUT /api/v1/courses/{courseId}`

权限：

- 课程所属教师

请求体：

```json
{
  "courseName": "Java Web 开发",
  "className": "计科231",
  "term": "2025-2026-2",
  "location": "教学楼A-102",
  "stuNumber": 65
}
```

可修改字段：

- `courseName`
- `className`
- `term`
- `location`
- `stuNumber`

### 5.6 删除课程

`DELETE /api/v1/courses/{courseId}`

权限：

- 课程所属教师

说明：

- 课程下已有签到任务时不能删除

### 5.7 学生通过邀请码加入课程

`POST /api/v1/courses/join`

权限：

- 学生

请求体：

```json
{
  "inviteCode": "JAVA66"
}
```

### 5.8 查看课程成员

`GET /api/v1/courses/{courseId}/members`

权限：

- 课程所属教师

查询参数：

- `pageNum`
- `pageSize`

### 5.9 移除课程成员

`DELETE /api/v1/courses/{courseId}/members/{studentId}`

权限：

- 课程所属教师

## 6. 签到任务接口

### 6.1 创建签到任务

`POST /api/v1/sign-tasks`

权限：

- 教师

请求体通用字段：

- `courseId`
- `title`
- `startTime`
- `endTime`
- `signType`
- `lateTime` 可选

二维码签到示例：

```json
{
  "courseId": 1,
  "title": "第3周二维码签到",
  "startTime": "2026-03-24 14:00:00",
  "endTime": "2026-03-24 14:20:00",
  "signType": 1,
  "qrCode": "QR_JAVA_003",
  "lateTime": "2026-03-24 14:10:00"
}
```

定位签到示例：

```json
{
  "courseId": 1,
  "title": "第4周定位签到",
  "startTime": "2026-03-24 15:00:00",
  "endTime": "2026-03-24 15:20:00",
  "signType": 0,
  "latitude": 30.274150,
  "longitude": 120.155150,
  "radius": 300,
  "lateTime": "2026-03-24 15:10:00"
}
```

校验规则：

- `startTime < endTime`
- `signType` 只能是 `0` 或 `1`
- `signType=1` 时必须传 `qrCode`
- `signType=0` 时必须传 `latitude`、`longitude`、`radius`
- `lateTime` 必须在开始和结束时间之间
- 同一教师只要存在未结束的签到任务，就不能再创建新的签到任务

### 6.2 查询签到任务列表

`GET /api/v1/sign-tasks`

权限：

- 课程教师
- 或课程学生

查询参数：

- `courseId`
- `pageNum`
- `pageSize`

返回字段：

- `id`
- `courseId`
- `courseName`
- `title`
- `startTime`
- `endTime`
- `signType`
- `qrCode`
- `latitude`
- `longitude`
- `radius`
- `lateTime`
- `createdAt`
- `taskState`
- `signedCount`
- `unsignedCount`

`taskState` 可能值：

- `not_started`
- `ongoing`
- `finished`

### 6.3 查询签到任务详情

`GET /api/v1/sign-tasks/{taskId}`

权限：

- 课程教师
- 或课程学生

### 6.4 更新签到任务

`PUT /api/v1/sign-tasks/{taskId}`

权限：

- 课程所属教师

请求体与创建接口基本一致，但不需要 `courseId`

补充校验：

- 如果该教师还有其他未结束签到任务，则不能把当前任务更新为新的未结束签到

### 6.5 删除签到任务

`DELETE /api/v1/sign-tasks/{taskId}`

权限：

- 课程所属教师

说明：

- 任务下已有签到记录时不能删除

### 6.6 查询课程当前有效签到任务

`GET /api/v1/courses/{courseId}/active-sign-task`

权限：

- 课程教师
- 或课程学生

无有效任务时返回：

```json
{
  "hasActiveTask": false
}
```

## 7. 签到记录接口

### 7.1 学生提交签到

`POST /api/v1/sign-records`

权限：

- 学生

请求体通用字段：

- `taskId`
- `location` 可选

二维码签到：

```json
{
  "taskId": 1,
  "qrCode": "QR_JAVA_001",
  "location": "教学楼A-101"
}
```

定位签到：

```json
{
  "taskId": 2,
  "location": "教学楼A-101",
  "latitude": 30.274100,
  "longitude": 120.155100
}
```

后端校验：

- 学生必须属于该课程
- 当前时间必须在签到范围内
- 同一任务不可重复签到
- 二维码签到时校验 `qrCode`
- 定位签到时校验距离是否超出 `radius`

状态值：

- `1`：正常
- `2`：迟到

### 7.2 查询学生本人某任务签到状态

`GET /api/v1/sign-records/my-status/{taskId}`

权限：

- 学生

返回字段：

- `signed`
- `recordId`
- `signTime`
- `status`
- `location`
- `latitude`
- `longitude`

### 7.3 教师查看签到记录列表

`GET /api/v1/sign-records`

权限：

- 教师

查询参数：

- `taskId`
- `status` 可选
- `keyword` 可选
- `pageNum`
- `pageSize`

说明：

- `keyword` 支持姓名/学号模糊查询

### 7.4 教师查看未签到学生列表

`GET /api/v1/sign-records/unsigned`

权限：

- 教师

查询参数：

- `taskId`
- `pageNum`
- `pageSize`

### 7.5 学生查看个人签到记录

`GET /api/v1/sign-records/mine`

权限：

- 学生

查询参数：

- `courseId` 可选
- `pageNum`
- `pageSize`

## 8. 统计接口

### 8.1 课程统计

`GET /api/v1/statistics/courses/{courseId}`

权限：

- 教师

返回字段：

- `courseId`
- `courseName`
- `memberCount`
- `taskCount`
- `totalSignedCount`
- `averageAttendanceRate`

### 8.2 单次签到统计

`GET /api/v1/statistics/sign-tasks/{taskId}`

权限：

- 教师

返回字段：

- `taskId`
- `title`
- `memberCount`
- `signedCount`
- `unsignedCount`
- `attendanceRate`
- `lateCount`

### 8.3 课程学生出勤统计

`GET /api/v1/statistics/courses/{courseId}/students`

权限：

- 教师

查询参数：

- `taskId` 可选，不传表示课程总出勤统计，传入后表示只统计该次签到任务
- `pageNum`
- `pageSize`

返回字段：

- `studentId`
- `studentNumber`
- `studentName`
- `signedCount`
- `unsignedCount`
- `attendanceRate`

## 9. 当前主要错误码

- `200`：成功
- `400`：参数错误
- `401`：未登录、token 无效、token 过期、token 签名错误
- `403`：无权限
- `404`：查无此人、课程不存在、签到任务不存在、邀请码不存在
- `409`：重复签到、重复加课、二维码无效、超出签到范围、当前不在签到时间范围内、已有子数据不可删除
- `500`：系统异常

## 10. 当前实际登录与旧文档差异

当前代码已经不是微信 `code2Session` 登录，而是手机号登录：

- 前端传 `phone`
- 后端查老师表和学生表
- 找到即返回 JWT
- 找不到返回 `查无此人`

如果后续你又改回微信登录，这份文档也需要一起更新。
