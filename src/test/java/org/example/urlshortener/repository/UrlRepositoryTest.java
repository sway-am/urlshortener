package org.example.urlshortener.repository;

import org.example.urlshortener.model.Url;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UrlRepositoryTest {

    @Autowired
    private UrlRepository urlRepository;

    @Test
    void findByOriginalUrl_shouldReturnUrl() {
        Url url = new Url();
        url.setOriginalUrl("https://example.com");
        url.setShortCode("abc123");

        urlRepository.save(url);

        Optional<Url> found = urlRepository.findByOriginalUrl("https://example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getShortCode()).isEqualTo("abc123");
    }
}
