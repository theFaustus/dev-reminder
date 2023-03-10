package com.evil.devreminder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("quotes")
public class Quote {
    @Id
    private String id;
    private String text;
    private String author;

    public Quote(String text, String author) {
        this.text = text;
        this.author = author;
    }
}
