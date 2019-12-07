package com.evil.devreminder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Trivia {
    private String mathFact;
    private String historyFact;
    private String randomFact;
}
