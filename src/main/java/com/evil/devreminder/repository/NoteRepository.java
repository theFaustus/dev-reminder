package com.evil.devreminder.repository;

import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.NoteType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {
    List<Note> findByNoteTypeOrderByIdAsc(NoteType type);
    List<Note> findByNoteTypeAndMessageLikeOrderByIdAsc(NoteType type, String keyword);
}
