package org.example.urlshortener.controller;

import org.example.urlshortener.service.UrlService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RedirectController {

    private final UrlService urlService;

    public RedirectController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{code}")
    public RedirectView redirect(@PathVariable String code) {
        String originalUrl = urlService.resolve(code);
        return new RedirectView(originalUrl);
    }
}
