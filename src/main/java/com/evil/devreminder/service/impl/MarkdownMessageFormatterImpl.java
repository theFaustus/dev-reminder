package com.evil.devreminder.service.impl;

import com.evil.devreminder.domain.Crypto;
import com.evil.devreminder.domain.CryptoFearGreedIndex;
import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.Picture;
import com.evil.devreminder.domain.Quote;
import com.evil.devreminder.domain.Trivia;
import com.evil.devreminder.domain.Weather;
import com.evil.devreminder.domain.WeatherForecast;
import com.evil.devreminder.domain.Word;
import com.evil.devreminder.service.MessageFormatter;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Service
public class MarkdownMessageFormatterImpl implements MessageFormatter {

    public static final String BOLD_CHAR = "*";
    private static final String ITALIC_CHAR = "\\_";
    private static final String CODE_BLOCK_CHAR = "```";
    private static final String CODE_CHAR = "`";
    private static final String STRIKETHROUGH_CHAR = "~";
    private static final String QUOTE_CHAR = ">";

    @Override
    public String bold(String text) {
        return BOLD_CHAR + text + BOLD_CHAR;
    }

    @Override
    public String italic(String text) {
        return ITALIC_CHAR + text + ITALIC_CHAR;
    }

    @Override
    public String codifiedBlock(String text) {
        return CODE_BLOCK_CHAR + text + CODE_BLOCK_CHAR;
    }

    @Override
    public String codified(String text) {
        return CODE_CHAR + text + CODE_CHAR;
    }

    @Override
    public String link(String text) {
        return "(" + text + ")";
    }

    @Override
    public String namedLink(String name, String text) {
        return "[" + name + "]" + "(" + text + ")";
    }

    @Override
    public String image(String name, String text) {
        return "[" + name + "]" + "(" + text + ")";
    }

    @Override
    public String escape(final String text) {
        return text
                .replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("`", "\\`");
    }

    @Override
    public String strikethrough(String text) {
        return STRIKETHROUGH_CHAR + text + STRIKETHROUGH_CHAR;
    }

    @Override
    public String quote(String text) {
        return QUOTE_CHAR + text;
    }

    @Override
    public String getNoteMessage(Note n) {
        return "\uD83E\uDDE0 " + bold(n.getNoteType().name()) + " - " + n.getTitle() + "\n" + codifiedBlock(
                StringEscapeUtils.unescapeJava(n.getMessage()));
    }

