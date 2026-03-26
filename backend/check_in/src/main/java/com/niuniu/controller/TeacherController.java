package com.niuniu.controller;

import com.niuniu.common.ApiResponse;
import com.niuniu.dto.common.PageResponse;
import com.niuniu.dto.course.CourseDetailResponse;
import com.niuniu.dto.course.CourseMemberResponse;
import com.niuniu.dto.course.CourseResponse;
import com.niuniu.dto.course.CreateCourseRequest;
import com.niuniu.dto.course.UpdateCourseRequest;
import com.niuniu.dto.record.SignRecordResponse;
import com.niuniu.dto.statistics.CourseStatisticsResponse;
import com.niuniu.dto.statistics.StudentAttendanceResponse;
import com.niuniu.dto.statistics.TaskStatisticsResponse;
import com.niuniu.dto.task.CreateSignTaskRequest;
import com.niuniu.dto.task.SignTaskResponse;
import com.niuniu.dto.task.UpdateSignTaskRequest;
import com.niuniu.dto.user.UpdateProfileRequest;
import com.niuniu.dto.user.UserProfileResponse;
import com.niuniu.service.CourseService;
import com.niuniu.service.SignRecordService;
import com.niuniu.service.SignTaskService;
import com.niuniu.service.StatisticsService;
import com.niuniu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/teacher")
public class TeacherController {

    private final UserService userService;
    private final CourseService courseService;
    private final SignTaskService signTaskService;
    private final SignRecordService signRecordService;
    private final StatisticsService statisticsService;

    public TeacherController(UserService userService, CourseService courseService, SignTaskService signTaskService,
                             SignRecordService signRecordService, StatisticsService statisticsService) {
        this.userService = userService;
        this.courseService = courseService;
        this.signTaskService = signTaskService;
        this.signRecordService = signRecordService;
        this.statisticsService = statisticsService;
    }

    @GetMapping("/me")
    @Operation(summary = "查询教师个人信息")
    public ApiResponse<UserProfileResponse> me() {
        log.info("教师查询个人信息");
        return ApiResponse.success(userService.me());
    }

    @PutMapping("/me")
    @Operation(summary = "更新教师个人信息")
    public ApiResponse<Void> updateMe(@RequestBody UpdateProfileRequest request) {
        log.info("教师更新个人信息: name={}, phone={}", request.getName(), request.getPhone());
        userService.updateProfile(request);
        return ApiResponse.success();
    }

    @PostMapping("/courses")
    @Operation(summary = "创建课程")
    public ApiResponse<CourseResponse> createCourse(@RequestBody CreateCourseRequest request) {
        log.info("教师创建课程: courseName={}, className={}, term={}", request.getCourseName(), request.getClassName(), request.getTerm());
        return ApiResponse.success(courseService.createCourse(request));
    }

    @GetMapping("/courses")
    @Operation(summary = "查询教师课程列表")
    public ApiResponse<PageResponse<CourseResponse>> teachingCourses(@RequestParam(defaultValue = "1") Integer pageNum,
                                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                                     @RequestParam(required = false) String keyword) {
        log.info("教师查询课程列表: pageNum={}, pageSize={}, keyword={}", pageNum, pageSize, keyword);
        return ApiResponse.success(courseService.listTeachingCourses(pageNum, pageSize, keyword));
    }

    @GetMapping("/courses/{courseId}")
    @Operation(summary = "查询课程详情")
    public ApiResponse<CourseDetailResponse> courseDetail(@PathVariable Integer courseId) {
        log.info("教师查询课程详情: courseId={}", courseId);
        return ApiResponse.success(courseService.getCourseDetail(courseId));
    }

    @PutMapping("/courses/{courseId}")
    @Operation(summary = "更新课程")
    public ApiResponse<Void> updateCourse(@PathVariable Integer courseId, @RequestBody UpdateCourseRequest request) {
        log.info("教师更新课程: courseId={}, courseName={}", courseId, request.getCourseName());
        courseService.updateCourse(courseId, request);
        return ApiResponse.success();
    }

    @DeleteMapping("/courses/{courseId}")
    @Operation(summary = "删除课程")
    public ApiResponse<Void> deleteCourse(@PathVariable Integer courseId) {
        log.info("教师删除课程: courseId={}", courseId);
        courseService.deleteCourse(courseId);
        return ApiResponse.success();
    }

    @GetMapping("/courses/{courseId}/members")
    @Operation(summary = "查询课程成员")
    public ApiResponse<PageResponse<CourseMemberResponse>> courseMembers(@PathVariable Integer courseId,
                                                                         @RequestParam(defaultValue = "1") Integer pageNum,
                                                                         @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("教师查询课程成员: courseId={}, pageNum={}, pageSize={}", courseId, pageNum, pageSize);
        return ApiResponse.success(courseService.listMembers(courseId, pageNum, pageSize));
    }

    @DeleteMapping("/courses/{courseId}/members/{studentId}")
    @Operation(summary = "移除课程成员")
    public ApiResponse<Void> removeMember(@PathVariable Integer courseId, @PathVariable Integer studentId) {
        log.info("教师移除课程成员: courseId={}, studentId={}", courseId, studentId);
        courseService.removeMember(courseId, studentId);
        return ApiResponse.success();
    }

