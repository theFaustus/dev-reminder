package com.evil.devreminder.service.impl;

import com.evil.devreminder.domain.Crypto;
import com.evil.devreminder.domain.CryptoFearGreedIndex;
import com.evil.devreminder.domain.CryptoFearGreedType;
import com.evil.devreminder.domain.Quote;
import com.evil.devreminder.service.CryptoService;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class CryptoServiceImpl implements CryptoService {
    public static final String HTTPS_CRYPTO_REST = "https://api.alternative.me";
    private final RestTemplate restTemplate;

    @Override
    public CryptoFearGreedIndex getCryptoFearGreedIndex() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", "notice_gdpr_prefs=");
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(HTTPS_CRYPTO_REST + "/fng", HttpMethod.GET,
                                                                requestEntity, String.class);
        try {
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            return new CryptoFearGreedIndex(
                    root.path("data").path(0).path("value").asLong(),
                    LocalDateTime.ofInstant(Instant.ofEpochSecond(root.path("data").path(0).path("timestamp").asLong()),
                                            TimeZone.getDefault().toZoneId()),
                    CryptoFearGreedType.from(root.path("data").path(0).path("value_classification").asText()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new CryptoFearGreedIndex(0, LocalDateTime.now(), CryptoFearGreedType.NEUTRAL);
    }

    @Override
    public List<Crypto> getTop5Cryptos() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", "notice_gdpr_prefs=");
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(HTTPS_CRYPTO_REST + "/v1/ticker/?limit=5", HttpMethod.GET,
                                                                requestEntity, String.class);
        try {
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            List<Crypto> cryptos = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                cryptos.add(new Crypto(root.path(i).path("rank").asInt(),
                                       root.path(i).path("name").asText(),
                                       root.path(i).path("symbol").asText(),
                                       root.path(i).path("price_usd").asDouble(),
                                       root.path(i).path("available_supply").asDouble(),
                                       root.path(i).path("total_supply").asDouble(),
                                       root.path(i).path("max_supply").asDouble()));
            }
            return  cryptos;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Collections.singletonList(new Crypto(-1, "n/a", "n/a", -1.0, -1.0, -1.0, -1.0));
    }
}
