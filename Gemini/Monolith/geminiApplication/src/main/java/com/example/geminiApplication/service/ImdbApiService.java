package com.example.geminiApplication.service;

import com.example.geminiApplication.model.ImdbApiResponse;
import com.example.geminiApplication.model.MovieInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class ImdbApiService {

    private final WebClient webClient;

    @Value("${imdb.api.key}")
    private String rapidApiKey;

    @Value("${imdb.api.host}")
    private String rapidApiHost;

    @Value("${imdb.api.url}")
    private String imdbApiUrl;

    public ImdbApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<Optional<MovieInfo>> searchMovie(String title) {
        String uri = UriComponentsBuilder.fromUriString(imdbApiUrl)
                .queryParam("q", title)
                .toUriString();

        return webClient.get()
                .uri(uri)
                .header("X-RapidAPI-Key", rapidApiKey)
                .header("X-RapidAPI-Host", rapidApiHost)
                .retrieve()
                .bodyToMono(ImdbApiResponse.class)
                .map(response -> {
                    if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
                        Optional<ImdbApiResponse.MovieResult> movieResult = response.getResults().stream()
                                .filter(r -> "movie".equalsIgnoreCase(r.getTitleType()) && r.getPrincipals() != null && !r.getPrincipals().isEmpty())
                                .findFirst();

                        if (movieResult.isPresent()) {
                            ImdbApiResponse.MovieResult m = movieResult.get();
                            String leadActor = m.getPrincipals().stream()
                                    .filter(p -> "actor".equalsIgnoreCase(p.getCategory()))
                                    .map(ImdbApiResponse.MovieResult.Principal::getName)
                                    .findFirst()
                                    .orElse("N/A");

                            return Optional.of(MovieInfo.builder()
                                    .title(m.getTitle())
                                    .year(m.getYear())
                                    .runningTimeInMinutes(null) // This still needs a second API call for real data
                                    .leadActor(leadActor)
                                    .build());
                        }
                    }
                    // **THE FIX:** Explicitly specify the generic type for Optional.empty()
                    return Optional.<MovieInfo>empty();
                })
                .onErrorResume(e -> {
                    System.err.println("Error calling IMDB API: " + e.getMessage());
                    // **THE FIX:** Explicitly specify the generic type for Mono.just(Optional.empty())
                    return Mono.just(Optional.<MovieInfo>empty());
                });
    }
}