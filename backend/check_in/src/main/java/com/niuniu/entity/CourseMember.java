package com.niuniu.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CourseMember {
    private Integer id;
    private Integer courseId;
    private Integer studentId;
    private LocalDateTime joinedAt;
}
