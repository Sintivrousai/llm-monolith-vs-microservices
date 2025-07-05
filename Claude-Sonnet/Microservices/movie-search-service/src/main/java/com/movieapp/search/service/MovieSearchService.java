package com.movieapp.search.service;

import com.movieapp.search.dto.ImdbResponse;
import com.movieapp.search.dto.MovieDto;
import com.movieapp.search.exception.MovieNotFoundException;
import com.movieapp.search.exception.ExternalApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.Optional;

@Service
public class MovieSearchService {

    private final WebClient webClient;
    private final String rapidApiKey;
    private final String rapidApiHost;

    public MovieSearchService(WebClient.Builder webClientBuilder,
                              @Value("${rapidapi.key}") String rapidApiKey,
                              @Value("${rapidapi.host}") String rapidApiHost) {
        this.webClient = webClientBuilder
                .baseUrl("https://imdb8.p.rapidapi.com")
                .build();
        this.rapidApiKey = rapidApiKey;
        this.rapidApiHost = rapidApiHost;
    }

    public MovieDto searchMovie(String title) {
        try {
            ImdbResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/title/find")
                            .queryParam("q", title)
                            .build())
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .header("X-RapidAPI-Key", rapidApiKey)
                    .header("X-RapidAPI-Host", rapidApiHost)
                    .retrieve()
                    .bodyToMono(ImdbResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
                throw new MovieNotFoundException("Δεν βρέθηκαν αποτελέσματα για την ταινία: " + title);
            }

            return convertToMovieDto(response.getResults().get(0));

        } catch (WebClientResponseException e) {
            handleWebClientException(e, title);
            return null; // Δεν θα φτάσει ποτέ εδώ
        } catch (Exception e) {
            throw new ExternalApiException("Σφάλμα κατά την επικοινωνία με την εξωτερική υπηρεσία: " + e.getMessage());
        }
    }

    private void handleWebClientException(WebClientResponseException e, String title) {
        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new MovieNotFoundException("Δεν βρέθηκαν αποτελέσματα για την ταινία: " + title);
        } else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            throw new ExternalApiException("Μη εξουσιοδοτημένη πρόσβαση στην εξωτερική υπηρεσία");
        } else if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
            throw new ExternalApiException("Πάρα πολλές αιτήσεις προς την εξωτερική υπηρεσία");
        } else {
            throw new ExternalApiException("Σφάλμα API: " + e.getStatusCode() + " - " + e.getMessage());
        }
    }

    private MovieDto convertToMovieDto(ImdbResponse.ImdbResult result) {
        String leadActor = extractLeadActor(result);

        return new MovieDto(
                result.getTitle(),
                result.getYear() != null ? result.getYear().toString() : "Δεν διατίθεται",
                result.getRunningTimeInMinutes() != null ? result.getRunningTimeInMinutes().toString() : "Δεν διατίθεται",
                leadActor
        );
    }

    private String extractLeadActor(ImdbResponse.ImdbResult result) {
        return Optional.ofNullable(result.getPrincipals())
                .flatMap(principals -> principals.stream()
                        .filter(p -> "actor".equalsIgnoreCase(p.getCategory()) ||
                                "actress".equalsIgnoreCase(p.getCategory()))
                        .findFirst()
                        .map(ImdbResponse.ImdbResult.Principal::getName))
                .orElse("Δεν διατίθεται");
    }
}