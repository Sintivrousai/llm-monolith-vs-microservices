package com.example.movie_search_api;

import com.example.movie_search_api.dto.MovieResponse;
import com.example.movie_search_api.service.MovieService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Scanner;

@SpringBootApplication
public class MovieSearchApiApplication implements CommandLineRunner {

	private final ApplicationContext context;

	public MovieSearchApiApplication(ApplicationContext context) {
		this.context = context;
	}

	public static void main(String[] args) {
		SpringApplication.run(MovieSearchApiApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Scanner scanner = new Scanner(System.in);
		MovieService service = context.getBean(MovieService.class);

		while (true) {
			System.out.print("Δώσε τίτλο ταινίας: ");
			String title = scanner.nextLine();

			try {
				MovieResponse movie = service.findMovieByTitle(title);
				if (movie == null) {
					System.out.println("Δεν βρέθηκε ταινία με αυτόν τον τίτλο.");
				} else {
					System.out.println("\nΑποτελέσματα:");
					System.out.println("Τίτλος: " + movie.getTitle());
					System.out.println("Έτος: " + movie.getYear());
					System.out.println("Διάρκεια: " + movie.getRunningTimeInMinutes() + " λεπτά");
					System.out.println("Πρωταγωνιστής: " + movie.getLeadActor());
				}
			} catch (Exception e) {
				System.out.println("Σφάλμα: " + e.getMessage());
			}

			System.out.print("\nΘέλεις να κάνεις νέα αναζήτηση; (ναι/όχι): ");
			String again = scanner.nextLine();
			if (!again.equalsIgnoreCase("ναι")) {
				break;
			}
		}

		// Τέλος εφαρμογής, χωρίς εξαναγκασμένο System.exit
		System.out.println("Έξοδος από την εφαρμογή.");
	}
}
