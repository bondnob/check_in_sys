package com.niuniu.dto.course;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CourseMemberResponse {
    private Integer memberId;
    private Integer studentId;
    private String studentNumber;
    private String studentName;
    private String avatar;
    private String phone;
    private LocalDateTime joinedAt;
}
