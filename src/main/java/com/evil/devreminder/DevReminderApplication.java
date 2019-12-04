package com.evil.devreminder;

import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.NoteType;
import com.evil.devreminder.repository.NoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class DevReminderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevReminderApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(NoteRepository noteRepository) {
		return (args) -> {
			noteRepository.deleteAll();
			noteRepository.save(new Note("Read", "Read every day", NoteType.CUSTOM));
			noteRepository.save(new Note("Effective Java", "Item 48", NoteType.SIMPLE));
			noteRepository.save(new Note("Motivation", "Be better", NoteType.MOTIVATION));
			noteRepository.save(new Note("Quote of the day", "Hmmm", NoteType.QUOTE));
		};
	}
}
