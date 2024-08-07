package com.weatherapp.application.repository;

import com.weatherapp.application.repository.model.AccessLogEntity;
import com.weatherapp.application.repository.model.ApiKeyEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class AccessLogRepositoryTest {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void givenNewEntity_whenSaved_thenSuccess() {

        ApiKeyEntity apiKeyEntity = ApiKeyEntity.builder()
                .apiKey(UUID.randomUUID().toString())
                .status(ApiKeyEntity.KeyStatus.ACTIVE)
                .build();
        entityManager.persist(apiKeyEntity);

        AccessLogEntity accessLogEntity = AccessLogEntity.builder()
                .apiKey(apiKeyEntity.getApiKey())
                .accessedAt(LocalDateTime.now())
                .build();
        accessLogRepository.save(accessLogEntity);

        List<AccessLogEntity> allSavedEntities = accessLogRepository.findAll();
        assertEquals(allSavedEntities.size(), 1);
        allSavedEntities.stream().findFirst().ifPresent(savedEntity -> {
            assertNotNull(savedEntity.getAccessedAt());
            assertNotNull(savedEntity.getId());
        });

    }

    @Test
    @DisplayName("Only entries accessed during defined period are returned")
    public void givenAccessLogs_inLastNMinutes_thenReturned() {


        String apiKey = UUID.randomUUID().toString();

        ApiKeyEntity apiKeyEntity = ApiKeyEntity.builder()
                .apiKey(apiKey)
                .status(ApiKeyEntity.KeyStatus.ACTIVE)
                .build();
        entityManager.persist(apiKeyEntity);

        entityManager.persist(
                AccessLogEntity.builder()
                        .apiKey(apiKey)
                        .accessedAt(LocalDateTime.now())
                        .build()
        );
        entityManager.persist(
                AccessLogEntity.builder()
                        .apiKey(apiKey)
                        .accessedAt(LocalDateTime.now())
                        .build()
        );
        entityManager.persist(
                AccessLogEntity.builder()
                        .apiKey(apiKey)
                        .accessedAt(LocalDateTime.now().minusMinutes(45))
                        .build()
        );
        assertEquals(2,
                accessLogRepository.getCountByApiKeyAndAccessedAt(apiKey, LocalDateTime.now().minusMinutes(30))
        );

    }

}