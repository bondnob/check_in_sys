package com.niuniu.service;

import com.niuniu.common.BusinessException;
import com.niuniu.common.UserType;
import com.niuniu.dto.common.PageResponse;
import com.niuniu.dto.task.CreateSignTaskRequest;
import com.niuniu.dto.task.SignTaskResponse;
import com.niuniu.dto.task.UpdateSignTaskRequest;
import com.niuniu.entity.Course;
import com.niuniu.entity.SignTask;
import com.niuniu.mapper.CourseMemberMapper;
import com.niuniu.mapper.SignRecordMapper;
import com.niuniu.mapper.SignTaskMapper;
import com.niuniu.security.AuthContext;
import com.niuniu.security.UserSession;
import com.niuniu.util.QrCodeTokenUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class SignTaskService {

    private final SignTaskMapper signTaskMapper;
    private final CourseMemberMapper courseMemberMapper;
    private final SignRecordMapper signRecordMapper;
    private final CourseService courseService;

    public SignTaskService(SignTaskMapper signTaskMapper, CourseMemberMapper courseMemberMapper,
                           SignRecordMapper signRecordMapper, CourseService courseService) {
        this.signTaskMapper = signTaskMapper;
        this.courseMemberMapper = courseMemberMapper;
        this.signRecordMapper = signRecordMapper;
        this.courseService = courseService;
    }

    public SignTaskResponse create(CreateSignTaskRequest request) {
        UserSession session = require(UserType.TEACHER);
        validateTime(request.getStartTime(), request.getEndTime());
        validateRequest(request);
        Course course = courseService.requireOwnedCourse(request.getCourseId(), session.getUserNumber());
        SignTask task = new SignTask();
        task.setCourseId(request.getCourseId());
        task.setTitle(request.getTitle());
        task.setStartTime(request.getStartTime());
        task.setEndTime(request.getEndTime());
        task.setSignType(request.getSignType());
        task.setQrCode(request.getQrCode());
        task.setLatitude(request.getLatitude());
        task.setLongitude(request.getLongitude());
        task.setRadius(request.getRadius());
        task.setLateTime(request.getLateTime());
        signTaskMapper.insert(task);
        log.info("签到任务创建成功: taskId={}, courseId={}, signType={}", task.getId(), task.getCourseId(), task.getSignType());
        return buildResponse(task, course.getCourseName());
    }

    public PageResponse<SignTaskResponse> list(Integer courseId, Integer pageNum, Integer pageSize) {
        Course course = courseService.requireOwnedCourseOrRelated(courseId);
        int offset = (Math.max(pageNum, 1) - 1) * pageSize;
        List<SignTask> tasks = signTaskMapper.listByCourse(courseId, offset, pageSize);
        List<SignTaskResponse> list = new ArrayList<>();
        for (SignTask task : tasks) {
            list.add(buildResponse(task, course.getCourseName()));
        }
        long total = signTaskMapper.countByCourse(courseId);
        log.info("签到任务列表查询完成: courseId={}, total={}", courseId, total);
        return new PageResponse<>(list, total, pageNum, pageSize);
    }

    public SignTaskResponse detail(Integer taskId) {
        SignTask task = requireTask(taskId);
        Course course = courseService.requireOwnedCourseOrRelated(task.getCourseId());
        log.info("签到任务详情查询成功: taskId={}", taskId);
        return buildResponse(task, course.getCourseName());
    }

    public void update(Integer taskId, UpdateSignTaskRequest request) {
        UserSession session = require(UserType.TEACHER);
        SignTask task = requireTask(taskId);
        courseService.requireOwnedCourse(task.getCourseId(), session.getUserNumber());
        validateTime(request.getStartTime(), request.getEndTime());
        validateRequest(request);
        task.setTitle(request.getTitle());
        task.setStartTime(request.getStartTime());
        task.setEndTime(request.getEndTime());
        task.setSignType(request.getSignType());
        task.setQrCode(request.getQrCode());
        task.setLatitude(request.getLatitude());
        task.setLongitude(request.getLongitude());
        task.setRadius(request.getRadius());
        task.setLateTime(request.getLateTime());
        signTaskMapper.update(task);
        log.info("签到任务更新成功: taskId={}", taskId);
    }

    public void delete(Integer taskId) {
        UserSession session = require(UserType.TEACHER);
        SignTask task = requireTask(taskId);
        courseService.requireOwnedCourse(task.getCourseId(), session.getUserNumber());
        if (signRecordMapper.countByTaskId(taskId) > 0) {
            throw new BusinessException(409, "任务下已有签到记录，不能删除");
        }
        signTaskMapper.delete(taskId);
        log.info("签到任务删除成功: taskId={}", taskId);
    }

    public SignTaskResponse activeTask(Integer courseId) {
        Course course = courseService.requireOwnedCourseOrRelated(courseId);
        SignTask task = signTaskMapper.activeByCourse(courseId);
        if (task == null) {
            log.info("当前无有效签到任务: courseId={}", courseId);
            SignTaskResponse response = new SignTaskResponse();
            response.setHasActiveTask(Boolean.FALSE);
            return response;
        }
        log.info("查询到当前有效签到任务: courseId={}, taskId={}", courseId, task.getId());
        SignTaskResponse response = buildResponse(task, course.getCourseName());
        response.setHasActiveTask(Boolean.TRUE);
        return response;
    }

    private SignTaskResponse buildResponse(SignTask task, String courseName) {
        SignTaskResponse response = new SignTaskResponse();
        response.setId(task.getId());
        response.setCourseId(task.getCourseId());
        response.setCourseName(courseName);
        response.setTitle(task.getTitle());
        response.setStartTime(task.getStartTime());
        response.setEndTime(task.getEndTime());
        response.setSignType(task.getSignType());
        response.setQrCode(resolveQrCode(task));
        response.setLatitude(task.getLatitude());
        response.setLongitude(task.getLongitude());
        response.setRadius(task.getRadius());
        response.setLateTime(task.getLateTime());
        response.setCreatedAt(task.getCreatedAt());
        response.setTaskState(resolveState(task));
        int signed = signRecordMapper.countByTaskId(task.getId());
        int members = courseMemberMapper.countByCourseId(task.getCourseId());
        response.setSignedCount(signed);
        response.setUnsignedCount(Math.max(members - signed, 0));
        return response;
    }

    private String resolveQrCode(SignTask task) {
        if (task.getSignType() == null || task.getSignType() != 1 || !StringUtils.hasText(task.getQrCode())) {
            return task.getQrCode();
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(task.getStartTime())) {
            now = task.getStartTime();
        }
        if (now.isAfter(task.getEndTime())) {
            now = task.getEndTime();
        }
        String dynamicCode = QrCodeTokenUtil.currentToken(task.getId(), task.getQrCode(), now);
        log.debug("生成动态二维码: taskId={}, windowCode={}", task.getId(), dynamicCode);
        return dynamicCode;
    }

    private String resolveState(SignTask task) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(task.getStartTime())) {
            return "not_started";
        }
        if (now.isAfter(task.getEndTime())) {
            return "finished";
        }
        return "ongoing";
    }

    private void validateTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null || !startTime.isBefore(endTime)) {
            throw new BusinessException(400, "startTime 必须早于 endTime");
        }
    }

    private void validateRequest(CreateSignTaskRequest request) {
        validateSignConfig(request.getSignType(), request.getQrCode(), request.getLatitude(), request.getLongitude(), request.getRadius());
        validateLateTime(request.getStartTime(), request.getEndTime(), request.getLateTime());
    }

    private void validateRequest(UpdateSignTaskRequest request) {
        validateSignConfig(request.getSignType(), request.getQrCode(), request.getLatitude(), request.getLongitude(), request.getRadius());
        validateLateTime(request.getStartTime(), request.getEndTime(), request.getLateTime());
    }

    private void validateSignConfig(Integer signType, String qrCode, Double latitude, Double longitude, Integer radius) {
        if (signType == null || (signType != 0 && signType != 1)) {
            throw new BusinessException(400, "signType 只能是 0 或 1");
        }
        if (signType == 1 && !StringUtils.hasText(qrCode)) {
            throw new BusinessException(400, "二维码签到必须提供 qrCode");
        }
        if (signType == 0) {
            if (latitude == null || longitude == null || radius == null || radius <= 0) {
                throw new BusinessException(400, "定位签到必须提供 latitude、longitude、radius");
            }
        }
    }

    private void validateLateTime(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime lateTime) {
        if (lateTime != null && (lateTime.isBefore(startTime) || lateTime.isAfter(endTime))) {
            throw new BusinessException(400, "lateTime 必须在签到开始和结束时间之间");
        }
    }

    private SignTask requireTask(Integer taskId) {
        SignTask task = signTaskMapper.findById(taskId);
        if (task == null) {
            throw new BusinessException(404, "签到任务不存在");
        }
        return task;
    }

    private UserSession require(UserType expected) {
        UserSession session = AuthContext.get();
        if (session == null || session.getUserType() != expected) {
            throw new BusinessException(403, "无权限");
        }
        return session;
    }
}
