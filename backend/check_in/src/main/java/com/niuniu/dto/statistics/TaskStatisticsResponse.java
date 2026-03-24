package com.niuniu.dto.statistics;

import lombok.Data;

@Data
public class TaskStatisticsResponse {
    private Integer taskId;
    private String title;
    private Integer memberCount;
    private Integer signedCount;
    private Integer unsignedCount;
    private Double attendanceRate;
    private Integer lateCount;
}
