// A data-access layer built on top of Spring Data JPA for managing URL entities.
package org.example.urlshortener.repository;

import org.example.urlshortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortCode(String shortCode);
}
