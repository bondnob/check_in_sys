package com.niuniu.dto.record;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MySignStatusResponse {
    private Boolean signed;
    private Integer recordId;
    private LocalDateTime signTime;
    private Integer status;
    private String location;
    private Double latitude;
    private Double longitude;
}
