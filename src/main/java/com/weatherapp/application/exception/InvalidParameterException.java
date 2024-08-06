package com.weatherapp.application.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidParameterException extends RuntimeException {
    private Map<String, String> resourceIdMap;
    public InvalidParameterException(String message) {
        super(message);
    }
    public InvalidParameterException(String message, Map<String, String> resourceIdMap) {
        super(message);
        this.resourceIdMap = resourceIdMap;
    };
}
