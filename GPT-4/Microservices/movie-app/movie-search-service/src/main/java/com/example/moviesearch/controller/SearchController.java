package com.example.moviesearch.controller;

import com.example.moviesearch.dto.MovieDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@RestController
@RequestMapping("/api/v1/movies")
public class SearchController {

    @Value("${rapidapi.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("title") String title) {
        try {
            String encodedTitle;
            try {
                encodedTitle = URLEncoder.encode(title, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Encoding error", e);
            }
            String url = "https://imdb8.p.rapidapi.com/title/find?q=" + encodedTitle;

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-RapidAPI-Key", apiKey);
            headers.set("X-RapidAPI-Host", "imdb8.p.rapidapi.com");

            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, request, JsonNode.class);
            JsonNode results = response.getBody().get("results");

            if (results != null && results.isArray() && results.size() > 0) {
                JsonNode first = results.get(0);
                MovieDTO dto = new MovieDTO(
                        first.path("title").asText(),
                        first.path("year").asText(),
                        first.path("runningTimeInMinutes").asInt(0),
                        first.path("principals").get(0).path("name").asText()
                );
                return ResponseEntity.ok(dto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No results found");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while calling IMDB API: " + e.getMessage());
        }
    }
}