package com.niuniu.dto.course;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CourseDetailResponse {
    private Integer id;
    private String courseName;
    private String className;
    private String term;
    private String location;
    private String inviteCode;
    private Integer stuNumber;
    private String teacherId;
    private String teacherName;
    private Integer memberCount;
    private LocalDateTime createdAt;
}
