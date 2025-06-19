package com.example.movie_search_api.client;

import com.example.movie_search_api.dto.MovieResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class ImdbClient {
    private static final String API_HOST = "imdb8.p.rapidapi.com";
    private static final String API_KEY = "731ab3ce06msh395acef29479e1fp192013jsn18d94ddc681b";
    private static final String BASE_URL = "https://imdb8.p.rapidapi.com/title/find/?q=";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public MovieResponse searchMovie(String query) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + query))
                .header("x-rapidapi-host", API_HOST)
                .header("x-rapidapi-key", API_KEY)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("IMDB API error: " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        JsonNode results = root.path("results");

        if (!results.isArray() || results.isEmpty()) {
            return null;
        }

        JsonNode first = results.get(0);
        MovieResponse movie = new MovieResponse();
        movie.setTitle(first.path("title").asText());
        movie.setYear(first.path("year").asText(""));
        movie.setRunningTimeInMinutes(first.path("runningTimeInMinutes").asInt(0));

        JsonNode principals = first.path("principals");
        if (principals.isArray() && principals.size() > 0) {
            movie.setLeadActor(principals.get(0).path("name").asText("Unknown"));
        } else {
            movie.setLeadActor("Unknown");
        }

        return movie;
    }
}
