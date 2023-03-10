package com.evil.devreminder.repository;

import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.NoteType;
import com.evil.devreminder.domain.Quote;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends MongoRepository<Quote, String> {
    @Aggregation(pipeline = "{ $sample: { size: 1 } }")
    Quote findRandomQuote();

    @Aggregation(pipeline = "{ $sample: { size: 3 } }")
    List<Quote> findRandomQuotes();
}