    @Override
    public String getMultipleNotesMessage(final List<Note> notes) {
        if (notes.isEmpty()) return "No results by your search criteria";
        StringBuilder stringBuilder = new StringBuilder();
        for (final Note n : notes) {
            stringBuilder.append("\uD83E\uDDE0 ").append(bold(n.getNoteType().name()))
                    .append(" - ").append(n.getTitle()).append("\n")
                    .append(codifiedBlock(StringEscapeUtils.unescapeJava(n.getMessage())))
                    .append("\n\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public String getSpringNoteMessage(Note n) {
        return "\uD83C\uDF43 " + bold(n.getNoteType().name()) + " - " + n.getTitle() + "\n" + codifiedBlock(
                StringEscapeUtils.unescapeJava(n.getMessage()));
    }

    @Override
    public String getSpringMultipleNotesMessage(final List<Note> notes) {
        if (notes.isEmpty()) return "No results by your search criteria";
        StringBuilder stringBuilder = new StringBuilder();
        for (final Note n : notes) {
            stringBuilder
                    .append("\uD83C\uDF43 ").append(bold(n.getNoteType().name())).append(" - ")
                    .append(n.getTitle()).append("\n")
                    .append(codifiedBlock(StringEscapeUtils.unescapeJava(n.getMessage())))
                    .append("\n\n");
        }
        return stringBuilder.toString();
    }


    @Override
    public String getJpaNoteMessage(Note n) {
        return "\uD83D\uDCA0 " + bold(n.getNoteType().name()) + " - " + n.getTitle() + "\n" + codifiedBlock(
                StringEscapeUtils.unescapeJava(n.getMessage()));
    }

    @Override
    public String getJpaMultipleNotesMessage(final List<Note> notes) {
        if (notes.isEmpty()) return "No results by your search criteria";
        StringBuilder stringBuilder = new StringBuilder();
        for (final Note n : notes) {
            stringBuilder
                    .append("\uD83D\uDCA0 ").append(bold(n.getNoteType().name())).append(" - ")
                    .append(n.getTitle()).append("\n")
                    .append(codifiedBlock(StringEscapeUtils.unescapeJava(n.getMessage())))
                    .append("\n\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public String getQuoteMessage(Quote q) {
        return "\uD83E\uDDD0 " + "❝" + q.getText() + "❞ - " + italic(q.getAuthor());
    }

    @Override
    public String getTriviaMessage(Trivia t) {
        return bold("\uD83D\uDDFD History") + " : " + t.getHistoryFact() + "\n" +
                bold("\uD83D\uDD22 Math") + " : " + t.getMathFact() + "\n" +
                bold("⭐️ Random") + " : " + t.getRandomFact() + "\n";
    }

    @Override
    public String getPictureMessage(Picture p) {
        return bold("\uD83D\uDDBC") + " " + image(p.getTitle() + ", " + p.getCopyRight(), p.getUrl());
    }

    @Override
    public String getDictionaryMessage(Word w) {
        return "\uD83E\uDD13 " + bold(w.getTerm()) + " - " + escape(w.getDefinition());
    }

    @Override
    public String getComplexMessage(Note n, Weather w, Quote q, Trivia t, Word wd) {
        return "Good day sir! Today is " +
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                                        .withZone(ZoneId.systemDefault())) +
                " and here is something that might be of interest for your majesty : \n\n" +
                getWeatherMessage(w) + "\n\n" +
                getQuoteMessage(q) + "\n\n" +
                getTriviaMessage(t) + "\n\n" +
                getDictionaryMessage(wd) + "\n\n" +
                getNoteMessage(n);
    }

    @Override
    public String getComplexMessage(Note n, Weather w, Quote q, Word wd, Picture p) {
        return "Good day sir! Today is " +
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                                        .withZone(ZoneId.systemDefault())) +
                " and here is something that might be of interest for your majesty : \n\n" +
                getWeatherMessage(w) + "\n\n" +
                getQuoteMessage(q) + "\n\n" +
                getPictureMessage(p) + "\n\n" +
                getDictionaryMessage(wd) + "\n\n" +
                getNoteMessage(n);
    }

    @Override
    public String getComplexMessage(Note n, Weather w, Quote q, Word wd) {
        return "Good day sir! Today is " +
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                                        .withZone(ZoneId.systemDefault())) +
                " and here is something that might be of interest for your majesty : \n\n" +
                getWeatherMessage(w) + "\n\n" +
                getQuoteMessage(q) + "\n\n" +
                getDictionaryMessage(wd) + "\n\n" +
                getNoteMessage(n);
    }

    @Override
    public String getComplexMessage(final Note n, final Weather w, final List<WeatherForecast> wf,
                                    final Quote q, final Word wd) {
        return "Good day sir! Today is " +
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                                        .withZone(ZoneId.systemDefault())) +
                " and here is something that might be of interest for your majesty : \n\n" +
                getWeatherMessage(w) + "\n\n" +
                getWeatherForecastMessage(wf, 3) + "\n\n" +
                getQuoteMessage(q) + "\n\n" +
                getDictionaryMessage(wd) + "\n\n" +
                getNoteMessage(n);
    }

