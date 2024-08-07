package com.weatherapp.application.exception;

public class ApiKeyRateExceededException extends RuntimeException {
    public ApiKeyRateExceededException(String message) {
        super(message);
    }
}
