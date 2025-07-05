package com.movieapp.search.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImdbResponse {
    @JsonProperty("results")
    private List<ImdbResult> results;

    public List<ImdbResult> getResults() {
        return results;
    }

    public void setResults(List<ImdbResult> results) {
        this.results = results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImdbResult {
        @JsonProperty("id")
        private String id;

        @JsonProperty("title")
        private String title;

        @JsonProperty("year")
        private Integer year;

        @JsonProperty("runningTimeInMinutes")
        private Integer runningTimeInMinutes;

        @JsonProperty("principals")
        private List<Principal> principals;

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

        public List<Principal> getPrincipals() {
            return principals;
        }

        public void setPrincipals(List<Principal> principals) {
            this.principals = principals;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Principal {
            @JsonProperty("name")
            private String name;

            @JsonProperty("category")
            private String category;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }
        }
    }
}