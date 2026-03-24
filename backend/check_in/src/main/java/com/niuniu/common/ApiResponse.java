package com.niuniu.common;

public record ApiResponse<T>(Integer code, String message, T data) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(200, "success", null);
    }

    public static ApiResponse<Void> fail(Integer code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
