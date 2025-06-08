package com.example.geminiApplication.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;
import java.util.Optional; // Used for safer handling if a field might be missing

// Top-level API response
@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Important: Ignore fields we don't map
public class ImdbApiResponse {
    private List<MovieResult> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MovieResult {
        // Essential fields for your MovieInfo output:
        private String title;
        private Integer year;
        private Integer runningTimeInMinutes;

        // *** THIS FIELD IS ABSOLUTELY REQUIRED FOR ImdbApiService FILTERING ***
        private String titleType; // <--- MAKE SURE THIS LINE IS PRESENT

        // Fields needed to extract lead actor:
        private List<Principal> principals;

        // Nested class for principal actors/crew
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Principal {
            private String name; // This is what you need for leadActor
            private String category; // To filter for "actor"

            // As fixed previously, this must be a List<String>
            private List<String> characters;

            // ... other fields in Principal if you have them in your model, or they will be ignored
        }
        // ... potentially other nested classes like Image if you're mapping them.
    }
}