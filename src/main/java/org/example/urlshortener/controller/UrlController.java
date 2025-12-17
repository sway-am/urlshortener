package org.example.urlshortener.controller;

import org.example.urlshortener.dto.ShortenRequest;
import org.example.urlshortener.dto.ShortenResponse;
import org.example.urlshortener.dto.UrlStatsResponse;
import org.example.urlshortener.service.RateLimiterService;
import org.example.urlshortener.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class UrlController {

    private final UrlService urlService;
    private final RateLimiterService rateLimiterService;

    public UrlController(UrlService urlService, RateLimiterService rateLimiterService) {
        this.urlService = urlService;
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shorten(
            @RequestBody ShortenRequest request,
            HttpServletRequest httpRequest) {

        String clientIp = httpRequest.getRemoteAddr();
        String rateLimitKey = "rl:shorten:" + clientIp;

        rateLimiterService.checkRateLimit(rateLimitKey);

        String shortUrl = urlService.shorten(request.getUrl());
        return ResponseEntity.ok(new ShortenResponse(shortUrl));
    }

    @GetMapping("/stats/{code:[a-zA-Z0-9]+}")
    public ResponseEntity<UrlStatsResponse> stats(@PathVariable String code) {
        return ResponseEntity.ok(urlService.getStats(code));
    }
}
