package com.niuniu.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SignRecord {
    private Integer id;
    private Integer taskId;
    private Integer studentId;
    private LocalDateTime signTime;
    private Integer status;
    private String location;
    private Double latitude;
    private Double longitude;
}
