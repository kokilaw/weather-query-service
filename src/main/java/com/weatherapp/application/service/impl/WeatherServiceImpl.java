package com.weatherapp.application.service.impl;

import com.weatherapp.application.api.dto.WeatherResultDTO;
import com.weatherapp.application.client.OpenMapWeatherClient;
import com.weatherapp.application.repository.WeatherUpdateRepository;
import com.weatherapp.application.repository.model.WeatherUpdateEntity;
import com.weatherapp.application.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.weatherapp.application.Constants.PERIOD_CODE_FORMAT;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final OpenMapWeatherClient openMapWeatherClient;
    private final WeatherUpdateRepository weatherUpdateRepository;

    public WeatherServiceImpl(
            OpenMapWeatherClient openMapWeatherClient,
            WeatherUpdateRepository weatherUpdateRepository
    ) {
        this.openMapWeatherClient = openMapWeatherClient;
        this.weatherUpdateRepository = weatherUpdateRepository;
    }

    @Override
    public WeatherResultDTO getWeatherUpdate(String city, String countryCode) {
        String periodCode = getCurrentPeriodCode();
        String cityInLowerCase = city.toLowerCase();
        String countryCodeInLowerCase = countryCode.toLowerCase();
        Optional<WeatherUpdateEntity> cachedResult = weatherUpdateRepository
                .findByCityAndCountryCodeAndPeriodCode(
                        cityInLowerCase,
                        countryCodeInLowerCase,
                        periodCode
                );
        if (cachedResult.isEmpty()) {
            log.info("Weather result not available locally. city[{}] countryCode[{}] periodCode[{}]",
                    city, countryCode, periodCode);
            WeatherResultDTO weatherResultDTO = openMapWeatherClient.getWeatherUpdate(city, countryCode);
            weatherUpdateRepository.save(
                    WeatherUpdateEntity.builder()
                            .description(weatherResultDTO.description())
                            .periodCode(periodCode)
                            .countryCode(countryCodeInLowerCase)
                            .city(cityInLowerCase)
                            .build()
            );
            return weatherResultDTO;
        } else {
            log.info("Weather result available locally. city[{}] countryCode[{}] periodCode[{}]",
                    city, countryCode, periodCode);
            return new WeatherResultDTO(cachedResult.get().getDescription());
        }
    }

    private String getCurrentPeriodCode() {
        return LocalDateTime.now().format(PERIOD_CODE_FORMAT);
    }

}
