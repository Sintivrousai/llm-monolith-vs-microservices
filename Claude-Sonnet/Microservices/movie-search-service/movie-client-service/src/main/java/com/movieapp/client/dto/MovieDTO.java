package com.movieapp.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {
    private String title;
    private String year;
    private String runningTimeInMinutes;
    private String leadActor;

    // Constructors
    public MovieDTO() {}

    public MovieDTO(String title, String year, String runningTimeInMinutes, String leadActor) {
        this.title = title;
        this.year = year;
        this.runningTimeInMinutes = runningTimeInMinutes;
        this.leadActor = leadActor;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRunningTimeInMinutes() {
        return runningTimeInMinutes;
    }

    public void setRunningTimeInMinutes(String runningTimeInMinutes) {
        this.runningTimeInMinutes = runningTimeInMinutes;
    }

    public String getLeadActor() {
        return leadActor;
    }

    public void setLeadActor(String leadActor) {
        this.leadActor = leadActor;
    }
}

