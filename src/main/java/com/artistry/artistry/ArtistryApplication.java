package com.artistry.artistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ArtistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtistryApplication.class, args);
	}

}
