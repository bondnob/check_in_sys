package com.niuniu.dto.statistics;

import lombok.Data;

@Data
public class StudentAttendanceResponse {
    private Integer studentId;
    private String studentNumber;
    private String studentName;
    private Integer signedCount;
    private Integer unsignedCount;
    private Double attendanceRate;
}
