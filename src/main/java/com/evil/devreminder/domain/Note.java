package com.evil.devreminder.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Data
@Document("notes")
public class Note {
    @Id
    private String id;
    private String title;
    private String message;
    private NoteType noteType;

    public Note(String title, String message, NoteType noteType) {
        this.title = title;
        this.message = message;
        this.noteType = noteType;
    }
}
