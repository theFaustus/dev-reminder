package com.evil.devreminder.service.impl;

import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.NoteType;
import com.evil.devreminder.repository.NoteRepository;
import com.evil.devreminder.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private Set<Integer> seenSoftwareNotes = new HashSet<>();
    private Set<Integer> seenSpringNotes = new HashSet<>();
    private Set<Integer> seenJpaNotes = new HashSet<>();

    private Set<Integer> seenSearchSoftwareNotes = new HashSet<>();
    private Set<Integer> seenSearchSpringNotes = new HashSet<>();
    private Set<Integer> seenSearchJpaNotes = new HashSet<>();

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
        List<Note> notes = noteRepository.findByNoteTypeOrderByIdAsc(type);
        int randomNoteIndex = getRandomNonRepeatingNoteSeenIndex(notes.size(), type);
        return notes.isEmpty() ? new Note("unknown", "unknown", type) : notes.get(randomNoteIndex);
    }

    @Override
    public List<Note> searchNote(NoteType type, String... keywords) {
        Set<Note> notes = new HashSet<>();
        String keyword = String.join(" ", keywords);
        List<Note> noteByAllKeywords = noteRepository.findByNoteTypeAndMessageLikeOrderByIdAsc(type, keyword);
        notes.add(noteByAllKeywords.get(getRandomNonRepeatingNoteSearchIndex(noteByAllKeywords.size(), type)));

        keyword = Arrays.stream(keywords).max(Comparator.comparingInt(String::length)).orElse(" ");
        List<Note> noteByLongestKeyword = noteRepository.findByNoteTypeAndMessageLikeOrderByIdAsc(type, keyword);
        notes.add(noteByLongestKeyword.get(getRandomNonRepeatingNoteSearchIndex(noteByLongestKeyword.size(), type)));

        return notes.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public int getRandomNonRepeatingNoteSeenIndex(int bound, NoteType noteType){
        switch (noteType){
            case SOFTWARE: return getRandomNonRepeatingNoteIndex(seenSoftwareNotes, bound);
            case SPRING: return getRandomNonRepeatingNoteIndex(seenSpringNotes, bound);
            case JPA: return getRandomNonRepeatingNoteIndex(seenJpaNotes, bound);
            default: return new Random().nextInt(bound);
        }
    }

    public int getRandomNonRepeatingNoteSearchIndex(int bound, NoteType noteType){
        switch (noteType){
            case SOFTWARE: return getRandomNonRepeatingNoteIndex(seenSearchSoftwareNotes, bound);
            case SPRING: return getRandomNonRepeatingNoteIndex(seenSearchSpringNotes, bound);
            case JPA: return getRandomNonRepeatingNoteIndex(seenSearchJpaNotes, bound);
            default: return new Random().nextInt(bound);
        }
    }

    public int getRandomNonRepeatingNoteIndex(Set<Integer> seen, int bound) {
        Random r = new Random();
        int index = r.nextInt(bound);
        int tries = 0;
        while (seen.contains(index)) {
            index = r.nextInt(bound);
            tries++;
            if (tries > 10) {
                seen = new HashSet<>();
            }
        }
        seen.add(index);
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
