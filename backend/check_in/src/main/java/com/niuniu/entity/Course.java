package com.niuniu.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Course {
    private Integer id;
    private String teacherId;
    private String courseName;
    private String inviteCode;
    private String location;
    private LocalDateTime createdAt;
    private String className;
    private Integer stuNumber;
    private String term;
}
