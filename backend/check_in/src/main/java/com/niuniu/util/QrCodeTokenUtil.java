package com.niuniu.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class QrCodeTokenUtil {
    private static final long WINDOW_SECONDS = 600L;

    private QrCodeTokenUtil() {
    }

    public static String currentToken(Integer taskId, String baseCode, LocalDateTime now) {
        long slot = slot(now);
        return buildToken(taskId, baseCode, slot);
    }

    public static boolean isCurrentToken(Integer taskId, String baseCode, String qrCode, LocalDateTime now) {
        return buildToken(taskId, baseCode, slot(now)).equals(qrCode);
    }

    private static long slot(LocalDateTime time) {
        return time.toEpochSecond(ZoneOffset.ofHours(8)) / WINDOW_SECONDS;
    }

    private static String buildToken(Integer taskId, String baseCode, long slot) {
        return "QR-" + taskId + "-" + shortSha256(taskId + "|" + baseCode + "|" + slot);
    }

    private static String shortSha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                builder.append(String.format("%02x", hash[i]));
            }
            return builder.toString().toUpperCase();
        } catch (Exception ex) {
            throw new IllegalStateException("生成二维码口令失败", ex);
        }
    }
}
