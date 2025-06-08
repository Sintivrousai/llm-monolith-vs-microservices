package com.example.moviesearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private LocalDateTime timestamp;
    private int status;
    private String error;

    public ErrorResponse(String message, int status, String error) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
    }
}