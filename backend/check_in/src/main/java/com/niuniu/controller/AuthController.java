package com.niuniu.controller;

import com.niuniu.common.ApiResponse;
import com.niuniu.dto.auth.LoginResponse;
import com.niuniu.dto.auth.WxLoginRequest;
import com.niuniu.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/wx-login")
    public ApiResponse<LoginResponse> wxLogin(@RequestBody WxLoginRequest request) {
        log.info("收到登录请求: phone={}", request.getPhone());
        return ApiResponse.success(authService.wxLogin(request));
    }
}
