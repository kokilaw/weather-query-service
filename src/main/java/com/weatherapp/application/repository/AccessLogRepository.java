package com.weatherapp.application.repository;

import com.weatherapp.application.repository.model.AccessLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLogEntity, Long> {

}
