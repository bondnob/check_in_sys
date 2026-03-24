package com.niuniu.dto.task;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SignTaskResponse {
    private Boolean hasActiveTask;
    private Integer id;
    private Integer courseId;
    private String courseName;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer signType;
    private String qrCode;
    private Double latitude;
    private Double longitude;
    private Integer radius;
    private LocalDateTime lateTime;
    private LocalDateTime createdAt;
    private String taskState;
    private Integer signedCount;
    private Integer unsignedCount;
}
