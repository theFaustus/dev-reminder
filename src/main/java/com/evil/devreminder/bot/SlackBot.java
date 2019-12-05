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
import org.springframework.scheduling.annotation.Scheduled;
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

    @Scheduled(cron = "*/10 * * * * *")
    public void sendSimpleNote(){
        Note note = noteService.getRandomOneByType(NoteType.SIMPLE);
        Event event = new Event();
        event.setType(EventType.DIRECT_MESSAGE.name());
        event.setChannelId(slackChannelId);
        reply(activeSession, event, mf.bold(note.getNoteType().name()) + "\n" + mf.codified(note.getTitle()) + "\n" + note.getMessage());

    }

    @Controller(events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveDM(WebSocketSession session, Event event) {
        reply(session, event, "Hi, I am " + slackService.getCurrentUser().getName() + ". My creator is Ion Pascari. My purpose is to serve him.");
    }

    @Controller(pattern = "(devy)#(get)#(QUOTE|CUSTOM|SIMPLE|MOTIVATION|quote|custom|simple|motivation)", events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveDMRequestRandomNote(WebSocketSession session, Event event, Matcher matcher) {
        Note note = noteService.getRandomOneByType(NoteType.valueOf(matcher.group(3).toUpperCase()));
        reply(session, event, mf.bold(note.getNoteType().name()) + "\n" + mf.codified(note.getTitle()) + "\n" + note.getMessage());
    }

    @Controller(events = EventType.MESSAGE, pattern = "^([a-z ]{2})(\\d+)([a-z ]{2})$")
    public void onReceiveMessage(WebSocketSession session, Event event, Matcher matcher) {
        reply(session, event, "First group: " + matcher.group(0) + "\n" +
                "Second group: " + matcher.group(1) + "\n" +
                "Third group: " + matcher.group(2) + "\n" +
                "Fourth group: " + matcher.group(3));
    }

    @Controller(events = EventType.PIN_ADDED)
    public void onPinAdded(WebSocketSession session, Event event) {
        reply(session, event, "Thanks master for the pin! You can find all pinned items under channel details.");
    }

    @Controller(events = EventType.FILE_SHARED)
    public void onFileShared(WebSocketSession session, Event event) {
        log.info("File shared: {}", event);
    }

    @Controller(pattern = "(setup meeting)", next = "confirmTiming")
    public void setupMeeting(WebSocketSession session, Event event) {
        startConversation(event, "confirmTiming");   // start conversation
        reply(session, event, "Cool! At what time (ex. 15:30) do you want me to set up the meeting?");
    }

    @Controller(next = "askTimeForMeeting")
    public void confirmTiming(WebSocketSession session, Event event) {
        reply(session, event, "Your meeting is set at " + event.getText() +
                ". Would you like to repeat it tomorrow?");
        nextConversation(event);    // jump to next question in conversation
    }

}
