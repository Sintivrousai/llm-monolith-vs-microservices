package com.example.geminiApplication;

import com.example.geminiApplication.model.MovieInfo;
import com.example.geminiApplication.service.ImdbApiService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication
public class GeminiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeminiApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ImdbApiService imdbApiService) {
		return args -> {
			Scanner scanner = new Scanner(System.in);
			boolean continueSearch = true;

			while (continueSearch) {
				System.out.println("\nΠαρακαλώ πληκτρολογήστε τον τίτλο της ταινίας που ψάχνετε (ή 'έξοδος' για τερματισμό):");
				String title = scanner.nextLine();

				if ("έξοδος".equalsIgnoreCase(title)) {
					continueSearch = false;
					System.out.println("Ευχαριστούμε που χρησιμοποιήσατε την εφαρμογή!");
					continue;
				}

				if (title.trim().isEmpty()) {
					System.out.println("Ο τίτλος δεν μπορεί να είναι κενός. Παρακαλώ δοκιμάστε ξανά.");
					continue;
				}

				imdbApiService.searchMovie(title)
						.doOnSuccess(movieInfoOptional -> {
							if (movieInfoOptional.isPresent()) {
								MovieInfo movieInfo = movieInfoOptional.get();
								System.out.println("\nΒρέθηκε ταινία:");
								System.out.println("Τίτλος: " + movieInfo.getTitle());
								System.out.println("Έτος: " + movieInfo.getYear());
								// System.out.println("Διάρκεια: " + (movieInfo.getRunningTimeInMinutes() != null ? movieInfo.getRunningTimeInMinutes() + " λεπτά" : "Δεν είναι διαθέσιμη"));
								System.out.println("Πρωταγωνιστής: " + movieInfo.getLeadActor());
							} else {
								System.out.println("Δεν βρέθηκε ταινία με τον τίτλο '" + title + "'.");
							}
							promptForAnotherSearch(scanner, response -> {
								// This lambda is intentionally left empty.
								// The loop condition is managed by `continueSearch`.
								// The purpose is just to consume the input.
							});
						})
						.doOnError(e -> {
							System.err.println("Προέκυψε σφάλμα κατά την αναζήτηση: " + e.getMessage());
							promptForAnotherSearch(scanner, response -> {});
						})
						.block(); // Block to wait for the reactive stream to complete

			}
			scanner.close();
		};
	}

	private void promptForAnotherSearch(Scanner scanner, java.util.function.Consumer<String> consumer) {
		System.out.println("\nΘέλετε να αναζητήσετε άλλη ταινία; (ναι/οχι)");
		String response = scanner.nextLine();
		if (!"ναι".equalsIgnoreCase(response)) {
			System.exit(0); // Exit the application
		}
		consumer.accept(response);
	}
}