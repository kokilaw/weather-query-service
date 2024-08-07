package com.weatherapp.application.config;

import com.weatherapp.application.client.impl.OpenMapWeatherErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .errorHandler(new OpenMapWeatherErrorHandler())
                .build();
    }

}
