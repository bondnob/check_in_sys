package com.niuniu.service;

import com.niuniu.common.BusinessException;
import com.niuniu.common.UserType;
import com.niuniu.dto.common.PageResponse;
import com.niuniu.dto.statistics.CourseStatisticsResponse;
import com.niuniu.dto.statistics.StudentAttendanceResponse;
import com.niuniu.dto.statistics.TaskStatisticsResponse;
import com.niuniu.entity.Course;
import com.niuniu.entity.SignTask;
import com.niuniu.mapper.CourseMemberMapper;
import com.niuniu.mapper.SignRecordMapper;
import com.niuniu.mapper.SignTaskMapper;
import com.niuniu.mapper.StudentMapper;
import com.niuniu.security.AuthContext;
import com.niuniu.security.UserSession;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatisticsService {

    private final CourseMemberMapper courseMemberMapper;
    private final SignTaskMapper signTaskMapper;
    private final SignRecordMapper signRecordMapper;
    private final StudentMapper studentMapper;
    private final CourseService courseService;

    public StatisticsService(CourseMemberMapper courseMemberMapper, SignTaskMapper signTaskMapper,
                             SignRecordMapper signRecordMapper, StudentMapper studentMapper, CourseService courseService) {
        this.courseMemberMapper = courseMemberMapper;
        this.signTaskMapper = signTaskMapper;
        this.signRecordMapper = signRecordMapper;
        this.studentMapper = studentMapper;
        this.courseService = courseService;
    }

    public CourseStatisticsResponse courseStatistics(Integer courseId) {
        UserSession session = requireTeacher();
        Course course = courseService.requireOwnedCourse(courseId, session.getUserNumber());
        int memberCount = courseMemberMapper.countByCourseId(courseId);
        int taskCount = signTaskMapper.countTasksByCourse(courseId);
        int totalSigned = signRecordMapper.countByCourseId(courseId);

        CourseStatisticsResponse response = new CourseStatisticsResponse();
        response.setCourseId(courseId);
        response.setCourseName(course.getCourseName());
        response.setMemberCount(memberCount);
        response.setTaskCount(taskCount);
        response.setTotalSignedCount(totalSigned);
        double rate = memberCount == 0 || taskCount == 0 ? 0D : (totalSigned * 100.0) / (memberCount * taskCount);
        response.setAverageAttendanceRate(round(rate));
        log.info("课程统计完成: courseId={}, memberCount={}, taskCount={}, totalSigned={}", courseId, memberCount, taskCount, totalSigned);
        return response;
    }

    public TaskStatisticsResponse taskStatistics(Integer taskId) {
        UserSession session = requireTeacher();
        SignTask task = signTaskMapper.findById(taskId);
        if (task == null) {
            throw new BusinessException(404, "签到任务不存在");
        }
        courseService.requireOwnedCourse(task.getCourseId(), session.getUserNumber());
        int memberCount = courseMemberMapper.countByCourseId(task.getCourseId());
        int signedCount = signRecordMapper.countByTaskId(taskId);

        TaskStatisticsResponse response = new TaskStatisticsResponse();
        response.setTaskId(taskId);
        response.setTitle(task.getTitle());
        response.setMemberCount(memberCount);
        response.setSignedCount(signedCount);
        response.setUnsignedCount(Math.max(memberCount - signedCount, 0));
        response.setAttendanceRate(memberCount == 0 ? 0D : round(signedCount * 100.0 / memberCount));
        response.setLateCount(signRecordMapper.countLateByTaskId(taskId));
        log.info("任务统计完成: taskId={}, memberCount={}, signedCount={}", taskId, memberCount, signedCount);
        return response;
    }

    public PageResponse<StudentAttendanceResponse> studentStatistics(Integer courseId, Integer pageNum, Integer pageSize) {
        UserSession session = requireTeacher();
        courseService.requireOwnedCourse(courseId, session.getUserNumber());
        int offset = (Math.max(pageNum, 1) - 1) * pageSize;
        List<StudentAttendanceResponse> list = studentMapper.studentAttendance(courseId, offset, pageSize);
        long total = courseMemberMapper.countMembers(courseId);
        log.info("学生出勤统计完成: courseId={}, total={}", courseId, total);
        return new PageResponse<>(list, total, pageNum, pageSize);
    }

    private UserSession requireTeacher() {
        UserSession session = AuthContext.get();
        if (session == null || session.getUserType() != UserType.TEACHER) {
            throw new BusinessException(403, "无权限");
        }
        return session;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
