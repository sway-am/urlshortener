// Business logic layer for URL shortening service, handling URL validation, shortening, and persistence.
package org.example.urlshortener.service;

import org.example.urlshortener.model.Url;
import org.example.urlshortener.repository.UrlRepository;
import org.example.urlshortener.util.Base62;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class UrlService {
    private final UrlRepository urlRepository;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
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

        // 1) Save entity first to obtain DB id
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url = urlRepository.save(url); // id will be generated here

        // 2) Create short code from id
        String code = Base62.encode(url.getId());

        // 3) Persist short code
        url.setShortCode(code);
        url = urlRepository.save(url);

        // 4) Return full short URL
        return baseUrl.endsWith("/") ? baseUrl + code : baseUrl + "/" + code;
    }
    
}
