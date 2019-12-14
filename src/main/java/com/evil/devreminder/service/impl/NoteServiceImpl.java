package com.evil.devreminder.service.impl;

import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.NoteType;
import com.evil.devreminder.repository.NoteRepository;
import com.evil.devreminder.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private List<Integer> seenNotes = new ArrayList<>();

    @Override
    public Note save(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public Optional<Note> findById(String id) {
        return noteRepository.findById(id);
    }

    @Override
    public Note getRandomNoteByType(NoteType type) {
        List<Note> notes = noteRepository.findByNoteType(type);
        int randomNoteIndex = type == NoteType.SOFTWARE
                ? getRandomNonRepeatingNoteIndex(notes.size())
                : new Random().nextInt(notes.size());
        return notes.isEmpty() ? new Note("unknown", "unknown", type) : notes.get(randomNoteIndex);
    }

    public int getRandomNonRepeatingNoteIndex(int bound) {
        Random r = new Random();
        int index = r.nextInt(bound);
        int tries = 0;
        while (seenNotes.contains(index)) {
            index = r.nextInt(bound);
            tries++;
            if (tries > 10)
                seenNotes = new ArrayList<>();
        }
        seenNotes.add(index);
        return index;
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
