package com.niuniu.common;

public enum UserType {
    TEACHER,
    STUDENT;

    public static UserType from(String value) {
        try {
            return UserType.valueOf(value.trim().toUpperCase());
        } catch (Exception ex) {
            throw new BusinessException(400, "userType 只能是 teacher 或 student");
        }
    }
}
