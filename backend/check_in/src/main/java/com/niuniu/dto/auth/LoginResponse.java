package com.niuniu.dto.auth;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String userType;
    private Integer userId;
    private String userNumber;
    private String name;
    private String avatar;
    private Boolean newUser;
}
