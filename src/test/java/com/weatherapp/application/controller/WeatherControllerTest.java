package com.weatherapp.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherapp.application.Constants;
import com.weatherapp.application.api.dto.WeatherResultDTO;
import com.weatherapp.application.api.response.WeatherResultResponse;
import com.weatherapp.application.client.OpenMapWeatherClient;
import com.weatherapp.application.config.ApiKeyConfig;
import com.weatherapp.application.exception.NotFoundException;
import com.weatherapp.application.repository.AccessLogRepository;
import com.weatherapp.application.repository.ApiKeyRepository;
import com.weatherapp.application.repository.model.AccessLogEntity;
import com.weatherapp.application.repository.model.ApiKeyEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.IntStream;

import static com.weatherapp.application.Constants.ExceptionMessages.INVALID_COUNTRY_CODE;
import static com.weatherapp.application.Constants.ExceptionMessages.LOCATION_NOT_AVAILABLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = {"/scripts/delete-init-data.sql"}, executionPhase = AFTER_TEST_CLASS)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    OpenMapWeatherClient openMapWeatherClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Autowired
    AccessLogRepository accessLogRepository;

    @Autowired
    ApiKeyConfig apiKeyConfig;

    @Test
    public void whenValidLocationIsProvided_weatherResultIsReturn() throws Exception {
        String inputCity = "Tokyo";
        String inputCountryCode = "JP";
        String inputAPIKey = "5cf9dd49-c5f2-4634-893a-23a31abf2bdc";
        WeatherResultDTO mockedClientResponse = new WeatherResultDTO("slightly sunny");
        Mockito.when(openMapWeatherClient.getWeatherUpdate(any(), any()))
                .thenReturn(mockedClientResponse);
        mockMvc.perform(get("/v1/weather")
                        .queryParam("city", inputCity)
                        .queryParam("countryCode", inputCountryCode)
                        .header(Constants.Headers.API_KEY, inputAPIKey)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new WeatherResultResponse(mockedClientResponse.description()))));
    }

    @Test
    public void whenUnknownApiKeyIsProvided_errorIsReturned() throws Exception {
        String inputCity = "Tokyo";
        String inputCountryCode = "JP";
        String inputAPIKey = "random_unknown_api_key";
        mockMvc.perform(get("/v1/weather")
                        .queryParam("city", inputCity)
                        .queryParam("countryCode", inputCountryCode)
                        .header(Constants.Headers.API_KEY, inputAPIKey)
                        .contentType("application/json"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenAPIKeyExpired_returnedError() throws Exception {
        String inputCity = "Tokyo";
        String inputCountryCode = "JP";
        String inputAPIKey = "5cf9dd49-c5f2-4634-893a-23a31abf2bdc";

        apiKeyRepository.findById(inputAPIKey).ifPresent(apiKeyEntity -> {
            apiKeyEntity.setStatus(ApiKeyEntity.KeyStatus.EXPIRED);
            apiKeyRepository.save(apiKeyEntity);
        });

        WeatherResultDTO mockedClientResponse = new WeatherResultDTO("slightly sunny");
        Mockito.when(openMapWeatherClient.getWeatherUpdate(any(), any()))
                .thenReturn(mockedClientResponse);
        mockMvc.perform(get("/v1/weather")
                        .queryParam("city", inputCity)
                        .queryParam("countryCode", inputCountryCode)
                        .header(Constants.Headers.API_KEY, inputAPIKey)
                        .contentType("application/json"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenAPIKeyRateExceeded_errorIsReturn() throws Exception {
        String inputCity = "Tokyo";
        String inputCountryCode = "JP";
        String inputAPIKey = "5cf9dd49-c5f2-4634-893a-23a31abf2bdc";

        apiKeyRepository.findById(inputAPIKey).ifPresent(apiKeyEntity -> {
            IntStream.rangeClosed(1, apiKeyConfig.getRequestRateAllowedForPeriod()).forEach(value -> {
                accessLogRepository.save(AccessLogEntity.builder()
                        .apiKey(apiKeyEntity.getApiKey())
                        .accessedAt(LocalDateTime.now())
                        .build());
            });
        });

        WeatherResultDTO mockedClientResponse = new WeatherResultDTO("slightly sunny");
        Mockito.when(openMapWeatherClient.getWeatherUpdate(any(), any()))
                .thenReturn(mockedClientResponse);
        mockMvc.perform(get("/v1/weather")
                        .queryParam("city", inputCity)
                        .queryParam("countryCode", inputCountryCode)
                        .header(Constants.Headers.API_KEY, inputAPIKey)
                        .contentType("application/json"))
                .andExpect(status().is(429));
    }

    @Test
    public void whenInvalidInputProvided_errorIsReturn() throws Exception {
        String inputCity = "Tokyo";
        String inputCountryCode = "JPY";
        String inputAPIKey = "5cf9dd49-c5f2-4634-893a-23a31abf2bdc";

        WeatherResultDTO mockedClientResponse = new WeatherResultDTO("slightly sunny");
        Mockito.when(openMapWeatherClient.getWeatherUpdate(any(), any()))
                .thenReturn(mockedClientResponse);
        mockMvc.perform(get("/v1/weather")
                        .queryParam("city", inputCity)
                        .queryParam("countryCode", inputCountryCode)
                        .header(Constants.Headers.API_KEY, inputAPIKey)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.countryCode").value(inputCountryCode))
                .andExpect(jsonPath("$.detail").value(INVALID_COUNTRY_CODE));
    }

    @Test
    public void whenInputLocationNotFound_errorIsReturn() throws Exception {
        String inputCity = "assdlkjlas";
        String inputCountryCode = "JP";
        String inputAPIKey = "5cf9dd49-c5f2-4634-893a-23a31abf2bdc";

        WeatherResultDTO mockedClientResponse = new WeatherResultDTO("slightly sunny");
        Mockito.when(openMapWeatherClient.getWeatherUpdate(any(), any()))
                .thenThrow(new NotFoundException(
                        LOCATION_NOT_AVAILABLE,
                        Map.of("city", inputCity, "countryCode", inputCountryCode)
                ));
        mockMvc.perform(get("/v1/weather")
                        .queryParam("city", inputCity)
                        .queryParam("countryCode", inputCountryCode)
                        .header(Constants.Headers.API_KEY, inputAPIKey)
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value(LOCATION_NOT_AVAILABLE))
                .andExpect(jsonPath("$.countryCode").value(inputCountryCode))
                .andExpect(jsonPath("$.city").value(inputCity));
    }

}