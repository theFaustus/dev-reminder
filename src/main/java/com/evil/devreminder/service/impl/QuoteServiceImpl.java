package com.evil.devreminder.service.impl;

import com.evil.devreminder.domain.Quote;
import com.evil.devreminder.service.QuoteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {
    public static final String HTTPS_QUOTES_REST = "https://quotes.rest";
    private final RestTemplate restTemplate;

    @Override
    public Quote getQuoteOfTheDay() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(HTTPS_QUOTES_REST + "/qod", HttpMethod.GET, requestEntity, String.class);
        try {
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            return new Quote(
                    root.path("contents").withArray("quotes").get(0).path("quote").asText(),
                    root.path("contents").withArray("quotes").get(0).path("author").asText(),
                    root.path("contents").withArray("quotes").get(0).path("permalink").asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new Quote("Vooooooid...", "God", "https://tinyurl.com/y5fpyhb7");
    }
}
