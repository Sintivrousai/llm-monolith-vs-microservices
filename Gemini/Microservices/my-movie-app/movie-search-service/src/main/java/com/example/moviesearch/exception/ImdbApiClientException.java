package com.example.moviesearch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Επιστρέφει 500 Internal Server Error
public class ImdbApiClientException extends RuntimeException {
    public ImdbApiClientException(String message) {
        super(message);
    }

    public ImdbApiClientException(String message, Throwable cause) {
        super(message, cause);
    }
}