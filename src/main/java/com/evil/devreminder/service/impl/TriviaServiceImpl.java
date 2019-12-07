package com.evil.devreminder.service.impl;

import com.evil.devreminder.domain.Trivia;
import com.evil.devreminder.service.TriviaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TriviaServiceImpl implements TriviaService {
    public static final String HTTP_NUMBERSAPI_REST = "http://numbersapi.com";
    private final RestTemplate restTemplate;

    @Override
    public Trivia getTriviaForToday() {
        LocalDate date = LocalDate.now();
        ResponseEntity<String> randomFact = restTemplate.getForEntity(HTTP_NUMBERSAPI_REST + "/" + date.getDayOfMonth(), String.class);
        ResponseEntity<String> mathFact = restTemplate.getForEntity(HTTP_NUMBERSAPI_REST + "/" + date.getDayOfMonth() + "/math", String.class);
        ResponseEntity<String> historyFact = restTemplate.getForEntity(HTTP_NUMBERSAPI_REST + "/" + date.getMonthValue() + "/" + date.getDayOfMonth() + "/date", String.class);

        return new Trivia(mathFact.getBody(), historyFact.getBody(), randomFact.getBody());
    }
}
