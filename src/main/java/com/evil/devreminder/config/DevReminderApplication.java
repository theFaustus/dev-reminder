package com.evil.devreminder.config;

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
        };
    }

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
