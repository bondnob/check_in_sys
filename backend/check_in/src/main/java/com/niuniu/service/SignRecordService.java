package com.niuniu.service;

import com.niuniu.common.BusinessException;
import com.niuniu.common.UserType;
import com.niuniu.dto.common.PageResponse;
import com.niuniu.dto.record.MySignStatusResponse;
import com.niuniu.dto.record.SignRecordResponse;
import com.niuniu.dto.record.SubmitSignRequest;
import com.niuniu.entity.CourseMember;
import com.niuniu.entity.SignRecord;
import com.niuniu.entity.SignTask;
import com.niuniu.mapper.CourseMemberMapper;
import com.niuniu.mapper.SignRecordMapper;
import com.niuniu.mapper.SignTaskMapper;
import com.niuniu.security.AuthContext;
import com.niuniu.security.UserSession;
import com.niuniu.util.QrCodeTokenUtil;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class SignRecordService {

    private final SignRecordMapper signRecordMapper;
    private final SignTaskMapper signTaskMapper;
    private final CourseMemberMapper courseMemberMapper;
    private final CourseService courseService;

    public SignRecordService(SignRecordMapper signRecordMapper, SignTaskMapper signTaskMapper,
                             CourseMemberMapper courseMemberMapper, CourseService courseService) {
        this.signRecordMapper = signRecordMapper;
        this.signTaskMapper = signTaskMapper;
        this.courseMemberMapper = courseMemberMapper;
        this.courseService = courseService;
    }

    public SignRecordResponse submit(SubmitSignRequest request) {
        UserSession session = require(UserType.STUDENT);
        if (request.getTaskId() == null) {
            throw new BusinessException(400, "taskId 不能为空");
        }
        log.info("开始处理签到: studentId={}, taskId={}", session.getUserId(), request.getTaskId());
        SignTask task = requireTask(request.getTaskId());
        CourseMember member = courseMemberMapper.findByCourseAndStudent(task.getCourseId(), session.getUserId());
        if (member == null) {
            throw new BusinessException(403, "你不在该课程中");
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(task.getStartTime()) || now.isAfter(task.getEndTime())) {
            throw new BusinessException(409, "当前不在签到时间范围内");
        }
        if (signRecordMapper.findByTaskAndStudent(task.getId(), session.getUserId()) != null) {
            throw new BusinessException(409, "当前签到任务已签到");
        }
        SignRecord record = new SignRecord();
        record.setTaskId(task.getId());
        record.setStudentId(session.getUserId());
        record.setSignTime(now);
        validateSignMethod(task, request);
        record.setStatus(resolveStatus(task, now));
        record.setLocation(request.getLocation());
        record.setLatitude(request.getLatitude());
        record.setLongitude(request.getLongitude());
        signRecordMapper.insert(record);
        log.info("签到成功: recordId={}, taskId={}, studentId={}, status={}", record.getId(), record.getTaskId(), record.getStudentId(), record.getStatus());

        SignRecordResponse response = new SignRecordResponse();
        response.setRecordId(record.getId());
        response.setTaskId(record.getTaskId());
        response.setStudentId(record.getStudentId());
        response.setSignTime(record.getSignTime());
        response.setStatus(record.getStatus());
        response.setLocation(record.getLocation());
        response.setLatitude(record.getLatitude());
        response.setLongitude(record.getLongitude());
        return response;
    }

    public MySignStatusResponse myStatus(Integer taskId) {
        UserSession session = require(UserType.STUDENT);
        SignRecord record = signRecordMapper.findByTaskAndStudent(taskId, session.getUserId());
        log.info("查询本人签到状态: taskId={}, studentId={}, signed={}", taskId, session.getUserId(), record != null);
        MySignStatusResponse response = new MySignStatusResponse();
        response.setSigned(record != null);
        if (record != null) {
            response.setRecordId(record.getId());
            response.setSignTime(record.getSignTime());
            response.setStatus(record.getStatus());
            response.setLocation(record.getLocation());
            response.setLatitude(record.getLatitude());
            response.setLongitude(record.getLongitude());
        }
        return response;
    }

    public PageResponse<SignRecordResponse> listByTask(Integer taskId, Integer status, String keyword, Integer pageNum, Integer pageSize) {
        UserSession session = require(UserType.TEACHER);
        SignTask task = requireTask(taskId);
        courseService.requireOwnedCourse(task.getCourseId(), session.getUserNumber());
        int offset = (Math.max(pageNum, 1) - 1) * pageSize;
        List<SignRecordResponse> list = signRecordMapper.listByTask(taskId, status, keyword, offset, pageSize);
        long total = signRecordMapper.countByTask(taskId, status, keyword);
        log.info("教师查询签到记录完成: taskId={}, total={}", taskId, total);
        return new PageResponse<>(list, total, pageNum, pageSize);
    }

    public PageResponse<SignRecordResponse> listUnsigned(Integer taskId, Integer pageNum, Integer pageSize) {
        UserSession session = require(UserType.TEACHER);
        SignTask task = requireTask(taskId);
        courseService.requireOwnedCourse(task.getCourseId(), session.getUserNumber());
        int offset = (Math.max(pageNum, 1) - 1) * pageSize;
        List<SignRecordResponse> list = signRecordMapper.listUnsigned(taskId, offset, pageSize);
        long total = signRecordMapper.countUnsigned(taskId);
        log.info("教师查询未签到名单完成: taskId={}, total={}", taskId, total);
        return new PageResponse<>(list, total, pageNum, pageSize);
    }

    public PageResponse<SignRecordResponse> listMine(Integer courseId, Integer pageNum, Integer pageSize) {
        UserSession session = require(UserType.STUDENT);
        int offset = (Math.max(pageNum, 1) - 1) * pageSize;
        List<SignRecordResponse> list = signRecordMapper.listMine(session.getUserId(), courseId, offset, pageSize);
        long total = signRecordMapper.countMine(session.getUserId(), courseId);
        log.info("学生查询个人签到记录完成: studentId={}, total={}", session.getUserId(), total);
        return new PageResponse<>(list, total, pageNum, pageSize);
    }

    private SignTask requireTask(Integer taskId) {
        SignTask task = signTaskMapper.findById(taskId);
        if (task == null) {
            throw new BusinessException(404, "签到任务不存在");
        }
        return task;
    }

    private void validateSignMethod(SignTask task, SubmitSignRequest request) {
        if (task.getSignType() == 1) {
            if (!StringUtils.hasText(request.getQrCode())
                    || !StringUtils.hasText(task.getQrCode())
                    || !QrCodeTokenUtil.isCurrentToken(task.getId(), task.getQrCode(), request.getQrCode(), LocalDateTime.now())) {
                log.warn("二维码校验失败: taskId={}, studentQrCode={}", task.getId(), request.getQrCode());
                throw new BusinessException(409, "二维码无效");
            }
            log.info("二维码校验通过: taskId={}", task.getId());
            return;
        }
        if (task.getSignType() == 0) {
            if (request.getLatitude() == null || request.getLongitude() == null) {
                throw new BusinessException(400, "定位签到必须提交经纬度");
            }
            if (task.getLatitude() == null || task.getLongitude() == null || task.getRadius() == null) {
                throw new BusinessException(500, "签到任务未配置定位参数");
            }
            double distance = distanceMeters(task.getLatitude(), task.getLongitude(), request.getLatitude(), request.getLongitude());
            if (distance > task.getRadius()) {
                log.warn("定位签到超出范围: taskId={}, distance={}, radius={}", task.getId(), distance, task.getRadius());
                throw new BusinessException(409, "超出允许签到范围");
            }
            log.info("定位签到校验通过: taskId={}, distance={}, radius={}", task.getId(), distance, task.getRadius());
        }
    }

    private int resolveStatus(SignTask task, LocalDateTime now) {
        if (task.getLateTime() != null && now.isAfter(task.getLateTime())) {
            return 2;
        }
        return 1;
    }

    private double distanceMeters(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371000D;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    private UserSession require(UserType expected) {
        UserSession session = AuthContext.get();
        if (session == null || session.getUserType() != expected) {
            throw new BusinessException(403, "无权限");
        }
        return session;
    }
}
