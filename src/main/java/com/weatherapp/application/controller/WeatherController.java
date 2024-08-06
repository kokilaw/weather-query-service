package com.weatherapp.application.controller;

import com.weatherapp.application.api.dto.WeatherResultDTO;
import com.weatherapp.application.api.response.WeatherResultResponse;
import com.weatherapp.application.aspect.annotation.RateControlled;
import com.weatherapp.application.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @RateControlled
    @GetMapping
    public WeatherResultResponse getWeatherUpdate(@RequestParam String city, @RequestParam String countryCode) {
        WeatherResultDTO weatherUpdate = weatherService.getWeatherUpdate(city, countryCode);
        return new WeatherResultResponse(weatherUpdate.description());
    }

}
