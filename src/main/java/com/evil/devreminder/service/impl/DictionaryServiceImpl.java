package com.evil.devreminder.service.impl;

import com.evil.devreminder.domain.Word;
import com.evil.devreminder.service.DictionaryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class DictionaryServiceImpl implements DictionaryService {
    private static final String HTTPS_DEX_REST = "https://dexonline.ro";
    private final RestTemplate restTemplate;

    @Override
    public Word getRomanianWordOfTheDay() {
        ResponseEntity<String> response = restTemplate.getForEntity(HTTPS_DEX_REST + "/cuvantul-zilei/" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "/json", String.class);
        try {
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            return new Word(
                    root.path("requested").path("record").path("word").asText(),
                    root.path("requested").path("record").path("definition").path("internalRep").asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new Word("unknown", "unknown");
    }

    @Override
    public Word getRomanianDefinitionFor(String term) {
        ResponseEntity<String> response = restTemplate.getForEntity(HTTPS_DEX_REST + "/definitie/" +
                term + "/json", String.class);
        try {
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            return new Word(
                    root.path("word").asText(),
                    root.path("definitions").path(0).path("internalRep").asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new Word("unknown", "unknown");
    }
}