    @PostMapping("/sign-tasks")
    @Operation(summary = "创建签到任务")
    public ApiResponse<SignTaskResponse> createSignTask(@RequestBody CreateSignTaskRequest request) {
        log.info("教师创建签到任务: courseId={}, title={}, signType={}", request.getCourseId(), request.getTitle(), request.getSignType());
        return ApiResponse.success(signTaskService.create(request));
    }

    @GetMapping("/sign-tasks")
    @Operation(summary = "查询教师签到任务列表")
    public ApiResponse<PageResponse<SignTaskResponse>> signTaskList(@RequestParam(defaultValue = "1") Integer pageNum,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("教师查询签到任务列表: pageNum={}, pageSize={}", pageNum, pageSize);
        return ApiResponse.success(signTaskService.listForTeacher(pageNum, pageSize));
    }

    @GetMapping("/sign-tasks/{taskId}")
    @Operation(summary = "查询签到任务详情")
    public ApiResponse<SignTaskResponse> signTaskDetail(@PathVariable Integer taskId) {
        log.info("教师查询签到任务详情: taskId={}", taskId);
        return ApiResponse.success(signTaskService.detail(taskId));
    }

    @PutMapping("/sign-tasks/{taskId}")
    @Operation(summary = "更新签到任务")
    public ApiResponse<Void> updateSignTask(@PathVariable Integer taskId, @RequestBody UpdateSignTaskRequest request) {
        log.info("教师更新签到任务: taskId={}, title={}", taskId, request.getTitle());
        signTaskService.update(taskId, request);
        return ApiResponse.success();
    }

    @DeleteMapping("/sign-tasks/{taskId}")
    @Operation(summary = "删除签到任务")
    public ApiResponse<Void> deleteSignTask(@PathVariable Integer taskId) {
        log.info("教师删除签到任务: taskId={}", taskId);
        signTaskService.delete(taskId);
        return ApiResponse.success();
    }

    @GetMapping("/courses/{courseId}/active-sign-task")
    @Operation(summary = "查询当前有效签到任务")
    public ApiResponse<SignTaskResponse> activeSignTask(@PathVariable Integer courseId) {
        log.info("教师查询当前有效签到任务: courseId={}", courseId);
        return ApiResponse.success(signTaskService.activeTask(courseId));
    }

    @GetMapping("/sign-records")
    @Operation(summary = "查询签到记录")
    public ApiResponse<PageResponse<SignRecordResponse>> signRecords(@RequestParam Integer taskId,
                                                                     @RequestParam(required = false) Integer status,
                                                                     @RequestParam(required = false) String keyword,
                                                                     @RequestParam(defaultValue = "1") Integer pageNum,
                                                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("教师查询签到记录: taskId={}, status={}, keyword={}, pageNum={}, pageSize={}", taskId, status, keyword, pageNum, pageSize);
        return ApiResponse.success(signRecordService.listByTask(taskId, status, keyword, pageNum, pageSize));
    }

    @GetMapping("/sign-records/unsigned")
    @Operation(summary = "查询未签到名单")
    public ApiResponse<PageResponse<SignRecordResponse>> unsignedRecords(@RequestParam Integer taskId,
                                                                         @RequestParam(defaultValue = "1") Integer pageNum,
                                                                         @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("教师查询未签到名单: taskId={}, pageNum={}, pageSize={}", taskId, pageNum, pageSize);
        return ApiResponse.success(signRecordService.listUnsigned(taskId, pageNum, pageSize));
    }

    @GetMapping("/statistics/courses/{courseId}")
    @Operation(summary = "查询课程统计")
    public ApiResponse<CourseStatisticsResponse> courseStatistics(@PathVariable Integer courseId) {
        log.info("教师查询课程统计: courseId={}", courseId);
        return ApiResponse.success(statisticsService.courseStatistics(courseId));
    }

    @GetMapping("/statistics/sign-tasks/{taskId}")
    @Operation(summary = "查询任务统计")
    public ApiResponse<TaskStatisticsResponse> taskStatistics(@PathVariable Integer taskId) {
        log.info("教师查询任务统计: taskId={}", taskId);
        return ApiResponse.success(statisticsService.taskStatistics(taskId));
    }

    @GetMapping("/statistics/courses/{courseId}/students")
    @Operation(summary = "查询学生出勤统计")
    public ApiResponse<PageResponse<StudentAttendanceResponse>> studentStatistics(@PathVariable Integer courseId,
                                                                                  @RequestParam(required = false) Integer taskId,
                                                                                  @RequestParam(defaultValue = "1") Integer pageNum,
                                                                                  @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("教师查询学生出勤统计: courseId={}, taskId={}, pageNum={}, pageSize={}", courseId, taskId, pageNum, pageSize);
        return ApiResponse.success(statisticsService.studentStatistics(courseId, taskId, pageNum, pageSize));
    }
}
