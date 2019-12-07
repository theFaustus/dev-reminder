package com.evil.devreminder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Quote {
    private String text;
    private String author;
}
