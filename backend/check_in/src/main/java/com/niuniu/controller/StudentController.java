package com.niuniu.controller;

import com.niuniu.common.ApiResponse;
import com.niuniu.dto.common.PageResponse;
import com.niuniu.dto.course.CourseDetailResponse;
import com.niuniu.dto.course.CourseResponse;
import com.niuniu.dto.course.JoinCourseRequest;
import com.niuniu.dto.record.MySignStatusResponse;
import com.niuniu.dto.record.SignRecordResponse;
import com.niuniu.dto.record.SubmitSignRequest;
import com.niuniu.dto.task.SignTaskResponse;
import com.niuniu.dto.user.UpdateProfileRequest;
import com.niuniu.dto.user.UserProfileResponse;
import com.niuniu.service.CourseService;
import com.niuniu.service.SignRecordService;
import com.niuniu.service.SignTaskService;
import com.niuniu.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/v1/student")
public class StudentController {

    private final UserService userService;
    private final CourseService courseService;
    private final SignTaskService signTaskService;
    private final SignRecordService signRecordService;

    public StudentController(UserService userService, CourseService courseService,
                             SignTaskService signTaskService, SignRecordService signRecordService) {
        this.userService = userService;
        this.courseService = courseService;
        this.signTaskService = signTaskService;
        this.signRecordService = signRecordService;
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> me() {
        log.info("学生查询个人信息");
        return ApiResponse.success(userService.me());
    }

    @PutMapping("/me")
    public ApiResponse<Void> updateMe(@RequestBody UpdateProfileRequest request) {
        log.info("学生更新个人信息: name={}, phone={}", request.getName(), request.getPhone());
        userService.updateProfile(request);
        return ApiResponse.success();
    }

    @GetMapping("/courses")
    public ApiResponse<PageResponse<CourseResponse>> joinedCourses(@RequestParam(defaultValue = "1") Integer pageNum,
                                                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("学生查询已加入课程: pageNum={}, pageSize={}", pageNum, pageSize);
        return ApiResponse.success(courseService.listJoinedCourses(pageNum, pageSize));
    }

    @PostMapping("/courses/join")
    public ApiResponse<CourseResponse> joinCourse(@RequestBody JoinCourseRequest request) {
        log.info("学生加入课程: inviteCode={}", request.getInviteCode());
        return ApiResponse.success(courseService.joinCourse(request));
    }

    @GetMapping("/courses/{courseId}")
    public ApiResponse<CourseDetailResponse> courseDetail(@PathVariable Integer courseId) {
        log.info("学生查询课程详情: courseId={}", courseId);
        return ApiResponse.success(courseService.getCourseDetail(courseId));
    }

    @GetMapping("/courses/{courseId}/sign-tasks")
    public ApiResponse<PageResponse<SignTaskResponse>> signTaskList(@PathVariable Integer courseId,
                                                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("学生查询签到任务列表: courseId={}, pageNum={}, pageSize={}", courseId, pageNum, pageSize);
        return ApiResponse.success(signTaskService.list(courseId, pageNum, pageSize));
    }

    @GetMapping("/sign-tasks/{taskId}")
    public ApiResponse<SignTaskResponse> signTaskDetail(@PathVariable Integer taskId) {
        log.info("学生查询签到任务详情: taskId={}", taskId);
        return ApiResponse.success(signTaskService.detail(taskId));
    }

    @GetMapping("/courses/{courseId}/active-sign-task")
    public ApiResponse<SignTaskResponse> activeSignTask(@PathVariable Integer courseId) {
        log.info("学生查询当前有效签到任务: courseId={}", courseId);
        return ApiResponse.success(signTaskService.activeTask(courseId));
    }

    @PostMapping("/sign-records")
    public ApiResponse<SignRecordResponse> submitSign(@RequestBody SubmitSignRequest request) {
        log.info("学生提交签到: taskId={}, location={}", request.getTaskId(), request.getLocation());
        return ApiResponse.success(signRecordService.submit(request));
    }

    @GetMapping("/sign-records/my-status/{taskId}")
    public ApiResponse<MySignStatusResponse> myStatus(@PathVariable Integer taskId) {
        log.info("学生查询本人签到状态: taskId={}", taskId);
        return ApiResponse.success(signRecordService.myStatus(taskId));
    }

    @GetMapping("/sign-records")
    public ApiResponse<PageResponse<SignRecordResponse>> myRecords(@RequestParam(required = false) Integer courseId,
                                                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("学生查询个人签到记录: courseId={}, pageNum={}, pageSize={}", courseId, pageNum, pageSize);
        return ApiResponse.success(signRecordService.listMine(courseId, pageNum, pageSize));
    }
}
