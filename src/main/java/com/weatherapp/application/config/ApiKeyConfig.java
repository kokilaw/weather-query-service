package com.weatherapp.application.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "api-key")
public class ApiKeyConfig {
    private int timePeriodInMinutes;
    private int requestRateAllowedForPeriod;
}
