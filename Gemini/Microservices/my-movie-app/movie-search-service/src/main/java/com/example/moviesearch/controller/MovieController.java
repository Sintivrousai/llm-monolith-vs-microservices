package com.example.moviesearch.controller;

import com.example.moviesearch.dto.Movie;
import com.example.moviesearch.dto.ErrorResponse;
import com.example.moviesearch.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/search")
    public ResponseEntity<?> searchMovieByTitle(@RequestParam String title) {
        log.info("Received request to search for movie: {}", title);
        Optional<Movie> movie = movieService.findMovieByTitle(title);

        if (movie.isPresent()) {
            log.info("Movie found: {}", movie.get().getTitle());
            return ResponseEntity.ok(movie.get());
        } else {
            log.warn("Movie not found for title: {}", title);
            // Επιστρέφουμε ένα ErrorResponse για 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Movie not found for title: " + title,
                            HttpStatus.NOT_FOUND.value(), "Not Found"));
        }
    }
}