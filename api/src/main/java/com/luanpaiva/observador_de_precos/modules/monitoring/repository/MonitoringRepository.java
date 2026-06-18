package com.luanpaiva.observador_de_precos.modules.monitoring.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.luanpaiva.observador_de_precos.modules.monitoring.entity.Monitoring;

public interface MonitoringRepository extends JpaRepository<Monitoring, UUID>, JpaSpecificationExecutor<Monitoring> {
    Optional<Monitoring> findByIdAndUserId(
            UUID id,
            UUID userId);

    boolean existsByUserIdAndProductId(
            UUID userId,
            UUID productId);

}
