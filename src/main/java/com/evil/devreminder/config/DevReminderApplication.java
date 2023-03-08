package com.evil.devreminder.config;

import com.evil.devreminder.bot.telegram.TelegramBot;
import com.evil.devreminder.domain.CryptoFearGreedIndex;
import com.evil.devreminder.service.CSVReaderService;
import com.evil.devreminder.service.CryptoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.FileInputStream;

@SpringBootApplication(scanBasePackages = {"me.ramswaroop.jbot", "com.evil.devreminder"})
@EnableScheduling
@EnableMongoRepositories(basePackages = "com.evil.devreminder.repository")
@Slf4j
public class DevReminderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevReminderApplication.class, args);
    }

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public CommandLineRunner commandLineRunner(TelegramBot telegramBot, CryptoService cryptoService){
    	return args -> {
//    		csvReaderService.process(new FileInputStream("C:\\Users\\ipascari\\notes.csv"));
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(telegramBot);
		};
	}
}
