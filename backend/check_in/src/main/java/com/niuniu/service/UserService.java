package com.niuniu.service;

import com.niuniu.common.BusinessException;
import com.niuniu.common.UserType;
import com.niuniu.dto.user.UpdateProfileRequest;
import com.niuniu.dto.user.UserProfileResponse;
import com.niuniu.entity.Student;
import com.niuniu.entity.Teacher;
import com.niuniu.mapper.StudentMapper;
import com.niuniu.mapper.TeacherMapper;
import com.niuniu.security.AuthContext;
import com.niuniu.security.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;

    public UserService(StudentMapper studentMapper, TeacherMapper teacherMapper) {
        this.studentMapper = studentMapper;
        this.teacherMapper = teacherMapper;
    }

    public UserProfileResponse me() {
        UserSession session = requireSession();
        log.info("查询当前用户信息: userId={}, userType={}", session.getUserId(), session.getUserType());
        UserProfileResponse response = new UserProfileResponse();
        response.setUserType(session.getUserType().name().toLowerCase());
        if (session.getUserType() == UserType.TEACHER) {
            Teacher teacher = teacherMapper.findById(session.getUserId());
            response.setId(teacher.getId());
            response.setUserNumber(teacher.getUserNumber());
            response.setName(teacher.getName());
            response.setAvatar(teacher.getAvatar());
            response.setPhone(teacher.getPhone());
            response.setCreatedAt(teacher.getCreatedAt());
        } else {
            Student student = studentMapper.findById(session.getUserId());
            response.setId(student.getId());
            response.setUserNumber(student.getUserNumber());
            response.setName(student.getName());
            response.setAvatar(student.getAvatar());
            response.setPhone(student.getPhone());
            response.setCreatedAt(student.getCreatedAt());
        }
        return response;
    }

    public void updateProfile(UpdateProfileRequest request) {
        UserSession session = requireSession();
        log.info("更新当前用户信息: userId={}, userType={}, name={}, phone={}",
                session.getUserId(), session.getUserType(), request.getName(), request.getPhone());
        if (session.getUserType() == UserType.TEACHER) {
            Teacher teacher = teacherMapper.findById(session.getUserId());
            teacher.setName(request.getName());
            teacher.setAvatar(request.getAvatar());
            teacher.setPhone(request.getPhone());
            teacherMapper.update(teacher);
        } else {
            Student student = studentMapper.findById(session.getUserId());
            student.setName(request.getName());
            student.setAvatar(request.getAvatar());
            student.setPhone(request.getPhone());
            studentMapper.update(student);
        }
    }

    private UserSession requireSession() {
        UserSession session = AuthContext.get();
        if (session == null) {
            throw new BusinessException(401, "未登录");
        }
        return session;
    }
}
