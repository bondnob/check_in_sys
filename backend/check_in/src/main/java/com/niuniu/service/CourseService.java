package com.niuniu.service;

import com.niuniu.common.BusinessException;
import com.niuniu.common.UserType;
import com.niuniu.dto.common.PageResponse;
import com.niuniu.dto.course.CourseDetailResponse;
import com.niuniu.dto.course.CourseMemberResponse;
import com.niuniu.dto.course.CourseResponse;
import com.niuniu.dto.course.CreateCourseRequest;
import com.niuniu.dto.course.JoinCourseRequest;
import com.niuniu.dto.course.UpdateCourseRequest;
import com.niuniu.entity.Course;
import com.niuniu.entity.CourseMember;
import com.niuniu.mapper.CourseMapper;
import com.niuniu.mapper.CourseMemberMapper;
import com.niuniu.mapper.SignTaskMapper;
import com.niuniu.security.AuthContext;
import com.niuniu.security.UserSession;
import java.security.SecureRandom;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class CourseService {
    private static final String CODE_BASE = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private final SecureRandom random = new SecureRandom();

    private final CourseMapper courseMapper;
    private final CourseMemberMapper courseMemberMapper;
    private final SignTaskMapper signTaskMapper;

    public CourseService(CourseMapper courseMapper, CourseMemberMapper courseMemberMapper, SignTaskMapper signTaskMapper) {
        this.courseMapper = courseMapper;
        this.courseMemberMapper = courseMemberMapper;
        this.signTaskMapper = signTaskMapper;
    }

    public CourseResponse createCourse(CreateCourseRequest request) {
        UserSession session = require(UserType.TEACHER);
        if (!StringUtils.hasText(request.getCourseName()) || !StringUtils.hasText(request.getClassName())
                || !StringUtils.hasText(request.getTerm())) {
            throw new BusinessException(400, "courseName、className、term 不能为空");
        }
        Course course = new Course();
        course.setTeacherId(session.getUserNumber());
        course.setCourseName(request.getCourseName());
        course.setClassName(request.getClassName());
        course.setTerm(request.getTerm());
        course.setLocation(request.getLocation());
        course.setStuNumber(request.getStuNumber());
        course.setInviteCode(generateInviteCode());
        courseMapper.insert(course);
        log.info("课程创建成功: courseId={}, teacherNumber={}, inviteCode={}", course.getId(), session.getUserNumber(), course.getInviteCode());

        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setCourseName(course.getCourseName());
        response.setInviteCode(course.getInviteCode());
        response.setCreatedAt(course.getCreatedAt());
        return response;
    }

    public PageResponse<CourseResponse> listTeachingCourses(Integer pageNum, Integer pageSize, String keyword) {
        UserSession session = require(UserType.TEACHER);
        int offset = offset(pageNum, pageSize);
        List<CourseResponse> list = courseMapper.listTeaching(session.getUserNumber(), keyword, offset, pageSize);
        long total = courseMapper.countTeaching(session.getUserNumber(), keyword);
        log.info("教师课程列表查询完成: teacherNumber={}, total={}", session.getUserNumber(), total);
        return new PageResponse<>(list, total, pageNum, pageSize);
    }

    public PageResponse<CourseResponse> listJoinedCourses(Integer pageNum, Integer pageSize) {
        UserSession session = require(UserType.STUDENT);
        int offset = offset(pageNum, pageSize);
        List<CourseResponse> list = courseMapper.listJoined(session.getUserId(), offset, pageSize);
        long total = courseMapper.countJoined(session.getUserId());
        log.info("学生已加入课程列表查询完成: studentId={}, total={}", session.getUserId(), total);
        return new PageResponse<>(list, total, pageNum, pageSize);
    }

    public CourseDetailResponse getCourseDetail(Integer courseId) {
        CourseDetailResponse detail = courseMapper.detail(courseId);
        if (detail == null) {
            throw new BusinessException(404, "课程不存在");
        }
        ensureCourseRelated(courseId);
        log.info("课程详情查询成功: courseId={}", courseId);
        return detail;
    }

    public void updateCourse(Integer courseId, UpdateCourseRequest request) {
        UserSession session = require(UserType.TEACHER);
        Course course = requireOwnedCourse(courseId, session.getUserNumber());
        course.setCourseName(request.getCourseName());
        course.setClassName(request.getClassName());
        course.setTerm(request.getTerm());
        course.setLocation(request.getLocation());
        course.setStuNumber(request.getStuNumber());
        courseMapper.update(course);
        log.info("课程更新成功: courseId={}", courseId);
    }

    public void deleteCourse(Integer courseId) {
        UserSession session = require(UserType.TEACHER);
        requireOwnedCourse(courseId, session.getUserNumber());
        if (signTaskMapper.countByCourse(courseId) > 0) {
            throw new BusinessException(409, "课程下已有签到任务，不能直接删除");
        }
        courseMapper.delete(courseId);
        log.info("课程删除成功: courseId={}", courseId);
    }

    public CourseResponse joinCourse(JoinCourseRequest request) {
        UserSession session = require(UserType.STUDENT);
        if (!StringUtils.hasText(request.getInviteCode())) {
            throw new BusinessException(400, "inviteCode 不能为空");
        }
        Course course = courseMapper.findByInviteCode(request.getInviteCode());
        if (course == null) {
            throw new BusinessException(404, "邀请码不存在");
        }
        if (courseMemberMapper.findByCourseAndStudent(course.getId(), session.getUserId()) != null) {
            throw new BusinessException(409, "已加入该课程");
        }
        CourseMember member = new CourseMember();
        member.setCourseId(course.getId());
        member.setStudentId(session.getUserId());
        courseMemberMapper.insert(member);
        log.info("学生加入课程成功: studentId={}, courseId={}", session.getUserId(), course.getId());

        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setCourseName(course.getCourseName());
        response.setJoined(Boolean.TRUE);
        return response;
    }

    public PageResponse<CourseMemberResponse> listMembers(Integer courseId, Integer pageNum, Integer pageSize) {
        UserSession session = require(UserType.TEACHER);
        requireOwnedCourse(courseId, session.getUserNumber());
        int offset = offset(pageNum, pageSize);
        List<CourseMemberResponse> list = courseMemberMapper.listMembers(courseId, offset, pageSize);
        long total = courseMemberMapper.countMembers(courseId);
        log.info("课程成员列表查询完成: courseId={}, total={}", courseId, total);
        return new PageResponse<>(list, total, pageNum, pageSize);
    }

    public void removeMember(Integer courseId, Integer studentId) {
        UserSession session = require(UserType.TEACHER);
        requireOwnedCourse(courseId, session.getUserNumber());
        courseMemberMapper.delete(courseId, studentId);
        log.info("课程成员移除成功: courseId={}, studentId={}", courseId, studentId);
    }

    public void ensureCourseRelated(Integer courseId) {
        UserSession session = AuthContext.get();
        Course course = courseMapper.findById(courseId);
        if (course == null) {
            throw new BusinessException(404, "课程不存在");
        }
        if (session.getUserType() == UserType.TEACHER && session.getUserNumber().equals(course.getTeacherId())) {
            return;
        }
        if (session.getUserType() == UserType.STUDENT
                && courseMemberMapper.findByCourseAndStudent(courseId, session.getUserId()) != null) {
            return;
        }
        throw new BusinessException(403, "无权限访问该课程");
    }

    public Course requireOwnedCourse(Integer courseId, String teacherNumber) {
        Course course = courseMapper.findById(courseId);
        if (course == null) {
            throw new BusinessException(404, "课程不存在");
        }
        if (!teacherNumber.equals(course.getTeacherId())) {
            throw new BusinessException(403, "只能操作自己的课程");
        }
        return course;
    }

    public Course requireOwnedCourseOrRelated(Integer courseId) {
        ensureCourseRelated(courseId);
        return courseMapper.findById(courseId);
    }

    private UserSession require(UserType expected) {
        UserSession session = AuthContext.get();
        if (session == null || session.getUserType() != expected) {
            throw new BusinessException(403, "无权限");
        }
        return session;
    }

    private int offset(Integer pageNum, Integer pageSize) {
        return (Math.max(pageNum, 1) - 1) * pageSize;
    }

    private String generateInviteCode() {
        String code;
        do {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                builder.append(CODE_BASE.charAt(random.nextInt(CODE_BASE.length())));
            }
            code = builder.toString();
        } while (courseMapper.findByInviteCode(code) != null);
        return code;
    }
}
