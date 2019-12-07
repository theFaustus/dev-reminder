package com.evil.devreminder.controller;

import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.NoteType;
import com.evil.devreminder.service.CSVReaderService;
import com.evil.devreminder.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final CSVReaderService csvReaderService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public HttpEntity<List<Note>> getAllNotes() {
        List<Note> notes = noteService.findAll();
        if (notes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(notes, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/note/{id}", method = RequestMethod.GET)
    public HttpEntity<Note> getNoteById(@PathVariable("id") String id) {
        return noteService.findById(id)
                .<HttpEntity<Note>>map(note -> new ResponseEntity<>(note, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/note", method = RequestMethod.POST)
    public HttpEntity<?> saveNote(@RequestBody Note e) {
        if (noteService.noteExists(e)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            Note note = noteService.save(e);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/notes/note/{id}")
                    .buildAndExpand(note.getId()).toUri();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(location);
            return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/note/{id}", method = RequestMethod.PUT)
    public HttpEntity<?> updateNote(@PathVariable("id") String id, @RequestBody Note e) {
        Optional<Note> byId = noteService.findById(id);
        if(byId.isPresent()){
            Note note = byId.get();
            note.setTitle(e.getTitle());
            note.setMessage(e.getMessage());
            noteService.save(note);
            return new ResponseEntity<>(byId, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/note/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteNote(@PathVariable("id") String noteId) {
        noteService.deleteById(noteId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/note", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAll() {
        noteService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/upload", method = POST)
    public ResponseEntity<?> uploadNotes(@RequestPart("notes") MultipartFile notes) {
        try {
            List<String[]> readLines = csvReaderService.read(notes.getInputStream());
            readLines.stream()
                    .skip(1)
                    .forEach(line -> noteService.save(new Note(line[1], line[2], NoteType.valueOf(line[0]))));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Saved");
    }
}
