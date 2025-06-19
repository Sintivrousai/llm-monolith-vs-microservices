package com.example.movieapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

@SpringBootApplication
public class MovieApiServiceApplication implements CommandLineRunner {

    @Value("${search.service.url}")
    private String searchServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringApplication.run(MovieApiServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Τίτλος ταινίας: ");
            String input = scanner.nextLine();

            try {
                ResponseEntity<String> response = restTemplate.getForEntity(searchServiceUrl + "?title=" + input, String.class);

                if (response.getStatusCode() == HttpStatus.OK) {
                    System.out.println("Αποτέλεσμα:\n" + response.getBody());
                } else {
                    System.out.println("⚠ " + response.getBody());
                }
            } catch (Exception e) {
                System.out.println("❌ Σφάλμα επικοινωνίας: " + e.getMessage());
            }

            System.out.print("Νέα αναζήτηση; (ν/ο): ");
            if (!scanner.nextLine().equalsIgnoreCase("ν")) break;
        }

        System.out.println("Έξοδος.");
    }
}