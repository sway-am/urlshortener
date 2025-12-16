package org.example.urlshortener.controller;

import org.example.urlshortener.dto.ShortenRequest;
import org.example.urlshortener.dto.ShortenResponse;
import org.example.urlshortener.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shorten(@RequestBody ShortenRequest request) {
        String shortUrl = urlService.shorten(request.getUrl());
        return ResponseEntity.ok(new ShortenResponse(shortUrl));
    }
}
