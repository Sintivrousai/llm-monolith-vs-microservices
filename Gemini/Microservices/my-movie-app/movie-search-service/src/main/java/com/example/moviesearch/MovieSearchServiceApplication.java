package com.example.moviesearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MovieSearchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieSearchServiceApplication.class, args);
	}

	@Bean // Δηλώνει ένα RestTemplate bean για HTTP κλήσεις σε άλλα services
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
