package com.example.movie_search_api.controller;

import com.example.movie_search_api.model.ErrorResponse;
import com.example.movie_search_api.model.MovieResponse;
import com.example.movie_search_api.service.ImdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private ImdbService imdbService;

    @GetMapping("/search")
    public ResponseEntity<?> searchMovie(@RequestParam String title) {
        try {
            if (title == null || title.trim().isEmpty()) {
                ErrorResponse error = new ErrorResponse(
                        "INVALID_INPUT",
                        "Ο τίτλος της ταινίας είναι υποχρεωτικός"
                );
                return ResponseEntity.badRequest().body(error);
            }

            MovieResponse movie = imdbService.searchMovie(title.trim());
            return ResponseEntity.ok(movie);

        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                    "SEARCH_ERROR",
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    "UNKNOWN_ERROR",
                    "Προέκυψε ένα απροσδόκητο σφάλμα: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}