package org.example.urlshortener.dto;

import java.time.LocalDateTime;

public class UrlStatsResponse {

    private String originalUrl;
    private String shortUrl;
    private long clickCount;
    private LocalDateTime createdAt;
    private LocalDateTime expireAt;

    public UrlStatsResponse(
            String originalUrl,
            String shortUrl,
            long clickCount,
            LocalDateTime createdAt,
            LocalDateTime expireAt) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.clickCount = clickCount;
        this.createdAt = createdAt;
        this.expireAt = expireAt;
    }

    public String getOriginalUrl() { return originalUrl; }
    public String getShortUrl() { return shortUrl; }
    public long getClickCount() { return clickCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpireAt() { return expireAt; }
}
