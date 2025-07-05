package com.example.movie_search_api.cli;

import com.example.movie_search_api.model.MovieResponse;
import com.example.movie_search_api.service.ImdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MovieSearchCLI implements CommandLineRunner {

    @Autowired
    private ImdbService imdbService;

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("    ΚΑΛΩΣ ΗΡΘΑΤΕ ΣΤΟ MOVIE SEARCH APPLICATION");
        System.out.println("=".repeat(60));

        boolean continueSearch = true;

        while (continueSearch) {
            try {
                System.out.print("\nΠληκτρολογήστε τον τίτλο της ταινίας: ");
                String title = scanner.nextLine().trim();

                if (title.isEmpty()) {
                    System.out.println("❌ Παρακαλώ εισάγετε έναν έγκυρο τίτλο ταινίας.");
                    continue;
                }

                System.out.println("\n🔍 Αναζήτηση για: " + title + "...");

                MovieResponse movie = imdbService.searchMovie(title);

                System.out.println("✅ Επιτυχής αναζήτηση!");
                System.out.println(movie.toString());

            } catch (Exception e) {
                System.out.println("\n❌ ΣΦΑΛΜΑ: " + e.getMessage());
            }

            continueSearch = askForNewSearch(scanner);
        }

        System.out.println("\n👋 Σας ευχαριστούμε που χρησιμοποιήσατε το Movie Search Application!");
        System.out.println("Αντίο! 🎬\n");
    }

    private boolean askForNewSearch(Scanner scanner) {
        while (true) {
            System.out.print("\nΘέλετε να αναζητήσετε άλλη ταινία; (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("y") || response.equals("yes") || response.equals("ναι") || response.equals("ν")) {
                return true;
            } else if (response.equals("n") || response.equals("no") || response.equals("όχι") || response.equals("ο")) {
                return false;
            } else {
                System.out.println("❌ Παρακαλώ απαντήστε με 'y' για ναι ή 'n' για όχι.");
            }
        }
    }
}