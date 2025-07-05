package com.example.movie_search_api.model.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImdbApiResponse {
    @JsonProperty("results")
    private List<ImdbTitle> results;

    public List<ImdbTitle> getResults() {
        return results;
    }

    public void setResults(List<ImdbTitle> results) {
        this.results = results;
    }
}
