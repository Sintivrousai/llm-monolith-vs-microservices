package com.example.movie_search_api.controller;

import com.example.movie_search_api.dto.MovieResponse;
import com.example.movie_search_api.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<?> getMovieByTitle(@RequestParam String title) {
        MovieResponse response = movieService.findMovieByTitle(title);
        if (response == null) {
            return ResponseEntity.status(404).body("Δεν βρέθηκε καμία ταινία με τον τίτλο: " + title);
        }
        return ResponseEntity.ok(response);
    }
}
