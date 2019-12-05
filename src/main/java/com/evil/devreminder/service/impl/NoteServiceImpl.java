package com.evil.devreminder.service.impl;

import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.NoteType;
import com.evil.devreminder.repository.NoteRepository;
import com.evil.devreminder.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    @Override
    public Note save(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public Optional<Note> findById(String id) {
        return noteRepository.findById(id);
    }

    @Override
    public Note getRandomOneByType(NoteType type) {
        List<Note> byNoteType = noteRepository.findByNoteType(type);
        return byNoteType.get(new Random().nextInt(byNoteType.size()));
    }

    @Override
    public void deleteById(String id) {
        noteRepository.deleteById(id);
    }

    @Override
    public void update(Note note) {
        noteRepository.save(note);
    }

    @Override
    public boolean noteExists(Note note) {
        return noteRepository.exists(Example.of(note));
    }

    @Override
    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    @Override
    public void deleteAll() {
        noteRepository.deleteAll();
    }
}
