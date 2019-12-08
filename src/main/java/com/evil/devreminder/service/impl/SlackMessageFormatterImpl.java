package com.evil.devreminder.service.impl;

import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.Quote;
import com.evil.devreminder.domain.Trivia;
import com.evil.devreminder.domain.Weather;
import com.evil.devreminder.domain.Word;
import com.evil.devreminder.service.MessageFormatter;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Service
public class SlackMessageFormatterImpl implements MessageFormatter {

    public static final String BOLD_CHAR = "*";
    private static final String ITALIC_CHAR = "_";
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
    public String strikethrough(String text) {
        return STRIKETHROUGH_CHAR + text + STRIKETHROUGH_CHAR;

    }

    @Override
    public String quote(String text) {
        return QUOTE_CHAR + text;
    }

    @Override
    public String getNoteMessage(Note n) {
        return bold(n.getNoteType().name()) + " - " + n.getTitle() + "\n" + codifiedBlock(StringEscapeUtils.unescapeJava(n.getMessage()));
    }

    @Override
    public String getQuoteMessage(Quote q) {
        return quote("❝" + q.getText() + "❞") + " - " + italic(q.getAuthor());
    }

    @Override
    public String getTriviaMessage(Trivia t) {
        return bold("History") + " : " + t.getHistoryFact() + "\n" +
                bold("Math") + " : " + t.getMathFact() + "\n" +
                bold("Random") + " : " + t.getRandomFact() + "\n";
    }

    @Override
    public String getDictionaryMessage(Word w) {
        return bold(w.getTerm()) + " - " + w.getDefinition() + "\n";
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
    public String getWeatherMessage(Weather w) {
        return "\uD83C\uDF08 At the moment in " + w.getCity() + ", " + w.getCountry() + " is " + w.getTemperature() + "°C with " + w.getDescription();
    }

    @Override
    public String getHelpMessage() {
        return "Well, let`s see... Here is what can I do :\n" +
                codified("devy#get#SOFTWARE|MOTIVATION|software|motivation") + " - " + italic("get random note") + "\n" +
                codified("devy#get#QOD|qod") + " - " + italic("get quote of the day") + "\n" +
                codified("devy#get#TRIVIA|trivia") + " - " + italic("get a random trivia fact") + "\n" +
                codified("devy#get#WEATHER|weather#type your city") + " - " + italic("get weather for today for a city") + "\n" +
                codified("devy#get#DEX|dex#type your word") + " - " + italic("get definition for a word (DEX)") + "\n" +
                codified("devy#get#DEX-WOTD|DEX-WOTD") + " - " + italic("get word of the day (DEX)") + "\n" +
                codified("devy#get#HISTORY|history") + " - " + italic("get a history fact") + "\n" +
                codified("devy#add#SOFTWARE|MOTIVATION|software|motivation#type the title#type the description") + " - " + italic("add a new note");
    }

}
