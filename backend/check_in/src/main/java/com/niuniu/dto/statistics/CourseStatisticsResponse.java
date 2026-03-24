package com.niuniu.dto.statistics;

import lombok.Data;

@Data
public class CourseStatisticsResponse {
    private Integer courseId;
    private String courseName;
    private Integer memberCount;
    private Integer taskCount;
    private Integer totalSignedCount;
    private Double averageAttendanceRate;
}
