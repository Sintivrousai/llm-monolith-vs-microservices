package com.example.imdbapiclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Αγνοεί πεδία που δεν έχουν δηλωθεί στο DTO
public class ImdbMovieResponse {
    private List<JsonNode> results; // Χρησιμοποιούμε JsonNode για ευελιξία στην επεξεργασία
}