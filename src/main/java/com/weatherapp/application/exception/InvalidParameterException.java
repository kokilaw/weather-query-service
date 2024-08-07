package com.weatherapp.application.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidParameterException extends RuntimeException {
    private Map<String, String> parameterMap;
    public InvalidParameterException(String message) {
        super(message);
    }
    public InvalidParameterException(String message, Map<String, String> parameterMap) {
        super(message);
        this.parameterMap = parameterMap;
    };
}
