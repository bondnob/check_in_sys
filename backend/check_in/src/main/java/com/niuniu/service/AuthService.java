package com.niuniu.service;

import com.niuniu.common.BusinessException;
import com.niuniu.common.UserType;
import com.niuniu.dto.auth.LoginResponse;
import com.niuniu.dto.auth.WxLoginRequest;
import com.niuniu.entity.Student;
import com.niuniu.entity.Teacher;
import com.niuniu.mapper.StudentMapper;
import com.niuniu.mapper.TeacherMapper;
import com.niuniu.security.JwtTokenService;
import com.niuniu.security.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class AuthService {

    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;
    private final JwtTokenService tokenService;

    public AuthService(StudentMapper studentMapper, TeacherMapper teacherMapper, JwtTokenService tokenService) {
        this.studentMapper = studentMapper;
        this.teacherMapper = teacherMapper;
        this.tokenService = tokenService;
    }

    public LoginResponse wxLogin(WxLoginRequest request) {
        if (!StringUtils.hasText(request.getPhone())) {
            throw new BusinessException(400, "phone 不能为空");
        }
        log.info("开始登录: phone={}", request.getPhone());
        Teacher teacher = teacherMapper.findByPhone(request.getPhone().trim());
        if (teacher != null) {
            log.info("登录命中教师: teacherId={}, userNumber={}", teacher.getId(), teacher.getUserNumber());
            return issueLogin(teacher.getId(), teacher.getUserNumber(), teacher.getName(), teacher.getAvatar(), UserType.TEACHER, false);
        }
        Student student = studentMapper.findByPhone(request.getPhone().trim());
        if (student != null) {
            log.info("登录命中学生: studentId={}, userNumber={}", student.getId(), student.getUserNumber());
            return issueLogin(student.getId(), student.getUserNumber(), student.getName(), student.getAvatar(), UserType.STUDENT, false);
        }
        log.warn("登录失败: 查无此人, phone={}", request.getPhone());
        throw new BusinessException(404, "查无此人");
    }

    private LoginResponse issueLogin(Integer userId, String userNumber, String name, String avatar, UserType userType,
                                     Boolean isNewUser) {
        UserSession session = UserSession.builder()
                .userId(userId)
                .userNumber(userNumber)
                .name(name)
                .avatar(avatar)
                .userType(userType)
                .build();
        String token = tokenService.issueToken(session);
        log.info("签发token成功: userId={}, userType={}", userId, userType);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserType(userType.name().toLowerCase());
        response.setUserId(userId);
        response.setUserNumber(userNumber);
        response.setName(name);
        response.setAvatar(avatar);
        response.setNewUser(isNewUser);
        return response;
    }
}
