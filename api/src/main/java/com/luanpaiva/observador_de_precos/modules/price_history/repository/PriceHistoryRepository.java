package com.luanpaiva.observador_de_precos.modules.price_history.repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.luanpaiva.observador_de_precos.modules.price_history.entity.PriceHistory;

public interface PriceHistoryRepository
        extends JpaRepository<PriceHistory, UUID>, JpaSpecificationExecutor<PriceHistory> {

    List<PriceHistory> findByMonitoringIdOrderByCheckedAtDesc(UUID monitoringId);

    Optional<PriceHistory> findFirstByMonitoringUserIdOrderByPriceAsc(
            UUID userId);

    Optional<PriceHistory> findFirstByMonitoringUserIdOrderByPriceDesc(
            UUID userId);
}
