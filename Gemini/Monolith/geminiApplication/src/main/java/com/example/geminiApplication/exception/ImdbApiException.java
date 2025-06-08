package com.example.geminiApplication.exception;

public class ImdbApiException extends RuntimeException {
    public ImdbApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImdbApiException(String message) {
        super(message);
    }
}