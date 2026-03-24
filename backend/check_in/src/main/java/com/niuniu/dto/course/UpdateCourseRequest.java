package com.niuniu.dto.course;

import lombok.Data;

@Data
public class UpdateCourseRequest {
    private String courseName;
    private String className;
    private String term;
    private String location;
    private Integer stuNumber;
}
