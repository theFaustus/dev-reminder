package com.evil.devreminder.service.impl;

import com.evil.devreminder.domain.Picture;
import com.evil.devreminder.service.PictureService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class NasaPictureServiceImpl implements PictureService {
    private static final String HTTPS_NASA_REST = "https://api.nasa.gov/planetary/apod";
    private final RestTemplate restTemplate;
    @Value("${nasaApiToken}")
    private String nasaApiToken;

    @Override
    public String getPictureOfTheDayLink() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(HTTPS_NASA_REST + "?api_key=" + nasaApiToken,
                                                                HttpMethod.GET, requestEntity, String.class);
        try {
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            return root.path("url").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "https://tinyurl.com/y5fpyhb7";
    }

    @Override
    public Picture getPictureOfTheDay() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(HTTPS_NASA_REST + "?api_key=" + nasaApiToken,
                                                                HttpMethod.GET, requestEntity, String.class);
        try {
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            return new Picture(root.path("copyright").asText(),
                               root.path("explanation").asText(),
                               root.path("url").asText(),
                               root.path("title").asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new Picture("n/a", "n/a", "https://tinyurl.com/y5fpyhb7", "n/a");
    }
}
