package com.niuniu.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Teacher {
    private Integer id;
    private String openid;
    private String name;
    private String userNumber;
    private String avatar;
    private LocalDateTime createdAt;
    private String phone;
}
