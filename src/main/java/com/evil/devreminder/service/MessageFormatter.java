package com.evil.devreminder.service;

import com.evil.devreminder.domain.Crypto;
import com.evil.devreminder.domain.CryptoFearGreedIndex;
import com.evil.devreminder.domain.NewsArticle;
import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.Picture;
import com.evil.devreminder.domain.Quote;
import com.evil.devreminder.domain.Trivia;
import com.evil.devreminder.domain.Weather;
import com.evil.devreminder.domain.WeatherForecast;
import com.evil.devreminder.domain.Word;

import java.util.List;

public interface MessageFormatter {
    String bold(String text);
    String italic(String text);
    String codifiedBlock(String text);
    String codified(String text);
    String link(String text);
    String namedLink(String name, String text);
    String image(String name, String text);
    String escape(String text);
    String strikethrough(String text);
    String quote(String text);

    String getNoteMessage(Note n);
    String getMultipleNotesMessage(List<Note> notes);
    String getComplexMessage(Note n, Weather w, List<WeatherForecast> wf,
                             Quote q, Word wd, Picture p);

    String getComplexMessage(Note n, Weather w, List<WeatherForecast> wf,
                             Quote q, Word wd, Picture p, CryptoFearGreedIndex cfgi, List<Crypto> cs);

    String getWeatherMessage(Weather w);

    String getSpringNoteMessage(Note n);
    String getSpringMultipleNotesMessage(List<Note> notes);
    String getJpaNoteMessage(Note n);
    String getJpaMultipleNotesMessage(List<Note> notes);

    String getQuoteMessage(Quote q);
    String getTriviaMessage(Trivia t);
    String getPictureMessage(Picture p);
    String getDictionaryMessage(Word w);
    String getComplexMessage(Note n, Weather w, Quote q, Trivia t, Word wd);
    String getComplexMessage(Note n, Weather w, Quote q, Word wd, Picture p);
    String getComplexMessage(Note n, Weather w, Quote q, Word wd);
    String getComplexMessage(Note n, Weather w, List<WeatherForecast> weatherForecasts, Quote q, Word wd);
    String getWeatherForecastMessage(List<WeatherForecast> weatherForecasts);
    String getWeatherForecastMessage(List<WeatherForecast> weatherForecasts, int limit);

    String getCryptoMessage(CryptoFearGreedIndex cryptoFearGreedIndex, List<Crypto> cryptos, int limit);

    String getWakeMeUpMessage();
    String getHelpMessage();

    String getNewsArticlesMessage(List<NewsArticle> articles);
}
