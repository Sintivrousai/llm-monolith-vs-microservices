package com.example.moviesearch.service;

import com.example.moviesearch.dto.Movie;
import com.example.moviesearch.exception.ImdbApiClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final RestTemplate restTemplate;

    @Value("${imdb.api.client.service.url}")
    private String imdbApiClientServiceUrl;

    public Optional<Movie> findMovieByTitle(String title) {
        try {
            String url = imdbApiClientServiceUrl + "/api/imdb/movie?title=" + title;
            log.debug("Calling IMDB API Client Service at: {}", url);

            // Χρησιμοποιούμε exchange για να χειριστούμε την απάντηση πιο αναλυτικά,
            // ειδικά αν το client service μπορεί να επιστρέψει null (που το DTO δεν μπορεί να είναι)
            ResponseEntity<Movie> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null, // Request entity, null for GET
                    new ParameterizedTypeReference<Movie>() {} // Specifies the expected return type
            );

            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Movie not found by IMDB API Client for title: {}", title);
            return Optional.empty(); // Προώθηση του "δεν βρέθηκε" ως Optional.empty()
        } catch (HttpClientErrorException e) {
            log.error("Client error calling IMDB API Client Service: Status {} - Body {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new ImdbApiClientException("Error from IMDB API Client Service: " + e.getResponseBodyAsString(), e);
        } catch (HttpServerErrorException e) {
            log.error("Server error calling IMDB API Client Service: Status {} - Body {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new ImdbApiClientException("IMDB API Client Service experienced a server error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("An unexpected error occurred while calling IMDB API Client Service for title {}: {}", title, e.getMessage(), e);
            throw new ImdbApiClientException("An unexpected error occurred while processing movie search: " + e.getLocalizedMessage(), e);
        }
    }
}