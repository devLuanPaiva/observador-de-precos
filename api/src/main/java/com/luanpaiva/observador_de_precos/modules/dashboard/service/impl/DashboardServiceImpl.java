package com.luanpaiva.observador_de_precos.modules.dashboard.service.impl;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.luanpaiva.observador_de_precos.modules.alerts.repository.AlertRepository;
import com.luanpaiva.observador_de_precos.modules.dashboard.dto.DashboardResponseDTO;
import com.luanpaiva.observador_de_precos.modules.dashboard.service.DashboardService;
import com.luanpaiva.observador_de_precos.modules.monitoring.repository.MonitoringRepository;
import com.luanpaiva.observador_de_precos.modules.price_history.repository.PriceHistoryRepository;
import com.luanpaiva.observador_de_precos.modules.products.repository.ProductRepository;
import com.luanpaiva.observador_de_precos.security.SecurityContextHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

        private final ProductRepository productRepository;

        private final MonitoringRepository monitoringRepository;

        private final PriceHistoryRepository priceHistoryRepository;

        private final AlertRepository alertRepository;

        private final SecurityContextHelper securityContextHelper;

        @Override
        public DashboardResponseDTO getDashboard() {

                UUID userId = securityContextHelper.getCurrentUserId();

                long totalProducts = productRepository.countByUserId(userId);

                long totalMonitoring = monitoringRepository.countByUserId(userId);

                long activeMonitoring = monitoringRepository.countByUserIdAndActiveTrue(userId);

                long inactiveMonitoring = monitoringRepository.countByUserIdAndActiveFalse(userId);

                long availableProducts = productRepository.countByUserIdAndAvailableTrue(userId);

                long unavailableProducts = productRepository.countByUserIdAndAvailableFalse(userId);

                long unreadAlerts = alertRepository.countByMonitoringUserIdAndReadFalse(userId);

                BigDecimal lowestPrice = priceHistoryRepository
                                .findFirstByMonitoringUserIdOrderByPriceAsc(userId)
                                .map(history -> history.getPrice())
                                .orElse(BigDecimal.ZERO);

                BigDecimal highestPrice = priceHistoryRepository
                                .findFirstByMonitoringUserIdOrderByPriceDesc(userId)
                                .map(history -> history.getPrice())
                                .orElse(BigDecimal.ZERO);

                return new DashboardResponseDTO(
                                totalProducts,
                                totalMonitoring,
                                activeMonitoring,
                                inactiveMonitoring,
                                availableProducts,
                                unavailableProducts,
                                unreadAlerts,
                                lowestPrice,
                                highestPrice);
        }
}