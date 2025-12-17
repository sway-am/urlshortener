package org.example.urlshortener.service;

import org.example.urlshortener.dto.UrlStatsResponse;
import org.example.urlshortener.model.Url;
import org.example.urlshortener.repository.UrlRepository;
// import org.example.urlshortener.util.Base62;
import org.example.urlshortener.util.HashUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.urlshortener.exception.NotFoundException;
// import org.example.urlshortener.dto.UrlStatsResponse;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final UrlLookupService urlLookupService;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public UrlService(UrlRepository urlRepository, UrlLookupService urlLookupService) {
        this.urlRepository = urlRepository;
        this.urlLookupService = urlLookupService;
    }

    private void validateUrl(String originalUrl) {
        try {
            URI uri = new URI(originalUrl);
            String scheme = uri.getScheme();
            if (scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
                throw new IllegalArgumentException("URL must start with http or https");
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL format", e);
        }
    }

    @Transactional
    public String shorten(String originalUrl) {
        validateUrl(originalUrl);

        // Idempotency: return existing mapping if present
        Optional<Url> existing = urlRepository.findByOriginalUrl(originalUrl);
        if (existing.isPresent()) {
            Url url = existing.get();
            return buildShortUrl(url.getShortCode());
        }

        // Generate hash-based short code with collision handling
        String shortCode;
        int attempt = 0;

        do {
            String input = originalUrl + (attempt == 0 ? "" : attempt);
            shortCode = HashUtil.generateShortCode(input, 7);
            attempt++;
        } while (urlRepository.findByShortCode(shortCode).isPresent());

    
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortCode(shortCode);
        urlRepository.save(url);

        return buildShortUrl(shortCode);
    }

    private String buildShortUrl(String code) {
        return baseUrl.endsWith("/") ? baseUrl + code : baseUrl + "/" + code;
    }

    /**
     * Public resolve method: use UrlLookupService (which is cache-enabled) and increment clicks asynchronously.
     */
    public String resolve(String code) {
        String original = urlLookupService.getOriginalUrl(code);
        // increment click count asynchronously to avoid blocking redirect
        incrementClickCountAsync(code);
        return original;
    }

    /**
     * Async increment (fire-and-forget)
     */
    @Async
    @Transactional
    public CompletableFuture<Void> incrementClickCountAsync(String code) {
        Optional<Url> u = urlRepository.findByShortCode(code);
        u.ifPresent(url -> {
            url.setClickCount(url.getClickCount() + 1);
            urlRepository.save(url);
        });
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Evict cache entry when shortCode changes (e.g., admin updates / deletes).
     * If you evict, call UrlLookupService.getOriginalUrl(...) later to repopulate cache on demand.
     */
    public void evictCacheForCode(String code) {
        // Keep this method for API use; actual eviction should call a method annotated with @CacheEvict
        // You can add a small admin service that calls a @CacheEvict annotated method.
    }


    @Transactional(readOnly = true)
    public UrlStatsResponse getStats(String code) {
        Url url = urlRepository.findByShortCode(code)
                .orElseThrow(() -> new NotFoundException("Short URL not found"));

        String shortUrl = baseUrl.endsWith("/")
                ? baseUrl + url.getShortCode()
                : baseUrl + "/" + url.getShortCode();

        return new UrlStatsResponse(
                url.getOriginalUrl(),
                shortUrl,
                url.getClickCount(),
                url.getCreatedAt(),
                url.getExpireAt()
        );
    }

}
