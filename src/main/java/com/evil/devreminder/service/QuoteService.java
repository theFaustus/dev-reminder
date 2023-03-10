package com.evil.devreminder.service;

import com.evil.devreminder.domain.Quote;

import java.util.List;

public interface QuoteService {
    Quote getRandomQuote();
    List<Quote> getRandomQuotes();

    void save(Quote quote);

    void save(List<Quote> quotes);

    public Quote getQuoteOfTheDay();
}
