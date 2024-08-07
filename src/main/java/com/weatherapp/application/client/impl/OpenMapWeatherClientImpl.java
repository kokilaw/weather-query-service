package com.weatherapp.application.client.impl;

import com.weatherapp.application.Constants;
import com.weatherapp.application.api.dto.GeoCodeResultDTO;
import com.weatherapp.application.api.dto.WeatherResultDTO;
import com.weatherapp.application.client.OpenMapWeatherClient;
import com.weatherapp.application.config.OpenWeatherMapApiConfig;
import com.weatherapp.application.exception.InternalServerError;
import com.weatherapp.application.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Component
@Slf4j
public class OpenMapWeatherClientImpl implements OpenMapWeatherClient {

    private final OpenWeatherMapApiConfig openWeatherMapApiConfig;
    private final RestTemplate restTemplate;

    public OpenMapWeatherClientImpl(
            OpenWeatherMapApiConfig openWeatherMapApiConfig,
            RestTemplate restTemplate
    ) {
        this.openWeatherMapApiConfig = openWeatherMapApiConfig;
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherResultDTO getWeatherUpdate(String city, String countryCode) {
        GeoCodeResultDTO geoCodes = this.getGeoCodes(city, countryCode);
        log.info("Requesting weather data for - [{},{}] - [{}]", city, countryCode, geoCodes);
        String requestPath = String.format(
                Constants.OpenWeatherMap.FETCH_WEATHER_DATA_ENDPOINT,
                geoCodes.latitude(),
                geoCodes.longitude(),
                openWeatherMapApiConfig.getApiKey()
        );
        String requestUrl = openWeatherMapApiConfig.getBaseUrl().concat(requestPath);
        ResponseEntity<WeatherResultResponse> response =
                restTemplate.getForEntity(requestUrl, WeatherResultResponse.class);
        List<WeatherResponse> results = Objects.requireNonNull(response.getBody()).weather();
        return results.stream().findFirst().map(weatherResponse -> new WeatherResultDTO(weatherResponse.description()))
                .orElseThrow(() -> new InternalServerError("Results not available for given location"));

    }

    @Override
    public GeoCodeResultDTO getGeoCodes(String city, String countryCode) {
        log.info("Requesting geo code data for - [{},{}]", city, countryCode);

        String requestPath = String.format(
                Constants.OpenWeatherMap.FETCH_GEO_CODE_ENDPOINT,
                city,
                countryCode,
                openWeatherMapApiConfig.getApiKey()
        );
        String requestUrl = openWeatherMapApiConfig.getBaseUrl().concat(requestPath);

        ResponseEntity<GeoCodeResultItem[]> response =
                restTemplate.getForEntity(requestUrl, GeoCodeResultItem[].class);
        return Stream.of(Objects.requireNonNull(response.getBody()))
                .findAny()
                .map(geoCodeResultItem -> new GeoCodeResultDTO(geoCodeResultItem.lat(), geoCodeResultItem.lon()))
                .orElseThrow(() -> new NotFoundException(
                        Constants.ExceptionMessages.LOCATION_NOT_AVAILABLE,
                        Map.of("city", city, "countryCode", countryCode)
                ));
    }

    record GeoCodeResultItem(String name, String lat, String lon) {}

    record WeatherResultResponse(List<WeatherResponse> weather) {
    }

    record WeatherResponse(String id, String main, String description, String icon) {
    }

}
