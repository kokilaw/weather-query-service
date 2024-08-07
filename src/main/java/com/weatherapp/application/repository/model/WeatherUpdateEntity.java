package com.weatherapp.application.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "weather_update")
public class WeatherUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="city")
    private String city;

    @Column(name="country_code")
    private String countryCode;

    @Column(name="period_code")
    private String periodCode;

    @Column(name="description")
    private String description;

    @Column(name="created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

}
