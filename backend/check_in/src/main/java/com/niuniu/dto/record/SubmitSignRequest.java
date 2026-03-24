package com.niuniu.dto.record;

import lombok.Data;

@Data
public class SubmitSignRequest {
    private Integer taskId;
    private String qrCode;
    private String location;
    private Double latitude;
    private Double longitude;
}