    @Override
    public String getComplexMessage(final Note n, final Weather w, final List<WeatherForecast> wf,
                                    final Quote q, final Word wd, final Picture p) {
        return "Good day sir! Today is " +
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                                        .withZone(ZoneId.systemDefault())) +
                " and here is something that might be of interest for your majesty : \n\n" +
                getWeatherMessage(w) + "\n\n" +
                getWeatherForecastMessage(wf, 3) + "\n\n" +
                getQuoteMessage(q) + "\n\n" +
                getPictureMessage(p) + "\n\n" +
                getDictionaryMessage(wd) + "\n\n" +
                getNoteMessage(n);
    }

    @Override
    public String getComplexMessage(final Note n, final Weather w, final List<WeatherForecast> wf,
                                    final Quote q, final Word wd, final Picture p, final CryptoFearGreedIndex cfgi, final List<Crypto> cs) {
        return "Good day sir! Today is " +
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                                        .withZone(ZoneId.systemDefault())) +
                " and here is something that might be of interest for your majesty : \n\n" +
                getCryptoMessage(cfgi, cs, 1) + "\n\n" +
                getWeatherMessage(w) + "\n\n" +
                getWeatherForecastMessage(wf, 3) + "\n\n" +
                getQuoteMessage(q) + "\n\n" +
                getPictureMessage(p) + "\n\n" +
                getDictionaryMessage(wd) + "\n\n" +
                getNoteMessage(n);
    }

    @Override
    public String getWeatherMessage(Weather w) {
        return "\uD83C\uDF1E At the moment in " + w.getCity() + ", " + w.getCountry() + " is " + w.getTemperature() + "°C with " + w.getDescription();
    }

    @Override
    public String getWeatherForecastMessage(List<WeatherForecast> weatherForecasts) {
        StringBuilder stringBuilder = new StringBuilder(
                "\uD83C\uDF1D The forecast for " + weatherForecasts.get(0).getTimezone() + " is : ");
        weatherForecasts.forEach(w -> {
            stringBuilder.append("\n\t - ").append(
                    w.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))).append(" : ").append(
                    w.getDayTemperature()).append("°C/").append(w.getNightTemperature()).append("°C (").append(
                    w.getDescription()).append(")");
        });
        return stringBuilder.toString();
    }

    @Override
    public String getWeatherForecastMessage(List<WeatherForecast> weatherForecasts, int limit) {
        StringBuilder stringBuilder = new StringBuilder(
                "\uD83C\uDF1D The forecast for " + weatherForecasts.get(0).getTimezone() + " is : ");
        weatherForecasts.stream().limit(limit).forEach(w -> {
            stringBuilder.append("\n\t - ").append(
                    w.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))).append(" : ").append(
                    w.getDayTemperature()).append("°C/").append(w.getNightTemperature()).append("°C (").append(
                    w.getDescription()).append(")");
        });
        return stringBuilder.toString();
    }

    @Override
    public String getCryptoMessage(CryptoFearGreedIndex cryptoFearGreedIndex, List<Crypto> cryptos, int limit) {
        StringBuilder stringBuilder = new StringBuilder(
                " The crypto index is "
                        + cryptoFearGreedIndex.getIndex() + " : "
                        + cryptoFearGreedIndex.getCryptoFearGreedType() + " "
                        + cryptoFearGreedIndex.getCryptoFearGreedType().getEmoji());
        cryptos.stream().limit(limit).forEach(c -> stringBuilder.append("\n\t • ")
                .append(c.getRank()).append(". ").append(c.getSymbol())
                .append("(").append(c.getName()).append(") - ")
                .append(c.getPrice()).append(" $"));
        return stringBuilder.toString().replaceAll("_", " ");
    }

    @Override
    public String getWakeMeUpMessage() {
        return "\n\t\t\t\t\uD83D\uDCA4 If I am sleepy, wake me up (https://dev-reminder.herokuapp.com/actuator/health).";
    }


    @Override
    public String getHelpMessage() {
        return "Well, let's see... Here is what can I do :\n" +
                codified("get#SOFTWARE|MOTIVATION|PRACTICES|software|motivation|practices") + " - " + italic(
                "get random note") + "\n" +
                codified("get#QOD|qod") + " - " + italic("get quote of the day") + "\n" +
                codified("get#TRIVIA|trivia") + " - " + italic("get a random trivia fact") + "\n" +
                codified("get#WEATHER|weather#type your city") + " - " + italic(
                "get weather for today for a city") + "\n" +
                codified("get#DEX|dex#type your word") + " - " + italic("get definition for a word (DEX)") + "\n" +
                codified("get#DEX-WOTD|DEX-WOTD") + " - " + italic("get word of the day (DEX)") + "\n" +
                codified(
                        "add#SOFTWARE|MOTIVATION|PRACTICES|software|motivation|practices#type the title#type the description") + " - " + italic(
                "add a new note");
    }

}
