package org.example.urlshortener.controller;

import java.io.IOException;

import org.example.urlshortener.service.UrlService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RedirectController {

    private final UrlService urlService;

    public RedirectController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{code:[a-zA-Z0-9]+}")
    public void redirect(
            @PathVariable String code,
            HttpServletResponse response) throws IOException {

        String originalUrl = urlService.resolve(code);
        response.sendRedirect(originalUrl);
    }
}
