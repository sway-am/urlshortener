package org.example.urlshortener.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generateShortCode(String input, int length) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));

            return base62Encode(hash).substring(0, length);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    private static String base62Encode(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        long value = 0;

        for (byte b : bytes) {
            value = (value << 8) | (b & 0xff);
            while (value >= 62) {
                sb.append(BASE62.charAt((int) (value % 62)));
                value /= 62;
            }
        }

        if (value > 0) {
            sb.append(BASE62.charAt((int) value));
        }

        return sb.toString();
    }
}
