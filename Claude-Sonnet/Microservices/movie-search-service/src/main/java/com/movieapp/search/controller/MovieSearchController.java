package com.movieapp.search.controller;

import com.movieapp.search.dto.MovieDto;
import com.movieapp.search.service.MovieSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
public class MovieSearchController {

    private final MovieSearchService movieSearchService;

    public MovieSearchController(MovieSearchService movieSearchService) {
        this.movieSearchService = movieSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<MovieDto> searchMovie(@RequestParam String title) {
        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        MovieDto movie = movieSearchService.searchMovie(title.trim());
        return ResponseEntity.ok(movie);
    }
}