package com.niuniu.dto.record;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SignRecordResponse {
    private Integer recordId;
    private Integer taskId;
    private Integer courseId;
    private String courseName;
    private String title;
    private Integer studentId;
    private String studentNumber;
    private String studentName;
    private LocalDateTime signTime;
    private Integer status;
    private String location;
    private Double latitude;
    private Double longitude;
}
