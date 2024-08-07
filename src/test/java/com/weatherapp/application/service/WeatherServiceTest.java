package com.weatherapp.application.service;

import com.weatherapp.application.api.dto.WeatherResultDTO;
import com.weatherapp.application.client.OpenMapWeatherClient;
import com.weatherapp.application.repository.WeatherUpdateRepository;
import com.weatherapp.application.repository.model.WeatherUpdateEntity;
import com.weatherapp.application.service.impl.AccessMonitorServiceImpl;
import com.weatherapp.application.service.impl.WeatherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    OpenMapWeatherClient openMapWeatherClient;

    @Mock
    WeatherUpdateRepository weatherUpdateRepository;

    private WeatherService weatherService;

    @BeforeEach
    public void setup() {
        this.weatherService = new WeatherServiceImpl(openMapWeatherClient, weatherUpdateRepository);
    }

    @Test
    public void givenValidCityAndCountryCode_whenDataExist_isReturned() {
        String city = "london";
        String countryCode = "uk";
        WeatherUpdateEntity existingEntity = WeatherUpdateEntity.builder()
                .city(city)
                .countryCode(countryCode)
                .description("partially sunny")
                .build();
        Mockito.when(weatherUpdateRepository.findByCityAndCountryCodeAndPeriodCode(eq(city), eq(countryCode), any()))
                .thenReturn(Optional.of(existingEntity));
        WeatherResultDTO result = assertDoesNotThrow(() -> weatherService.getWeatherUpdate(city, countryCode));
        assertEquals(existingEntity.getDescription(), result.description());
    }

    @Test
    public void givenValidCityAndCountryCode_whenDataNotExist_isFetchedAndReturned() {

        String city = "london";
        String countryCode = "uk";
        WeatherResultDTO responseDTO = new WeatherResultDTO("slight drizzles");
        Mockito.when(weatherUpdateRepository.findByCityAndCountryCodeAndPeriodCode(eq(city), eq(countryCode), any()))
                .thenReturn(Optional.empty());
        Mockito.when(openMapWeatherClient.getWeatherUpdate(eq(city), eq(countryCode))).thenReturn(responseDTO);
        WeatherResultDTO result = assertDoesNotThrow(() -> weatherService.getWeatherUpdate(city, countryCode));
        Mockito.verify(openMapWeatherClient, Mockito.times(1))
                .getWeatherUpdate(city, countryCode);

        ArgumentCaptor<WeatherUpdateEntity> argumentCaptor = ArgumentCaptor.forClass(WeatherUpdateEntity.class);
        Mockito.verify(weatherUpdateRepository, Mockito.times(1))
                .save(argumentCaptor.capture());
        WeatherUpdateEntity savingEntity = argumentCaptor.getValue();
        assertEquals(savingEntity.getDescription(), responseDTO.description());
        assertEquals(savingEntity.getCountryCode(), countryCode);
        assertEquals(savingEntity.getCity(), city);
        assertEquals(result.description(), responseDTO.description());

    }

}