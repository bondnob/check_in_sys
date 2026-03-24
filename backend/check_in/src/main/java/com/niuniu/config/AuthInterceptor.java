package com.niuniu.config;

import com.niuniu.common.BusinessException;
import com.niuniu.security.AuthContext;
import com.niuniu.security.JwtTokenService;
import com.niuniu.security.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenService tokenService;

    public AuthInterceptor(JwtTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || authorization.isBlank()) {
            log.warn("鉴权失败: path={}, reason=missing_token", request.getRequestURI());
            throw new BusinessException(401, "未登录或 token 缺失");
        }
        String token = authorization.startsWith("Bearer ") ? authorization.substring(7) : authorization.trim();
        UserSession session = tokenService.parseToken(token);
        log.info("鉴权成功: path={}, userId={}, userType={}", request.getRequestURI(), session.getUserId(), session.getUserType());
        AuthContext.set(session);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AuthContext.clear();
    }
}
