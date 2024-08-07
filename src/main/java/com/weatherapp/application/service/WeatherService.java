package com.weatherapp.application.service;

import com.weatherapp.application.api.dto.WeatherResultDTO;

public interface WeatherService {
    WeatherResultDTO getWeatherUpdate(String city, String countryCode);
}
