package com.example.movie_search_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieResponse {
    private String title;
    private String year;
    @JsonProperty("runningTimeInMinutes")
    private Integer runningTimeInMinutes;
    private String leadActor;

    // Constructors
    public MovieResponse() {}

    public MovieResponse(String title, String year, Integer runningTimeInMinutes, String leadActor) {
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

    public Integer getRunningTimeInMinutes() {
        return runningTimeInMinutes;
    }

    public void setRunningTimeInMinutes(Integer runningTimeInMinutes) {
        this.runningTimeInMinutes = runningTimeInMinutes;
    }

    public String getLeadActor() {
        return leadActor;
    }

    public void setLeadActor(String leadActor) {
        this.leadActor = leadActor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("============== ΑΠΟΤΕΛΕΣΜΑΤΑ ΑΝΑΖΗΤΗΣΗΣ ==============\n");
        sb.append("Τίτλος: ").append(title).append("\n");
        sb.append("Έτος Κυκλοφορίας: ").append(year).append("\n");
        sb.append("Διάρκεια: ").append(runningTimeInMinutes).append(" λεπτά\n");
        sb.append("Πρωταγωνιστής: ").append(leadActor).append("\n");
        sb.append("===================================================\n");
        return sb.toString();
    }
}
