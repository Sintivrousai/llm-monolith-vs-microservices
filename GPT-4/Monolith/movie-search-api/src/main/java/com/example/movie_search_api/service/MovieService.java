package com.example.movie_search_api.service;

import com.example.movie_search_api.client.ImdbClient;
import com.example.movie_search_api.dto.MovieResponse;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
    private final ImdbClient imdbClient;

    public MovieService(ImdbClient imdbClient) {
        this.imdbClient = imdbClient;
    }

    public MovieResponse findMovieByTitle(String title) {
        try {
            return imdbClient.searchMovie(title);
        } catch (Exception e) {
            throw new RuntimeException("Σφάλμα κατά την αναζήτηση ταινίας: " + e.getMessage());
        }
    }
}
