package com.niuniu.dto.user;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserProfileResponse {
    private Integer id;
    private String userType;
    private String userNumber;
    private String name;
    private String avatar;
    private String phone;
    private LocalDateTime createdAt;
}
