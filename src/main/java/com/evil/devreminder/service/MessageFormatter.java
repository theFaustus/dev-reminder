package com.evil.devreminder.service;

import com.evil.devreminder.domain.Note;
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
    String strikethrough(String text);
    String quote(String text);

    String getNoteMessage(Note n);

    String getWeatherMessage(Weather w);
    String getQuoteMessage(Quote q);
    String getTriviaMessage(Trivia t);
    String getDictionaryMessage(Word w);
    String getComplexMessage(Note n, Weather w, Quote q, Trivia t, Word wd);
    String getComplexMessage(Note n, Weather w, Quote q, Word wd);
    String getComplexMessage(Note n, Weather w, List<WeatherForecast> weatherForecasts, Quote q, Word wd);
    String getWeatherForecastMessage(List<WeatherForecast> weatherForecasts);
    String getWeatherForecastMessage(List<WeatherForecast> weatherForecasts, int limit);
    String getWakeMeUpMessage();
    String getHelpMessage();
}
