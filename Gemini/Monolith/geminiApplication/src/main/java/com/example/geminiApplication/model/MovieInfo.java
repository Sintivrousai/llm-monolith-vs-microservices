package com.example.geminiApplication.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieInfo {
    private String title;
    private Integer year;
    private Integer runningTimeInMinutes;
    private String leadActor;
}
