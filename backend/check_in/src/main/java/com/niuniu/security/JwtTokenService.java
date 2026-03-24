package com.niuniu.security;

import com.niuniu.common.BusinessException;
import com.niuniu.common.UserType;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

    private final String secret;
    private final long expireSeconds;

    public JwtTokenService(@Value("${auth.jwt.secret}") String secret,
                           @Value("${auth.jwt.expire-seconds:604800}") long expireSeconds) {
        this.secret = secret;
        this.expireSeconds = expireSeconds;
    }

    public String issueToken(UserSession session) {
        long issuedAt = Instant.now().getEpochSecond();
        long expireAt = issuedAt + expireSeconds;
        String header = base64Url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payload = base64Url(buildPayload(session, issuedAt, expireAt));
        String signature = sign(header + "." + payload);
        return header + "." + payload + "." + signature;
    }

    public UserSession parseToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new BusinessException(401, "token 格式错误");
        }
        String signature = sign(parts[0] + "." + parts[1]);
        if (!MessageDigest.isEqual(signature.getBytes(StandardCharsets.UTF_8), parts[2].getBytes(StandardCharsets.UTF_8))) {
            throw new BusinessException(401, "token 签名无效");
        }
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        long exp = longValue(payload, "exp");
        System.out.println(exp);
        if (Instant.now().getEpochSecond() > exp) {
            throw new BusinessException(401, "token 已过期");
        }
        return UserSession.builder()
                .userId((int) longValue(payload, "userId"))
                .userNumber(stringValue(payload, "userNumber"))
                .name(stringValue(payload, "name"))
                .avatar(stringValue(payload, "avatar"))
                .userType(UserType.from(stringValue(payload, "userType")))
                .build();
    }

    private String buildPayload(UserSession session, long issuedAt, long expireAt) {
        return "{"
                + "\"userId\":" + session.getUserId() + ","
                + "\"userNumber\":\"" + escape(session.getUserNumber()) + "\","
                + "\"name\":\"" + escape(session.getName()) + "\","
                + "\"avatar\":\"" + escape(nullable(session.getAvatar())) + "\","
                + "\"userType\":\"" + session.getUserType().name().toLowerCase() + "\","
                + "\"iat\":" + issuedAt + ","
                + "\"exp\":" + expireAt
                + "}";
    }

    private String nullable(String value) {
        return value == null ? "" : value;
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String sign(String content) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(content.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("JWT 签名失败", ex);
        }
    }

    private String base64Url(String content) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(content.getBytes(StandardCharsets.UTF_8));
    }

    private long longValue(String json, String key) {
        String mark = "\"" + key + "\":";
        int start = json.indexOf(mark);
        if (start < 0) {
            throw new BusinessException(401, "token 缺少字段: " + key);
        }
        int from = start + mark.length();
        int end = from;
        while (end < json.length() && Character.isDigit(json.charAt(end))) {
            end++;
        }
        return Long.parseLong(json.substring(from, end));
    }

    private String stringValue(String json, String key) {
        String mark = "\"" + key + "\":\"";
        int start = json.indexOf(mark);
        if (start < 0) {
            throw new BusinessException(401, "token 缺少字段: " + key);
        }
        int from = start + mark.length();
        int end = from;
        StringBuilder builder = new StringBuilder();
        while (end < json.length()) {
            char c = json.charAt(end);
            if (c == '"' && json.charAt(end - 1) != '\\') {
                break;
            }
            builder.append(c);
            end++;
        }
        return builder.toString().replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
