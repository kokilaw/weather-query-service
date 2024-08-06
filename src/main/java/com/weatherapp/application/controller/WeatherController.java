package com.weatherapp.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;


@RestController
@RequestMapping("v1/weather")
public class WeatherController {

    @GetMapping
    public Map<String, String> getWeatherUpdate(@RequestParam String city, @RequestParam String countryCode) {
        return Collections.emptyMap();
    };

}
