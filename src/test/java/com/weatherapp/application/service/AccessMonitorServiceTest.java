package com.weatherapp.application.service;

import com.weatherapp.application.config.ApiKeyConfig;
import com.weatherapp.application.exception.ApiKeyNotFoundException;
import com.weatherapp.application.exception.ApiKeyRateExceededException;
import com.weatherapp.application.exception.ExpiredApiKeyException;
import com.weatherapp.application.repository.AccessLogRepository;
import com.weatherapp.application.repository.ApiKeyRepository;
import com.weatherapp.application.repository.model.AccessLogEntity;
import com.weatherapp.application.repository.model.ApiKeyEntity;
import com.weatherapp.application.service.impl.AccessMonitorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class AccessMonitorServiceTest {

    @Mock
    AccessLogRepository accessLogRepository;

    @Mock
    ApiKeyRepository apiKeyRepository;

    @Mock
    ApiKeyConfig apiKeyConfig;

    private AccessMonitorService accessMonitorService;

    @BeforeEach
    public void setup() {
        this.accessMonitorService = new AccessMonitorServiceImpl(
                apiKeyRepository, accessLogRepository, apiKeyConfig);
    }

    @Test
    public void givenUnknownApiKey_whenChecked_throwsError() {
        Mockito.when(apiKeyRepository.findById(any())).thenReturn(Optional.empty());
        String randomApiKey = UUID.randomUUID().toString();
        assertThrows(ApiKeyNotFoundException.class, () -> accessMonitorService.validateKey(randomApiKey));
    }

    @Test
    public void givenExpiredApiKey_whenChecked_throwsError() {
        String randomApiKey = UUID.randomUUID().toString();
        Mockito.when(apiKeyRepository.findById(eq(randomApiKey)))
                .thenReturn(Optional.of(ApiKeyEntity.builder().status(ApiKeyEntity.KeyStatus.EXPIRED).build()));
        assertThrows(ExpiredApiKeyException.class, () -> accessMonitorService.validateKey(randomApiKey));
    }

    @Test
    public void givenRateExceededApiKey_whenChecked_throwsError() {
        String randomApiKey = UUID.randomUUID().toString();
        Mockito.when(apiKeyRepository.findById(eq(randomApiKey)))
                .thenReturn(Optional.of(ApiKeyEntity.builder()
                        .apiKey(randomApiKey)
                        .status(ApiKeyEntity.KeyStatus.ACTIVE)
                        .build()));
        Mockito.when(accessLogRepository.getCountByApiKeyAndAccessedAt(eq(randomApiKey), any())).thenReturn(2);
        Mockito.when(apiKeyConfig.getTimePeriodInMinutes()).thenReturn(60);
        Mockito.when(apiKeyConfig.getRequestRateAllowedForPeriod()).thenReturn(1);
        assertThrows(ApiKeyRateExceededException.class, () -> accessMonitorService.validateKey(randomApiKey));
    }

    @Test
    public void givenValidApiKey_whenChecked_doesNotThrowError() {
        String randomApiKey = UUID.randomUUID().toString();
        Mockito.when(apiKeyRepository.findById(eq(randomApiKey)))
                .thenReturn(Optional.of(ApiKeyEntity.builder()
                        .apiKey(randomApiKey)
                        .status(ApiKeyEntity.KeyStatus.ACTIVE)
                        .build()));
        Mockito.when(accessLogRepository.getCountByApiKeyAndAccessedAt(eq(randomApiKey), any())).thenReturn(2);
        Mockito.when(apiKeyConfig.getTimePeriodInMinutes()).thenReturn(60);
        Mockito.when(apiKeyConfig.getRequestRateAllowedForPeriod()).thenReturn(3);
        assertDoesNotThrow(() -> accessMonitorService.validateKey(randomApiKey));
    }

    @Test
    public void whenLogged_entryGetsSaved() {
        String randomApiKey = UUID.randomUUID().toString();
        ArgumentCaptor<AccessLogEntity> argumentCaptor = ArgumentCaptor.forClass(AccessLogEntity.class);
        assertDoesNotThrow(() -> accessMonitorService.logKeyAccess(randomApiKey));
        Mockito.verify(accessLogRepository, Mockito.times(1)).save(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().getApiKey(), randomApiKey);
    }


}