package com.evil.devreminder.repository;

import com.evil.devreminder.domain.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<Note, String> {
}
