package com.movieapp.client.service;

import com.movieapp.client.dto.MovieDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;
import java.util.Scanner;

@Service
public class MovieClientService {

    private final WebClient webClient;
    private final Scanner scanner;

    public MovieClientService(WebClient.Builder webClientBuilder,
                              @Value("${movie.search.service.url:http://localhost:8081}") String serviceUrl) {
        this.webClient = webClientBuilder
                .baseUrl(serviceUrl)
                .build();
        this.scanner = new Scanner(System.in);
    }

    public void startMovieSearch() {
        System.out.println("=== Καλώς ήρθατε στην Εφαρμογή Αναζήτησης Ταινιών ===\n");

        boolean continueSearch = true;

        while (continueSearch) {
            System.out.print("Εισάγετε τον τίτλο της ταινίας που αναζητάτε: ");
            String movieTitle = scanner.nextLine().trim();

            if (movieTitle.isEmpty()) {
                System.out.println("Ο τίτλος δεν μπορεί να είναι κενός. Παρακαλώ δοκιμάστε ξανά.\n");
                continue;
            }

            searchAndDisplayMovie(movieTitle);
            continueSearch = askForAnotherSearch();
        }

        System.out.println("Ευχαριστούμε που χρησιμοποιήσατε την εφαρμογή μας!");
        scanner.close();
    }

    private void searchAndDisplayMovie(String title) {
        try {
            System.out.println("\nΑναζήτηση ταινίας: " + title + "...\n");

            MovieDTO movie = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/movies/search")
                            .queryParam("title", title)
                            .build())
                    .retrieve()
                    .bodyToMono(MovieDTO.class)
                    .block();

            if (movie != null) {
                displayMovieDetails(movie);
            } else {
                System.out.println("Δεν ελήφθη απάντηση από την υπηρεσία.\n");
            }

        } catch (WebClientResponseException e) {
            handleWebClientException(e);
        } catch (Exception e) {
            System.out.println("Παρουσιάστηκε σφάλμα κατά την αναζήτηση: " + e.getMessage() + "\n");
        }
    }

    private void handleWebClientException(WebClientResponseException e) {
        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, String> errorBody = e.getResponseBodyAs(Map.class);
                if (errorBody != null && errorBody.containsKey("message")) {
                    System.out.println("❌ " + errorBody.get("message") + "\n");
                } else {
                    System.out.println("❌ Η ταινία δεν βρέθηκε.\n");
                }
            } catch (Exception ex) {
                System.out.println("❌ Η ταινία δεν βρέθηκε.\n");
            }
        } else if (e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            System.out.println("❌ Η υπηρεσία δεν είναι διαθέσιμη προς το παρόν. Παρακαλώ δοκιμάστε αργότερα.\n");
        } else {
            System.out.println("❌ Σφάλμα υπηρεσίας: " + e.getStatusCode() + "\n");
        }
    }

    private void displayMovieDetails(MovieDTO movie) {
        System.out.println("✅ Η ταινία βρέθηκε επιτυχώς!\n");
        System.out.println("🎬 Στοιχεία Ταινίας:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("📽️  Τίτλος: " + movie.getTitle());
        System.out.println("📅 Έτος Κυκλοφορίας: " + movie.getYear());
        System.out.println("⏱️  Διάρκεια: " + movie.getRunningTimeInMinutes() + " λεπτά");
        System.out.println("🎭 Πρωταγωνιστής: " + movie.getLeadActor());
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
    }

    private boolean askForAnotherSearch() {
        while (true) {
            System.out.print("Θέλετε να αναζητήσετε άλλη ταινία; (ν/ο): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("ν") || response.equals("ναι") || response.equals("y") || response.equals("yes")) {
                System.out.println();
                return true;
            } else if (response.equals("ο") || response.equals("όχι") || response.equals("n") || response.equals("no")) {
                System.out.println();
                return false;
            } else {
                System.out.println("Παρακαλώ απαντήστε με 'ν' (ναι) ή 'ο' (όχι).");
            }
        }
    }
}