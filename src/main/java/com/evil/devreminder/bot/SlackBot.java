package com.evil.devreminder.bot;

import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.NoteType;
import com.evil.devreminder.service.MessageFormatter;
import com.evil.devreminder.service.NoteService;
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

    private final NoteService noteService;
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
        Note note = noteService.getRandomOneByType(NoteType.SOFTWARE);
        Event event = new Event();
        event.setType(EventType.DIRECT_MESSAGE.name());
        event.setChannelId(slackChannelId);
        reply(activeSession, event, mf.bold(note.getNoteType().name()) + "\n" + mf.codified(note.getTitle()) + "\n" + note.getMessage());
    }

    //@Scheduled(cron = "*/10 * * * * *")
    public void sendMotivationNote() {
        Note note = noteService.getRandomOneByType(NoteType.MOTIVATION);
        Event event = new Event();
        event.setType(EventType.DIRECT_MESSAGE.name());
        event.setChannelId(slackChannelId);
        reply(activeSession, event, mf.bold(note.getNoteType().name()) + "\n" + mf.codified(note.getTitle()) + "\n" + note.getMessage());
    }


    @Controller(events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveUnknown(WebSocketSession session, Event event) {
        reply(session, event, "Hi, I am " + slackService.getCurrentUser().getName() + ". My creator is Ion Pascari. " +
                "My purpose is to serve him. Type <help> to see what can I do");
    }

    @Controller(pattern = "help", events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveHelp(WebSocketSession session, Event event) {
        reply(session, event,
                "Well, let`s see... Here is what can I do :\n" +
                        "* `devy#get#SOFTWARE|MOTIVATION|software|motivation` - _get random note_\n" +
                        "* `devy#get#QUOTE|quote` - _get random quote_\n" +
                        "* `devy#get#TRIVIA|trivia` - _get a random trivia fact_\n" +
                        "* `devy#get#WEATHER|weather#type your city` - _get weather for today for a city_\n" +
                        "* `devy#get#DEX|dex#type your word` - _get definition for a word (DEX)_\n" +
                        "* `devy#get#DEX-WOTD|DEX-WOTD` - _get word of the day (DEX)_\n" +
                        "* `devy#get#HISTORY|history` - _get a history fact_\n" +
                        "* `devy#add#SOFTWARE|MOTIVATION|software|motivation#type the title#type the description` - _add a new note_");
    }

    @Controller(pattern = "(devy)#(get)#(SOFTWARE|MOTIVATION|software|motivation)", events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveRequestRandomNote(WebSocketSession session, Event event, Matcher matcher) {
        Note note = noteService.getRandomOneByType(NoteType.valueOf(matcher.group(3).toUpperCase()));
        reply(session, event, mf.bold(note.getNoteType().name()) + "\n" + mf.codified(note.getTitle()) + "\n" + note.getMessage());
    }


    @Controller(events = EventType.PIN_ADDED)
    public void onPinAdded(WebSocketSession session, Event event) {
        reply(session, event, "Thanks master for the pin! You can find all pinned items under channel details.");
    }


}
