package com.example.imdbapiclient.service;

import com.example.imdbapiclient.dto.ImdbMovieResponse;
import com.example.imdbapiclient.dto.Movie;
import com.example.imdbapiclient.exception.ImdbApiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Service
@Slf4j
public class ImdbApiClientService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ImdbApiClientService(@Value("${rapidapi.imdb.host}") String rapidApiHost,
                                @Value("${rapidapi.imdb.key}") String rapidApiKey,
                                ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.webClient = WebClient.builder()
                .baseUrl("https://" + rapidApiHost)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-RapidAPI-Key", rapidApiKey)
                .defaultHeader("X-RapidAPI-Host", rapidApiHost)
                .build();
    }

    public Optional<Movie> findMovieByTitle(String title) {
        log.info("Searching for movie with title: {}", title);
        try {
            String responseBody = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/title/find")
                            .queryParam("q", title)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Always log the raw response for debugging!
            log.info("Raw response from IMDB API for title '{}': {}", title, responseBody);

            if (responseBody == null || responseBody.isEmpty()) {
                log.warn("Empty or null response received for movie title: {}", title);
                return Optional.empty();
            }

            ImdbMovieResponse imdbResponse = objectMapper.readValue(responseBody, ImdbMovieResponse.class);

            if (imdbResponse != null && imdbResponse.getResults() != null && !imdbResponse.getResults().isEmpty()) {
                Optional<JsonNode> movieNode = imdbResponse.getResults().stream()
                        .filter(node -> node.has("title")
                                && node.has("year")
                                // REMOVED: node.has("runningTimeInMinutes") - as it's not always in /find
                                && node.has("principals") // Still check for principals for the actor
                                // CHANGED: "titleType" field and filter for "movie"
                                && "movie".equalsIgnoreCase(node.path("titleType").asText()))
                        .findFirst();

                if (movieNode.isPresent()) {
                    JsonNode node = movieNode.get();
                    String movieTitle = node.get("title").asText();
                    Integer year = node.get("year").asInt();
                    // Since runningTimeInMinutes is not reliable in /find, initialize it as null or a default
                    Integer runningTimeInMinutes = null; // Or 0, or add logic for a second API call later

                    // This part is fine for getting the lead actor
                    String leadActor = "N/A";
                    if (node.has("principals") && node.get("principals").isArray() && node.get("principals").size() > 0) {
                        JsonNode firstPrincipal = node.get("principals").get(0);
                        if (firstPrincipal.has("name")) {
                            leadActor = firstPrincipal.get("name").asText();
                        }
                    }

                    Movie movie = new Movie(movieTitle, year, runningTimeInMinutes, leadActor);
                    log.info("Found movie: {}", movie);
                    return Optional.of(movie);
                } else {
                    log.info("No relevant movie data (matching 'movie' type with title/year/principals) found in IMDB API response for title: {}", title);
                    return Optional.empty(); // No movie found with the required fields or type
                }
            } else {
                log.info("IMDB API returned no results for title: {}", title);
                return Optional.empty(); // No results at all
            }

        } catch (WebClientResponseException e) {
            log.error("Error calling IMDB API for title {}: Status {} - {}", title, e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new ImdbApiException("Failed to retrieve movie data from IMDB API: " + e.getLocalizedMessage(), e);
        } catch (Exception e) {
            log.error("An unexpected error occurred while processing IMDB API response for title {}: {}", title, e.getMessage(), e);
            throw new ImdbApiException("An unexpected error occurred: " + e.getLocalizedMessage(), e);
        }
    }
}