package com.weatherapp.application.client;

import com.weatherapp.application.api.dto.GeoCodeResultDTO;
import com.weatherapp.application.api.dto.WeatherResultDTO;

public interface OpenMapWeatherClient {
    WeatherResultDTO getWeatherUpdate(String city, String countryCode);
    GeoCodeResultDTO getGeoCodes(String city, String countryCode);
}
