package org.example.urlshortener.service;

import org.example.urlshortener.exception.ExpiredException;
import org.example.urlshortener.exception.NotFoundException;
import org.example.urlshortener.model.Url;
import org.example.urlshortener.repository.UrlRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlLookupService {

    private final UrlRepository urlRepository;

    public UrlLookupService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Cacheable(value = "codeToUrl", key = "#code")
    public String getOriginalUrl(String code) {
        Url url = urlRepository.findByShortCode(code)
                .orElseThrow(() -> new NotFoundException("Short URL not found"));
        if (url.getExpireAt() != null && url.getExpireAt().isBefore(LocalDateTime.now())) {
            throw new ExpiredException("Short URL has expired");
        }
        return url.getOriginalUrl();
    }
}
