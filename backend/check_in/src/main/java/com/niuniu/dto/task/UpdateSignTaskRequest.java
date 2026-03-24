package com.niuniu.dto.task;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UpdateSignTaskRequest {
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer signType;
    private String qrCode;
    private Double latitude;
    private Double longitude;
    private Integer radius;
    private LocalDateTime lateTime;
}
