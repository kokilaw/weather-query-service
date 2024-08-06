package com.weatherapp.application.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class NotFoundException extends RuntimeException {
    private Map<String, String> parameterMap;
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(String message, Map<String, String> parameterMap) {
        super(message);
        this.parameterMap = parameterMap;
    };
}
