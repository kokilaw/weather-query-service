package com.weatherapp.application.repository;

import com.weatherapp.application.repository.model.ApiKeyEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ApiKeyRepositoryTest {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void givenNewEntity_whenSaved_thenSuccess() {
        ApiKeyEntity apiKeyEntity = ApiKeyEntity.builder()
                .apiKey(UUID.randomUUID().toString())
                .status(ApiKeyEntity.KeyStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        apiKeyRepository.save(apiKeyEntity);
        assertEquals(entityManager.find(ApiKeyEntity.class, apiKeyEntity.getApiKey()), apiKeyEntity);
    }

}