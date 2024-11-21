package be.ucll.se.team15backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Team15BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Team15BackendApplication.class, args);
	}

}
