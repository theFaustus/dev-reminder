package com.evil.devreminder.config;

import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.NoteType;
import com.evil.devreminder.repository.NoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = {"me.ramswaroop.jbot", "com.evil.devreminder"})
@EnableScheduling
@EnableMongoRepositories(basePackages = "com.evil.devreminder.repository")
@Slf4j
public class DevReminderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevReminderApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(NoteRepository noteRepository) {
        return (args) -> {
            noteRepository.deleteAll();
            noteRepository.save(new Note("Effective Java", "Item 48", NoteType.SOFTWARE));
            noteRepository.save(new Note("Effective Java 3rd", "Item 72", NoteType.SOFTWARE));
            noteRepository.save(new Note("Motivation", "Be better", NoteType.SOFTWARE));
            noteRepository.save(new Note("Motivation / Work", "Be better", NoteType.SOFTWARE));
            noteRepository.save(new Note("Quote of the day", "Hmmm", NoteType.MOTIVATION));
            noteRepository.save(new Note("Quote of the month", "Hrrrmmm", NoteType.MOTIVATION));
        };
    }

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
