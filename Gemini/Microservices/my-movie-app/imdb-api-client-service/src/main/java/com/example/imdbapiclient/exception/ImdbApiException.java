package com.example.imdbapiclient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Επιστρέφει 500 Internal Server Error
public class ImdbApiException extends RuntimeException {
    public ImdbApiException(String message) {
        super(message);
    }

    public ImdbApiException(String message, Throwable cause) {
        super(message, cause);
    }
}