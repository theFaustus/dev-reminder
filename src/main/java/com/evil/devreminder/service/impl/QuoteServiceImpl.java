package com.evil.devreminder.service.impl;

import com.evil.devreminder.domain.Quote;
import com.evil.devreminder.service.QuoteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {
    public static final String HTTPS_QUOTES_REST = "https://quotes.rest";
    private final RestTemplate restTemplate;

    @Override
    public Quote getQuoteOfTheDay() {
        ResponseEntity<String> response = restTemplate.getForEntity(HTTPS_QUOTES_REST + "/qod", String.class);
        try {
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            return new Quote(
                    root.path("contents").path("quotes").path(0).path("quote").asText(),
                    root.path("contents").path("quotes").path(0).path("author").asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new Quote("Vooooooid...", "God");
    }
}
