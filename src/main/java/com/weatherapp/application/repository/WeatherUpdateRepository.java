package com.weatherapp.application.repository;

import com.weatherapp.application.repository.model.WeatherUpdateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherUpdateRepository extends JpaRepository<WeatherUpdateEntity, Long> {
    Optional<WeatherUpdateEntity> findByCityAndCountryCodeAndPeriodCode(
            String city,
            String countryCode,
            String periodCode
    );
}
