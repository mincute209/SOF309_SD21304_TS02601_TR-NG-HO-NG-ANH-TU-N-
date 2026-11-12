package com.jolliebear.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * Băm mật khẩu bằng SHA-256 + salt ngẫu nhiên.
 * Định dạng lưu trong DB: "saltHex:hashHex".
 * Vẫn tương thích ngược với mật khẩu cũ dạng plaintext (không chứa ':').
 */
public class PasswordUtil {
    private static final SecureRandom RANDOM = new SecureRandom();

    // Tạo chuỗi băm mới từ mật khẩu gốc
    public static String hash(String password) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        String saltHex = toHex(salt);
        return saltHex + ":" + sha256(saltHex + password);
    }

    // Kiểm tra mật khẩu nhập vào có khớp chuỗi đã lưu không
    public static boolean verify(String password, String stored) {
        if (stored == null || password == null) return false;
        int idx = stored.indexOf(':');
        if (idx < 0) {
            // Mật khẩu cũ dạng plaintext -> so sánh trực tiếp (tương thích ngược)
            return stored.equals(password);
        }
        String saltHex = stored.substring(0, idx);
        String hash = stored.substring(idx + 1);
        return sha256(saltHex + password).equalsIgnoreCase(hash);
    }

    public static boolean isHashed(String stored) {
        return stored != null && stored.indexOf(':') >= 0;
    }

    private static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return toHex(md.digest(input.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
