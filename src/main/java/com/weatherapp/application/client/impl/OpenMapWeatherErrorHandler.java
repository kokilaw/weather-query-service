package com.weatherapp.application.client.impl;

import com.weatherapp.application.Constants;
import com.weatherapp.application.exception.InternalServerError;
import com.weatherapp.application.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
public class OpenMapWeatherErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is5xxServerError() ||
                response.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String responseBody = new BufferedReader(new InputStreamReader(response.getBody()))
                .lines().collect(Collectors.joining("\n"));
        log.error("Error occurred - [{}][{}]", response.getStatusCode(), responseBody);
        if (response.getStatusCode().is5xxServerError()) {
            throw new InternalServerError(Constants.ExceptionMessages.ERROR_WITH_OPEN_WEATHER_MAP);
        } else if (response.getStatusCode().is4xxClientError()) {
            if (Constants.HttpStatusCode.NOT_FOUND == response.getStatusCode().value()) {
                throw new NotFoundException(Constants.ExceptionMessages.WEATHER_DATA_NOT_AVAILABLE);
            } else {
                throw new InternalServerError(Constants.ExceptionMessages.ERROR_COMMUNICATING_WITH_OPEN_WEATHER_MAP);
            }
        }
    }

}
