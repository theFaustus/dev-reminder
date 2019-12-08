package com.evil.devreminder.bot;

import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.NoteType;
import com.evil.devreminder.domain.Quote;
import com.evil.devreminder.domain.Trivia;
import com.evil.devreminder.domain.Weather;
import com.evil.devreminder.domain.Word;
import com.evil.devreminder.service.DictionaryService;
import com.evil.devreminder.service.MessageFormatter;
import com.evil.devreminder.service.NoteService;
import com.evil.devreminder.service.QuoteService;
import com.evil.devreminder.service.TriviaService;
import com.evil.devreminder.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ramswaroop.jbot.core.common.Controller;
import me.ramswaroop.jbot.core.common.EventType;
import me.ramswaroop.jbot.core.common.JBot;
import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.models.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.WebSocketSession;

import java.util.regex.Matcher;

@JBot
@Slf4j
@RequiredArgsConstructor
public class SlackBot extends Bot {
    @Value("${slackBotToken}")
    private String slackBotToken;
    @Value("${slackDMChannelId}")
    private String slackChannelId;
    @Value("${weather.default.city}")
    private String defaultCity;

    private final NoteService noteService;
    private final QuoteService quoteService;
    private final WeatherService weatherService;
    private final TriviaService triviaService;
    private final DictionaryService dictionaryService;
    private final MessageFormatter mf;

    private WebSocketSession activeSession;

    @Override
    public String getSlackToken() {
        return slackBotToken;
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        super.afterConnectionEstablished(session);
        this.activeSession = session;
        log.info("Connection established. Active session > " + this.activeSession);
    }

    //@Scheduled(cron = "*/10 * * * * *")
    public void sendSoftwareNote() {
        Note note = noteService.getRandomNoteByType(NoteType.SOFTWARE);
        Event event = new Event();
        event.setType(EventType.DIRECT_MESSAGE.name());
        event.setChannelId(slackChannelId);
        reply(activeSession, event, mf.getNoteMessage(note));
    }

    //@Scheduled(cron = "*/10 * * * * *")
    public void sendMotivationNote() {
        Note note = noteService.getRandomNoteByType(NoteType.MOTIVATION);
        Event event = new Event();
        event.setType(EventType.DIRECT_MESSAGE.name());
        event.setChannelId(slackChannelId);
        reply(activeSession, event, mf.getNoteMessage(note));
    }


    @Controller(events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveUnknown(WebSocketSession session, Event event) {
        reply(session, event, "Hi, I am " + slackService.getCurrentUser().getName() + ". My creator is Ion Pascari. " +
                "My purpose is to serve him. Type <help> to see what can I do");
    }

    @Controller(pattern = "help", events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveHelp(WebSocketSession session, Event event) {
        reply(session, event, mf.getHelpMessage());
    }

    @Controller(pattern = "(devy)#(get)#(SOFTWARE|MOTIVATION|software|motivation)", events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveRequestRandomNote(WebSocketSession session, Event event, Matcher matcher) {
        Note n = noteService.getRandomNoteByType(NoteType.valueOf(matcher.group(3).toUpperCase()));
        reply(session, event, mf.getNoteMessage(n));
    }

    @Controller(pattern = "(devy)#(get)#(QOD|qod)", events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveRequestQuote(WebSocketSession session, Event event, Matcher matcher) {
        Quote q = quoteService.getQuoteOfTheDay();
        reply(session, event, mf.getQuoteMessage(q));
    }

    @Controller(pattern = "(devy)#(get)#(WEATHER|weather)#(\\w+)", events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveRequestWeather(WebSocketSession session, Event event, Matcher matcher) {
        Weather w = weatherService.getWeatherFor(matcher.group(4));
        reply(session, event, mf.getWeatherMessage(w));
    }

    @Controller(pattern = "(devy)#(get)#(TRIVIA|trivia)", events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveRequestTrivia(WebSocketSession session, Event event, Matcher matcher) {
        Trivia t = triviaService.getTriviaForToday();
        reply(session, event, mf.getTriviaMessage(t));
    }

    @Controller(pattern = "(devy)#(get)#(DEX-WOTD|dex-wotd)", events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveRequestWordOfTheDay(WebSocketSession session, Event event, Matcher matcher) {
        Word w = dictionaryService.getRomanianWordOfTheDay();
        reply(session, event, mf.getDictionaryMessage(w));
    }

    @Controller(pattern = "(devy)#(get)#(DEX|dex)#(\\w+)", events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveRequestWordDefinition(WebSocketSession session, Event event, Matcher matcher) {
        Word w = dictionaryService.getRomanianDefinitionFor(matcher.group(4));
        reply(session, event, mf.getDictionaryMessage(w));
    }

    @Controller(pattern = "(devy)#(get)#(COMPLEX|complex)", events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveRequestComplex(WebSocketSession session, Event event, Matcher matcher) {
        Trivia t = triviaService.getTriviaForToday();
        Weather w = weatherService.getWeatherFor(defaultCity);
        Note n = noteService.getRandomNoteByType(NoteType.SOFTWARE);
        Quote q = quoteService.getQuoteOfTheDay();
        Word wd = dictionaryService.getRomanianWordOfTheDay();
        reply(session, event, mf.getComplexMessage(n, w, q, t, wd));
    }

    @Controller(events = EventType.PIN_ADDED)
    public void onPinAdded(WebSocketSession session, Event event) {
        reply(session, event, "Thanks master for the pin! You can find all pinned items under channel details.");
    }


}
