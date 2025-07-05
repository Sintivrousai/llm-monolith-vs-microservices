package com.movieapp.client;

import com.movieapp.client.service.MovieClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieClientServiceApplication implements CommandLineRunner {

	private final MovieClientService movieClientService;

	public MovieClientServiceApplication(MovieClientService movieClientService) {
		this.movieClientService = movieClientService;
	}

	public static void main(String[] args) {
		SpringApplication.run(MovieClientServiceApplication.class, args);
	}

	@Override
	public void run(String... args) {
		movieClientService.startMovieSearch();
	}
}