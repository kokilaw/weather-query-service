package com.weatherapp.application.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "open-weather-map-api")
public class OpenWeatherMapApiConfig {
    private String baseUrl;
    private String apiKey;
}
