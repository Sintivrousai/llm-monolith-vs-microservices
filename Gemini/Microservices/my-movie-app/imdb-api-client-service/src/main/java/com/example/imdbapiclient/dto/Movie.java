package com.example.imdbapiclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    private String title;
    private Integer year;
    private Integer runningTimeInMinutes;
    private String leadActor;
}