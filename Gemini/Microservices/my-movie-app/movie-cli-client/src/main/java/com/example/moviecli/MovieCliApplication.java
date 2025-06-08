package com.example.moviecli;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Scanner;

public class MovieCliApplication {

    private static final String API_BASE_URL = "http://localhost:8080/api/v1/movies";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper(); // Για να παρσάρουμε απαντήσεις σφάλματος

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueSearch = true;

        System.out.println("Καλώς ήρθατε στην εφαρμογή αναζήτησης ταινιών!");

        while (continueSearch) {
            System.out.print("\nΠληκτρολογήστε τον τίτλο της ταινίας που ψάχνετε (ή 'exit' για έξοδο): ");
            String movieTitle = scanner.nextLine();

            if ("exit".equalsIgnoreCase(movieTitle.trim())) {
                continueSearch = false;
                System.out.println("Ευχαριστούμε που χρησιμοποιήσατε την εφαρμογή.");
                continue;
            }

            if (movieTitle.trim().isEmpty()) {
                System.out.println("Ο τίτλος δεν μπορεί να είναι κενός. Παρακαλώ δοκιμάστε ξανά.");
                continue;
            }

            try {
                String url = API_BASE_URL + "/search?title=" + movieTitle;
                ResponseEntity<Movie> response = restTemplate.getForEntity(url, Movie.class);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    Movie movie = response.getBody();
                    System.out.println("\n--- Βρέθηκε Ταινία ---");
                    System.out.println("Τίτλος: " + movie.getTitle());
                    System.out.println("Έτος κυκλοφορίας: " + movie.getYear());
                    System.out.println("Διάρκεια προβολής: " + movie.getRunningTimeInMinutes() + " λεπτά");
                    System.out.println("Πρωταγωνιστής: " + movie.getLeadActor());
                    System.out.println("---------------------\n");
                } else {
                    // Αυτό το block πιθανότατα δεν θα εκτελεστεί αν το HttpClientErrorException χειρίζεται 4xx/5xx
                    System.err.println("Αδύνατη η ανάκτηση πληροφοριών για την ταινία '" + movieTitle + "'.");
                }
            } catch (HttpClientErrorException.NotFound e) {
                // Χειρισμός 404 Not Found (ταινία δεν βρέθηκε)
                System.out.println("Δεν βρέθηκε ταινία με τίτλο '" + movieTitle + "'. Παρακαλώ δοκιμάστε ξανά.");
            } catch (HttpClientErrorException e) {
                // Χειρισμός άλλων σφαλμάτων 4xx ή 5xx από το API
                try {
                    // Προσπάθεια να παρσάρουμε την απάντηση σφάλματος από το API
                    ErrorResponse errorResponse = objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponse.class);
                    System.err.println("Σφάλμα από την υπηρεσία: " + errorResponse.getMessage());
                } catch (Exception jsonEx) {
                    System.err.println("Σφάλμα κατά την επικοινωνία με την υπηρεσία: " + e.getLocalizedMessage());
                    System.err.println("Ακατέργαστη απάντηση σφάλματος: " + e.getResponseBodyAsString());
                }
            } catch (Exception e) {
                System.err.println("Προέκυψε ένα απροσδόκητο σφάλμα: " + e.getLocalizedMessage());
            }

            System.out.print("Θέλετε να αναζητήσετε εκ νέου ταινία; (ναι/όχι): ");
            String choice = scanner.nextLine();
            if (!"ναι".equalsIgnoreCase(choice.trim())) {
                continueSearch = false;
                System.out.println("Ευχαριστούμε που χρησιμοποιήσατε την εφαρμογή.");
            }
        }
        scanner.close();
    }

    // DTO for Movie (ίδιο με τα Spring Boot services)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Movie {
        private String title;
        private Integer year;
        private Integer runningTimeInMinutes;
        private String leadActor;
    }

    // DTO for Error Response (ίδιο με το movie-search-service)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ErrorResponse {
        private String message;
        private LocalDateTime timestamp;
        private int status;
        private String error;
    }
}