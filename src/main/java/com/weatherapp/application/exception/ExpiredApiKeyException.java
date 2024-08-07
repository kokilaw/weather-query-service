package com.weatherapp.application.exception;

public class ExpiredApiKeyException extends RuntimeException {
    public ExpiredApiKeyException(String message) {
        super(message);
    }
}
