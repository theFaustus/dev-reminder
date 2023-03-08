package com.evil.devreminder.bot.telegram;

import com.evil.devreminder.domain.Crypto;
import com.evil.devreminder.domain.CryptoFearGreedIndex;
import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.NoteType;
import com.evil.devreminder.domain.Picture;
import com.evil.devreminder.domain.Quote;
import com.evil.devreminder.domain.Weather;
import com.evil.devreminder.domain.WeatherForecast;
import com.evil.devreminder.domain.Word;
import com.evil.devreminder.service.CryptoService;
import com.evil.devreminder.service.DictionaryService;
import com.evil.devreminder.service.MessageFormatter;
import com.evil.devreminder.service.NoteService;
import com.evil.devreminder.service.PictureService;
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
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.telegram.telegrambots.meta.api.methods.ParseMode.MARKDOWN;


@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingCommandBot {
    private final NoteService noteService;
    private final QuoteService quoteService;
    private final WeatherService weatherService;
    private final TriviaService triviaService;
    private final PictureService pictureService;
    private final DictionaryService dictionaryService;
    private final CryptoService cryptoService;
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
                       final PictureService pictureService,
                       final DictionaryService dictionaryService,
                       final CryptoService cryptoService, final MessageFormatter mf) {
        this.noteService = noteService;
        this.quoteService = quoteService;
        this.weatherService = weatherService;
        this.triviaService = triviaService;
        this.pictureService = pictureService;
        this.dictionaryService = dictionaryService;
        this.cryptoService = cryptoService;
        this.mf = mf;

        register(new StartCommand());
        register(new ComplexNoteCommand());
        register(new RegularMotivationNoteCommand(mf));
        register(new RegularSoftwareNoteCommand(mf));
        register(new RegularPracticesNoteCommand(mf));
        register(new RegularSpringNoteCommand(mf));
        register(new RegularJpaNoteCommand(mf));
        register(new SearchSoftwareNoteCommand(mf));
        register(new SearchSpringNoteCommand(mf));
        register(new SearchJpaNoteCommand(mf));
        register(new QuoteOfTheDayCommand(mf));
        register(new TriviaCommand(mf));
        register(new PictureOfTheDayCommand(mf));
        register(new WeatherCommand(mf));
        register(new WeatherForecastCommand(mf));
        register(new CryptoCommand(mf));
        register(new DexWordOfTheDayCommand(mf));
        register(new MerriamWordOfTheDayCommand(mf));
        register(new RegularNoteCommand());
        register(new HelpCommand());

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

    private void send(SendMessage sendMessage) {
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
        Picture p = pictureService.getPictureOfTheDay();
        CryptoFearGreedIndex cfgi = cryptoService.getCryptoFearGreedIndex();
        List<Crypto> cs = cryptoService.getTop5Cryptos();
        List<WeatherForecast> wf = weatherService.getWeatherForecastFor(latitude, longitude);
        final String complexMessage = mf.getComplexMessage(n, w, wf, q, wd, p, cfgi, cs);
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

    @Scheduled(cron = "${note.spring.cert.cron.expression}")
    public void sendSpringNote() {
        Note note = noteService.getRandomNoteByType(NoteType.SPRING);
        final String noteMessage = mf.getSpringNoteMessage(note);
        send(computeTelegramMessage(noteMessage));
    }

    @Scheduled(cron = "${note.jpa.cron.expression}")
    public void sendJpaNote() {
        Note note = noteService.getRandomNoteByType(NoteType.JPA);
        final String noteMessage = mf.getJpaNoteMessage(note);
        send(computeTelegramMessage(noteMessage));
    }

    private class StartCommand extends BotCommand {
        public StartCommand() {
            super("/start", "Basic menu");
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

                row = new KeyboardRow();
                row.add("/complex");
                row.add("/regular");
                keyboardRowList.add(row);

                row = new KeyboardRow();
                row.add("/qod");
                row.add("/trivia");
                keyboardRowList.add(row);

                row = new KeyboardRow();
                row.add("/weather");
                row.add("/wthr_dly");
                keyboardRowList.add(row);

                row = new KeyboardRow();
                row.add("/dexwotd");
                row.add("/merrwotd");
                keyboardRowList.add(row);

                row = new KeyboardRow();
                row.add("/potd");
                row.add("/crypto");
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
            super("/complex", "get a complex note containing most of the features of the bot");
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            try {
                Weather w = weatherService.getWeatherFor(defaultCity);
                List<WeatherForecast> wf = weatherService.getWeatherForecastFor(latitude, longitude);
                Note n = noteService.getRandomNoteByType(NoteType.SOFTWARE);
                Quote q = quoteService.getQuoteOfTheDay();
                Word wd = dictionaryService.getRomanianWordOfTheDay();
                Picture p = pictureService.getPictureOfTheDay();
                CryptoFearGreedIndex cfgi = cryptoService.getCryptoFearGreedIndex();
                List<Crypto> cs = cryptoService.getTop5Cryptos();
                absSender.execute(computeTelegramMessage(mf.getComplexMessage(n, w, wf, q, wd, p, cfgi, cs)));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private class RegularNoteCommand extends BotCommand {
        public RegularNoteCommand() {
            super("/regular", "get a simple note by category");
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

                row = new KeyboardRow();
                row.add("/regular_software");
                keyboardRowList.add(row);

                row = new KeyboardRow();
                row.add("/regular_practices");
                keyboardRowList.add(row);

                row = new KeyboardRow();
                row.add("/regular_motivation");
                keyboardRowList.add(row);

                row = new KeyboardRow();
                row.add("/regular_spring");
                keyboardRowList.add(row);

                row = new KeyboardRow();
                row.add("/regular_jpa");
                keyboardRowList.add(row);

                row = new KeyboardRow();
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

        public RegularSoftwareNoteCommand(final MessageFormatter mf) {
            super("/regular_software", "get a simple software note");
            this.mf = mf;
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

    private class SearchSoftwareNoteCommand extends BotCommand {
        private final MessageFormatter mf;

        public SearchSoftwareNoteCommand(final MessageFormatter mf) {
            super("/search_software", "search for a simple software note");
            this.mf = mf;
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            final List<Note> notes = noteService.searchNote(NoteType.SOFTWARE, strings);
            notes.forEach(n -> {
                try {
                    absSender.execute(computeTelegramMessage(mf.getNoteMessage(n)));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private class RegularSpringNoteCommand extends BotCommand {
        private final MessageFormatter mf;

        public RegularSpringNoteCommand(final MessageFormatter mf) {
            super("/regular_spring", "get a simple spring note");
            this.mf = mf;
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            final String noteMessage = mf.getSpringNoteMessage(noteService.getRandomNoteByType(NoteType.SPRING));
            try {
                absSender.execute(computeTelegramMessage(noteMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private class SearchSpringNoteCommand extends BotCommand {
        private final MessageFormatter mf;

        public SearchSpringNoteCommand(final MessageFormatter mf) {
            super("/search_spring", "search for a spring note");
            this.mf = mf;
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            final List<Note> notes = noteService.searchNote(NoteType.SPRING, strings);
            notes.forEach(n -> {
                try {
                    absSender.execute(computeTelegramMessage(mf.getSpringNoteMessage(n)));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private class SearchJpaNoteCommand extends BotCommand {
        private final MessageFormatter mf;

        public SearchJpaNoteCommand(final MessageFormatter mf) {
            super("/search_jpa", "search for a jpa note");
            this.mf = mf;
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            final List<Note> notes = noteService.searchNote(NoteType.JPA, strings);
            notes.forEach(n -> {
                try {
                    absSender.execute(computeTelegramMessage(mf.getJpaNoteMessage(n)));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private class RegularJpaNoteCommand extends BotCommand {
        private final MessageFormatter mf;

        public RegularJpaNoteCommand(final MessageFormatter mf) {
            super("/regular_jpa", "get a simple jpa note");
            this.mf = mf;
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            final String noteMessage = mf.getJpaNoteMessage(noteService.getRandomNoteByType(NoteType.JPA));
            try {
                absSender.execute(computeTelegramMessage(noteMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private class RegularPracticesNoteCommand extends BotCommand {
        private final MessageFormatter mf;

        public RegularPracticesNoteCommand(final MessageFormatter mf) {
            super("/regular_practices", "get a simple practices note");
            this.mf = mf;
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

        public RegularMotivationNoteCommand(final MessageFormatter mf) {
            super("/regular_motivation", "get a simple motivation note");
            this.mf = mf;
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

        public QuoteOfTheDayCommand(final MessageFormatter mf) {
            super("/qod", "get quote of the day");
            this.mf = mf;
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

        public TriviaCommand(final MessageFormatter mf) {
            super("/trivia", "get some nice trivia of the day");
            this.mf = mf;
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

        public WeatherCommand(final MessageFormatter mf) {
            super("/weather", "get weather at the moment");
            this.mf = mf;
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

        public WeatherForecastCommand(final MessageFormatter mf) {
            super("/wthr_dly", "get weather for days ahead");
            this.mf = mf;
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

    private class CryptoCommand extends BotCommand {
        private final MessageFormatter mf;

        public CryptoCommand(final MessageFormatter mf) {
            super("/crypto", "get crypto fear index and top 5 cryptos");
            this.mf = mf;
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            final CryptoFearGreedIndex cryptoFearGreedIndex = cryptoService.getCryptoFearGreedIndex();
            final List<Crypto> cryptos = cryptoService.getTop5Cryptos();
            final String cryptoMessage = mf.getCryptoMessage(cryptoFearGreedIndex, cryptos, 5);
            try {
                absSender.execute(computeTelegramMessage(cryptoMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private class DexWordOfTheDayCommand extends BotCommand {
        private final MessageFormatter mf;

        public DexWordOfTheDayCommand(final MessageFormatter mf) {
            super("/dexwotd", "get DEX word of the day");
            this.mf = mf;
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

        public MerriamWordOfTheDayCommand(final MessageFormatter mf) {
            super("/merrwotd", "get Merriam-Webster word of the day");
            this.mf = mf;
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

    private class PictureOfTheDayCommand extends BotCommand {
        private final MessageFormatter mf;

        public PictureOfTheDayCommand(final MessageFormatter mf) {
            super("/potd", "get picture of the day from NASA");
            this.mf = mf;
        }

        @Override
        public void execute(final AbsSender absSender, final User user, final Chat chat, final String[] strings) {
            final String triviaMessage = mf.getPictureMessage(pictureService.getPictureOfTheDay());
            try {
                absSender.execute(computeTelegramMessage(triviaMessage));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}