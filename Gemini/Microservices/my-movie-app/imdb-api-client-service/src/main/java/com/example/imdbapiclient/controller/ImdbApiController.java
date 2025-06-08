package com.example.imdbapiclient.controller;

import com.example.imdbapiclient.dto.Movie;
import com.example.imdbapiclient.exception.MovieNotFoundException;
import com.example.imdbapiclient.service.ImdbApiClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/imdb")
@RequiredArgsConstructor
@Slf4j
public class ImdbApiController {

    private final ImdbApiClientService imdbApiClientService;

    @GetMapping("/movie")
    public ResponseEntity<Movie> getMovieByTitle(@RequestParam String title) {
        log.info("IMDB API Client: Received request for movie title: {}", title);
        return imdbApiClientService.findMovieByTitle(title)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found on IMDB for title: " + title));
    }
}