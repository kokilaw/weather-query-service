package com.weatherapp.application.exception;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Getter
public class NotFoundException extends RuntimeException {
    private Map<String, String> resourceIdMap;
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(String message, Map<String, String> resourceIdMap) {
        super(message);
        this.resourceIdMap = resourceIdMap;
    };
}
