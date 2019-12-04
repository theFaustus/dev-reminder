package com.evil.devreminder.service;

import com.evil.devreminder.domain.Note;

import java.util.List;
import java.util.Optional;

public interface NoteService {
    Note save(Note note);

    Optional<Note> findById(String id);

    void deleteById(String id);

    void update(Note note);

    boolean noteExists(Note note);

    List<Note> findAll();

    void deleteAll();
}
