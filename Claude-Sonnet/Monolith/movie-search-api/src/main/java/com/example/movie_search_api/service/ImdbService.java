package com.example.movie_search_api.service;

import com.example.movie_search_api.model.MovieResponse;
import com.example.movie_search_api.model.external.ImdbApiResponse;
import com.example.movie_search_api.model.external.ImdbTitle;
import com.example.movie_search_api.model.external.Principal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;

@Service
public class ImdbService {

    private final WebClient webClient;

    @Value("${imdb.api.key}")
    private String apiKey;

    @Value("${imdb.api.host}")
    private String apiHost;

    @Value("${imdb.api.base-url}")
    private String baseUrl;

    public ImdbService() {
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    public MovieResponse searchMovie(String title) {
        try {
            ImdbApiResponse response = webClient.get()
                    .uri(baseUrl + "/title/find?q={title}", title)
                    .header("X-RapidAPI-Key", apiKey)
                    .header("X-RapidAPI-Host", apiHost)
                    .retrieve()
                    .bodyToMono(ImdbApiResponse.class)
                    .doOnError(error -> System.err.println("API Error: " + error.getMessage()))
                    .block();

            if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
                throw new RuntimeException("Δεν βρέθηκαν αποτελέσματα για την ταινία: " + title);
            }

            // Παίρνουμε το πρώτο αποτέλεσμα
            ImdbTitle movie = response.getResults().get(0);

            // Βρίσκουμε τον πρωταγωνιστή
            String leadActor = findLeadActor(movie);

            return new MovieResponse(
                    movie.getTitle(),
                    movie.getYear() != null ? movie.getYear().toString() : "Άγνωστο",
                    movie.getRunningTimeInMinutes(),
                    leadActor
            );

        } catch (WebClientResponseException e) {
            System.err.println("HTTP Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Σφάλμα επικοινωνίας με το IMDb API: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("General Error: " + e.getMessage());
            throw new RuntimeException("Σφάλμα κατά την αναζήτηση: " + e.getMessage());
        }
    }

    private String findLeadActor(ImdbTitle movie) {
        if (movie.getPrincipals() == null || movie.getPrincipals().length == 0) {
            return "Δεν βρέθηκε";
        }

        // Ψάχνουμε για actor ή actress
        return Arrays.stream(movie.getPrincipals())
                .filter(p -> "actor".equalsIgnoreCase(p.getCategory()) ||
                        "actress".equalsIgnoreCase(p.getCategory()))
                .findFirst()
                .map(Principal::getName)
                .orElse(movie.getPrincipals()[0].getName()); // Αν δεν βρούμε actor, παίρνουμε τον πρώτο
    }
}