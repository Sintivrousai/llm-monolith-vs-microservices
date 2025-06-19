package com.example.moviesearch.dto;

public class MovieDTO {
    private String title;
    private String year;
    private int runningTimeInMinutes;
    private String leadActor;

    public MovieDTO() {}

    public MovieDTO(String title, String year, int runningTimeInMinutes, String leadActor) {
        this.title = title;
        this.year = year;
        this.runningTimeInMinutes = runningTimeInMinutes;
        this.leadActor = leadActor;
    }

    // getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public int getRunningTimeInMinutes() { return runningTimeInMinutes; }
    public void setRunningTimeInMinutes(int runningTimeInMinutes) { this.runningTimeInMinutes = runningTimeInMinutes; }

    public String getLeadActor() { return leadActor; }
    public void setLeadActor(String leadActor) { this.leadActor = leadActor; }
}
