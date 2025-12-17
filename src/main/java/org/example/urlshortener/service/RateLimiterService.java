package org.example.urlshortener.service;

import org.example.urlshortener.exception.RateLimitException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {

    private static final int MAX_REQUESTS = 10;
    private static final Duration WINDOW = Duration.ofMinutes(1);

    private final StringRedisTemplate redisTemplate;

    public RateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void checkRateLimit(String key) {
        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count == 1) {
            // first request → set TTL
            redisTemplate.expire(key, WINDOW);
        }

        if (count != null && count > MAX_REQUESTS) {
            throw new RateLimitException("Too many requests. Try again later.");
        }
    }
}
