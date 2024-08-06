package com.weatherapp.application.service.impl;

import com.weatherapp.application.config.ApiKeyConfig;
import com.weatherapp.application.exception.ApiKeyNotFoundException;
import com.weatherapp.application.exception.ApiKeyRateExceededException;
import com.weatherapp.application.exception.ExpiredApiKeyException;
import com.weatherapp.application.repository.AccessLogRepository;
import com.weatherapp.application.repository.ApiKeyRepository;
import com.weatherapp.application.repository.model.AccessLogEntity;
import com.weatherapp.application.repository.model.ApiKeyEntity;
import com.weatherapp.application.service.AccessMonitorService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccessMonitorServiceImpl implements AccessMonitorService {

    private final ApiKeyRepository apiKeyRepository;
    private final AccessLogRepository accessLogRepository;
    private final ApiKeyConfig apiKeyConfig;

    public AccessMonitorServiceImpl(
            ApiKeyRepository apiKeyRepository,
            AccessLogRepository accessLogRepository,
            ApiKeyConfig apiKeyConfig) {
        this.apiKeyRepository = apiKeyRepository;
        this.accessLogRepository = accessLogRepository;
        this.apiKeyConfig = apiKeyConfig;
    }

    @Override
    public void validateKey(String apiKey) {
        ApiKeyEntity apiKeyEntity = getApiKeyEntity(apiKey);
        if (ApiKeyEntity.KeyStatus.EXPIRED == apiKeyEntity.getStatus()) {
            throw new ExpiredApiKeyException("Provided API Key Expired");
        }
        int logCountWithinConsideredPeriod = accessLogRepository.getCountByApiKeyAndAccessedAt(
                apiKey,
                LocalDateTime.now().minusMinutes(apiKeyConfig.getTimePeriodInMinutes())
        );
        if (logCountWithinConsideredPeriod >= apiKeyConfig.getRequestRateAllowedForPeriod()) {
            throw new ApiKeyRateExceededException("API Key usage limit reached for the period");
        }
    }

    private ApiKeyEntity getApiKeyEntity(String apiKey) {
        return apiKeyRepository.findById(apiKey)
                .orElseThrow(() -> new ApiKeyNotFoundException("Invalid API Key Provided"));
    }

    @Override
    public void logKeyAccess(String apiKey) {
        accessLogRepository.save(AccessLogEntity.builder()
                .apiKey(apiKey)
                .accessedAt(LocalDateTime.now())
                .build());
    }

}
