package com.niuniu.security;

import com.niuniu.common.UserType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSession {
    private Integer userId;
    private String userNumber;
    private String name;
    private String avatar;
    private UserType userType;
}
