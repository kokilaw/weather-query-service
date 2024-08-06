package com.weatherapp.application.service;

public interface AccessMonitorService {
    void validateKey(String apiKey);
    void logKeyAccess(String apiKey);
}
