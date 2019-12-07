package com.evil.devreminder.service;

import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.Quote;
import com.evil.devreminder.domain.Trivia;
import com.evil.devreminder.domain.Weather;

public interface MessageFormatter {
    String bold(String text);
    String italic(String text);
    String codifiedBlock(String text);
    String codified(String text);
    String strikethrough(String text);
    String quote(String text);

    String getNoteMessage(Note n);
    String getWeatherMessage(Weather w);
    String getQuoteMessage(Quote q);
    String getTriviaMessage(Trivia t);
    String getComplexMessage(Note n, Weather w, Quote q, Trivia t);
    String getHelpMessage();
}
