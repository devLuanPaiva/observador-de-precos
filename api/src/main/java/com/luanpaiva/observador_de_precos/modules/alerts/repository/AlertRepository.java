package com.luanpaiva.observador_de_precos.modules.alerts.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luanpaiva.observador_de_precos.modules.alerts.entity.Alert;

public interface AlertRepository extends JpaRepository<Alert, UUID> {
        List<Alert> findByMonitoringUserIdOrderByCreatedAtDesc(
                        UUID userId);

        List<Alert> findByMonitoringUserIdAndReadFalseOrderByCreatedAtDesc(
                        UUID userId);

        long countByMonitoringUserIdAndReadFalse(
                        UUID userId);
}
