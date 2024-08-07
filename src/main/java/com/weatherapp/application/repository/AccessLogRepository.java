package com.weatherapp.application.repository;

import com.weatherapp.application.repository.model.AccessLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLogEntity, Long> {
    @Query("SELECT COUNT(al) from AccessLogEntity al WHERE al.apiKey = :apiKey AND al.accessedAt >= :accessedAt")
    Integer getCountByApiKeyAndAccessedAt(String apiKey, LocalDateTime accessedAt);
}
