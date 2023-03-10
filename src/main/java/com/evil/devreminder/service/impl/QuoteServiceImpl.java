package com.evil.devreminder.service.impl;

import com.evil.devreminder.domain.Quote;
import com.evil.devreminder.repository.QuoteRepository;
import com.evil.devreminder.service.QuoteService;
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

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {
    public static final String HTTPS_QUOTES_REST = "https://api.quotable.io";
    private final RestTemplate restTemplate;
    private final QuoteRepository quoteRepository;

    @Override
    public Quote getRandomQuote(){
        return quoteRepository.findRandomQuote();
    }

    @Override
    public List<Quote> getRandomQuotes(){
        return quoteRepository.findRandomQuotes();
    }

    @Override
    public void save(Quote quote){
        quoteRepository.save(quote);
    }

    @Override
    public void save(List<Quote> quotes){
        quoteRepository.saveAll(quotes);
    }

    @Override
    public Quote getQuoteOfTheDay() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(HTTPS_QUOTES_REST + "/random", HttpMethod.GET, requestEntity, String.class);
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            return new Quote(root.path("content").asText(), root.path("author").asText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Quote("Vooooooid...", "God");
    }
}
