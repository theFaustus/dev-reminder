package com.evil.devreminder.service.impl;

import com.evil.devreminder.domain.Weather;
import com.evil.devreminder.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    public static final String WEATHER_API_TOKEN = "04c239f07eb248380fc04e42d4d6f450";
    public static final String HTTP_OPENWEATHERMAP_REST = "http://api.openweathermap.org/data/2.5/weather";
    private final RestTemplate restTemplate;

    @Override
    public Weather getWeatherFor(String city) {

        ResponseEntity<String> response = restTemplate.getForEntity(HTTP_OPENWEATHERMAP_REST + "?q=" + city + "&units=metric&appid=" + WEATHER_API_TOKEN, String.class);
        try {
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            return new Weather(
                    root.path("weather").path(0).path("description").asText(),
                    root.path("main").path("temp").asDouble(),
                    root.path("main").path("humidity").asDouble(),
                    root.path("name").asText(),
                    root.path("sys").path("country").asText()
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new Weather("unknown", 0.0, 0.0, "unkown", "unknown");
    }
}
