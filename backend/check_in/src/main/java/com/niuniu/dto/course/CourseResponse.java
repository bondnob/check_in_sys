package com.niuniu.dto.course;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CourseResponse {
    private Integer id;
    private String courseName;
    private String className;
    private String term;
    private String location;
    private String inviteCode;
    private Integer stuNumber;
    private Integer actualMemberCount;
    private String teacherName;
    private LocalDateTime joinedAt;
    private LocalDateTime createdAt;
    private Boolean joined;
}
