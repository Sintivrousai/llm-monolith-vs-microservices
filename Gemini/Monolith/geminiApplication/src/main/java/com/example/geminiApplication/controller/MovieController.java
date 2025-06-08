package com.example.geminiApplication.controller;

import com.example.geminiApplication.service.ImdbApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final ImdbApiService movieService;

    public MovieController(ImdbApiService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/search") // Changed endpoint path for clarity if needed, or keep @GetMapping
    public Mono<ResponseEntity<?>> searchMovieByTitle(@RequestParam String title) {
        if (title == null || title.trim().isEmpty()) {
            // Return a Mono containing a bad request response
            return Mono.just(ResponseEntity.badRequest().body(Map.of("message", "Ο τίτλος της ταινίας είναι υποχρεωτικός.")));
        }

        // Corrected method name: movieService.searchMovie(title)
        // Now handle the Mono<Optional<MovieInfo>> returned by searchMovie
        return movieService.searchMovie(title)
                .map(movieInfoOptional -> { // movieInfoOptional is Optional<MovieInfo>
                    if (movieInfoOptional.isPresent()) {
                        return ResponseEntity.ok(movieInfoOptional.get());
                    } else {
                        // Movie not found, return 404
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("message", "Δεν βρέθηκε ταινία με τον τίτλο: " + title));
                    }
                })
                .onErrorResume(e -> {
                    // This handles any errors that occur during the reactive stream processing
                    // including issues during the WebClient call or data transformation.
                    System.err.println("Σφάλμα κατά την αναζήτηση ταινίας: " + e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("message", "Προέκυψε σφάλμα κατά την αναζήτηση ταινίας: " + e.getMessage())));
                });
    }
}
