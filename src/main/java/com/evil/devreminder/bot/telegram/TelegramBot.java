package com.evil.devreminder.bot.telegram;

import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.NoteType;
import com.evil.devreminder.domain.Quote;
import com.evil.devreminder.domain.Weather;
import com.evil.devreminder.domain.WeatherForecast;
import com.evil.devreminder.domain.Word;
import com.evil.devreminder.service.DictionaryService;
import com.evil.devreminder.service.MessageFormatter;
import com.evil.devreminder.service.NoteService;
import com.evil.devreminder.service.QuoteService;
import com.evil.devreminder.service.TriviaService;
import com.evil.devreminder.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static org.telegram.telegrambots.meta.api.methods.ParseMode.HTML;
import static org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN;


@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingCommandBot {
    private final NoteService noteService;
    private final QuoteService quoteService;
    private final WeatherService weatherService;
    private final TriviaService triviaService;
    private final DictionaryService dictionaryService;
    private final MessageFormatter mf;
    @Value("${telegramBotName}")
    private String botName;
    @Value("${telegramBotToken}")
    private String botToken;
    @Value("${telegramChatId}")
    private String chatId;
    @Value("${weather.default.city}")
    private String defaultCity;
    @Value("${weather.default.lat}")
    private double latitude;
    @Value("${weather.default.long}")
    private double longitude;

    public TelegramBot(final NoteService noteService, final QuoteService quoteService,
                       final WeatherService weatherService, final TriviaService triviaService,
                       final DictionaryService dictionaryService, final MessageFormatter mf) {
        this.noteService = noteService;
        this.quoteService = quoteService;
        this.weatherService = weatherService;
        this.triviaService = triviaService;
        this.dictionaryService = dictionaryService;
        this.mf = mf;

        register(new StartCommand());
        register(new ComplexNoteCommand());
        register(new RegularNoteCommand());
        register(new RegularSoftwareNoteCommand(mf, noteService));
        register(new RegularPracticesNoteCommand(mf, noteService));
        register(new RegularMotivationNoteCommand(mf, noteService));
        register(new QuoteOfTheDayCommand(mf, quoteService));
        register(new TriviaCommand(mf, triviaService));
        register(new WeatherCommand(mf, weatherService));
        register(new WeatherForecastCommand(mf, weatherService));
        register(new DexWordOfTheDayCommand(mf, dictionaryService));
        register(new MerriamWordOfTheDayCommand(mf, dictionaryService));

        register(new HelpCommand("help", "Provides help", "Commands are : /complex"));
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onRegister() {
        log.info("TelegramBot. Connection established.");
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void processNonCommandUpdate(final Update update) {
        send(computeTelegramMessage("Hi, I am Devio. My creator is Ion Pascari. " +
                                            "My purpose is to serve him. Type /help to see what can I do or /start for home."));
    }

    private SendMessage computeTelegramMessage(String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.enableWebPagePreview();
        sendMessage.setParseMode(MARKDOWN);
        sendMessage.setText(message);
        sendMessage.setChatId(chatId);
        return sendMessage;
    }

    private void send(SendMessage sendMessage){
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Telegram went down", e);
        }
    }

    @Scheduled(cron = "${note.complex.cron.expression}")
    public void sendComplexNote() {
        Weather w = weatherService.getWeatherFor(defaultCity);
        Note n = noteService.getRandomNoteByType(NoteType.SOFTWARE);
        Quote q = quoteService.getQuoteOfTheDay();
        Word wd = dictionaryService.getRomanianWordOfTheDay();
        List<WeatherForecast> wf = weatherService.getWeatherForecastFor(latitude, longitude);
        final String complexMessage = mf.getComplexMessage(n, w, wf, q, wd);
        send(computeTelegramMessage(complexMessage));
    }

    @Scheduled(cron = "${note.motivation.cron.expression}")
    public void sendMotivationNote() {
        Note note = noteService.getRandomNoteByType(NoteType.MOTIVATION);
        final String noteMessage = mf.getNoteMessage(note);
        send(computeTelegramMessage(noteMessage));
    }

    @Scheduled(cron = "${note.practices.cron.expression}")
    public void sendPracticesNote() {
        Note note = noteService.getRandomNoteByType(NoteType.PRACTICES);
        final String noteMessage = mf.getNoteMessage(note);
        send(computeTelegramMessage(noteMessage));
    }

    @Scheduled(cron = "${note.software.cron.expression}")
    public void sendSoftwareNote() {
        Note note = noteService.getRandomNoteByType(NoteType.SOFTWARE);
        final String noteMessage = mf.getNoteMessage(note);
        send(computeTelegramMessage(noteMessage));
    }

    private class StartCommand extends BotCommand {
        public StartCommand() {
            super("start", "Basic menu");
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            try {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.enableWebPagePreview();
                sendMessage.enableMarkdown(true);
                sendMessage.setParseMode(MARKDOWN);

                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                replyKeyboardMarkup.setResizeKeyboard(true);
                replyKeyboardMarkup.setSelective(true);
                List<KeyboardRow> keyboardRowList = new ArrayList<>();
                KeyboardRow row;

                row=new KeyboardRow();
                row.add("/complex");
                row.add("/regular");
                keyboardRowList.add(row);

                row=new KeyboardRow();
                row.add("/qod");
                row.add("/trivia");
                keyboardRowList.add(row);

                row=new KeyboardRow();
                row.add("/weather");
                row.add("/wthr_dly");
                keyboardRowList.add(row);

                row=new KeyboardRow();
                row.add("/dexwotd");
                row.add("/merrwotd");
                keyboardRowList.add(row);

                replyKeyboardMarkup.setKeyboard(keyboardRowList);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText("Choose an option: ");
                absSender.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private class ComplexNoteCommand extends BotCommand {
        public ComplexNoteCommand() {
            super("complex", "Get a complex note containing most of the features of the bot");
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            try {
                Weather w = weatherService.getWeatherFor(defaultCity);
                List<WeatherForecast> wf = weatherService.getWeatherForecastFor(latitude, longitude);
                Note n = noteService.getRandomNoteByType(NoteType.SOFTWARE);
                Quote q = quoteService.getQuoteOfTheDay();
                Word wd = dictionaryService.getRomanianWordOfTheDay();
                absSender.execute(computeTelegramMessage(mf.getComplexMessage(n, w, wf, q, wd)));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private class RegularNoteCommand extends BotCommand {
        public RegularNoteCommand() {
            super("regular", "Get a simple note by category");
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            try {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.enableWebPagePreview();
                sendMessage.enableMarkdown(true);
                sendMessage.setParseMode(MARKDOWN);

                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                replyKeyboardMarkup.setResizeKeyboard(true);
                replyKeyboardMarkup.setOneTimeKeyboard(true);
                replyKeyboardMarkup.setSelective(true);
                List<KeyboardRow> keyboardRowList = new ArrayList<>();
                KeyboardRow row;

                row=new KeyboardRow();
                row.add("/regular_software");
                keyboardRowList.add(row);

                row=new KeyboardRow();
                row.add("/regular_practices");
                keyboardRowList.add(row);

                row=new KeyboardRow();
                row.add("/regular_motivation");
                keyboardRowList.add(row);

                row=new KeyboardRow();
                row.add("/start");
                keyboardRowList.add(row);

                replyKeyboardMarkup.setKeyboard(keyboardRowList);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText("Choose a category: ");
                absSender.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private class RegularSoftwareNoteCommand extends BotCommand {
        private final MessageFormatter mf;
        private final NoteService noteService;

        public RegularSoftwareNoteCommand(final MessageFormatter mf, final NoteService noteService) {
            super("/regular_software", "Get a simple software note");
            this.mf = mf;
            this.noteService = noteService;
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            final String noteMessage = mf.getNoteMessage(noteService.getRandomNoteByType(NoteType.SOFTWARE));
            try {
                absSender.execute(computeTelegramMessage(noteMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private class RegularPracticesNoteCommand extends BotCommand {
        private final MessageFormatter mf;
        private final NoteService noteService;

        public RegularPracticesNoteCommand(final MessageFormatter mf, final NoteService noteService) {
            super("/regular_practices", "Get a simple practices note");
            this.mf = mf;
            this.noteService = noteService;
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            final String noteMessage = mf.getNoteMessage(noteService.getRandomNoteByType(NoteType.PRACTICES));
            try {
                absSender.execute(computeTelegramMessage(noteMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private class RegularMotivationNoteCommand extends BotCommand {
        private final MessageFormatter mf;
        private final NoteService noteService;

        public RegularMotivationNoteCommand(final MessageFormatter mf, final NoteService noteService) {
            super("/regular_motivation", "Get a simple motivation note");
            this.mf = mf;
            this.noteService = noteService;
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            final String noteMessage = mf.getNoteMessage(noteService.getRandomNoteByType(NoteType.MOTIVATION));
            try {
                absSender.execute(computeTelegramMessage(noteMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private class QuoteOfTheDayCommand extends BotCommand {
        private final MessageFormatter mf;
        private final QuoteService quoteService;

        public QuoteOfTheDayCommand(final MessageFormatter mf, final QuoteService quoteService) {
            super("/qod", "Get quote of the day");
            this.mf = mf;
            this.quoteService = quoteService;
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            final String quoteMessage = mf.getQuoteMessage(quoteService.getQuoteOfTheDay());
            try {
                absSender.execute(computeTelegramMessage(quoteMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private class TriviaCommand extends BotCommand {
        private final MessageFormatter mf;
        private final TriviaService triviaService;

        public TriviaCommand(final MessageFormatter mf, final TriviaService triviaService) {
            super("/trivia", "Get some nice trivia of the day");
            this.mf = mf;
            this.triviaService = triviaService;
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            final String triviaMessage = mf.getTriviaMessage(triviaService.getTriviaForToday());
            try {
                absSender.execute(computeTelegramMessage(triviaMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private class WeatherCommand extends BotCommand {
        private final MessageFormatter mf;
        private final WeatherService weatherService;

        public WeatherCommand(final MessageFormatter mf, final WeatherService weatherService) {
            super("/weather", "Get weather at the moment");
            this.mf = mf;
            this.weatherService = weatherService;
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            final String weatherMessage = mf.getWeatherMessage(weatherService.getWeatherFor(defaultCity));
            try {
                absSender.execute(computeTelegramMessage(weatherMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private class WeatherForecastCommand extends BotCommand {
        private final MessageFormatter mf;
        private final WeatherService weatherService;

        public WeatherForecastCommand(final MessageFormatter mf, final WeatherService weatherService) {
            super("/wthr_dly", "Get weather for days ahead");
            this.mf = mf;
            this.weatherService = weatherService;
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            final String weatherMessage = mf.getWeatherForecastMessage(
                    weatherService.getWeatherForecastFor(latitude, longitude));
            try {
                absSender.execute(computeTelegramMessage(weatherMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private class DexWordOfTheDayCommand extends BotCommand {
        private final MessageFormatter mf;
        private final DictionaryService dictionaryService;

        public DexWordOfTheDayCommand(final MessageFormatter mf, final DictionaryService dictionaryService) {
            super("/dexwotd", "Get DEX word of the day");
            this.mf = mf;
            this.dictionaryService = dictionaryService;
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            final String dictionaryMessage = mf.getDictionaryMessage(dictionaryService.getRomanianWordOfTheDay());
            try {
                absSender.execute(computeTelegramMessage(dictionaryMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private class MerriamWordOfTheDayCommand extends BotCommand {
        private final MessageFormatter mf;
        private final DictionaryService dictionaryService;

        public MerriamWordOfTheDayCommand(final MessageFormatter mf, final DictionaryService dictionaryService) {
            super("/merrwotd", "Get Merriam-Webster word of the day");
            this.mf = mf;
            this.dictionaryService = dictionaryService;
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            final String dictionaryMessage = mf.getDictionaryMessage(dictionaryService.getEnglishWordOfTheDay());
            try {
                final SendMessage message = computeTelegramMessage(dictionaryMessage);

                absSender.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}