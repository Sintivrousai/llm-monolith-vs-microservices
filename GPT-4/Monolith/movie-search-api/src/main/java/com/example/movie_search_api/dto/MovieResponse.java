package com.example.movie_search_api.dto;

import lombok.Data;

@Data
public class MovieResponse {
    private String title;
    private String year;
    private int runningTimeInMinutes;
    private String leadActor;
}

