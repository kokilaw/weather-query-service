package com.weatherapp.application.repository;

import com.weatherapp.application.repository.model.WeatherUpdateEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WeatherResponseUpdateRepositoryTest {

    @Autowired
    private WeatherUpdateRepository weatherUpdateRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void givenNewEntity_whenSaved_isSuccess() {

        WeatherUpdateEntity weatherUpdateEntity = WeatherUpdateEntity.builder()
                .city("melbourne")
                .countryCode("au")
                .description("Seems like sunny")
                .periodCode("2024051614")
                .build();
        weatherUpdateRepository.save(weatherUpdateEntity);
        Optional<WeatherUpdateEntity> result = weatherUpdateRepository.findByCityAndCountryCodeAndPeriodCode(
                weatherUpdateEntity.getCity(),
                weatherUpdateEntity.getCountryCode(),
                weatherUpdateEntity.getPeriodCode()
        );
        assertTrue(result.isPresent());
    }

}