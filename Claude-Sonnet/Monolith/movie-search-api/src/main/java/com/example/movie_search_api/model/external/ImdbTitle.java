package com.example.movie_search_api.model.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImdbTitle {
    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("runningTimeInMinutes")
    private Integer runningTimeInMinutes;

    @JsonProperty("principals")
    private Principal[] principals;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getRunningTimeInMinutes() {
        return runningTimeInMinutes;
    }

    public void setRunningTimeInMinutes(Integer runningTimeInMinutes) {
        this.runningTimeInMinutes = runningTimeInMinutes;
    }

    public Principal[] getPrincipals() {
        return principals;
    }

    public void setPrincipals(Principal[] principals) {
        this.principals = principals;
    }
}
