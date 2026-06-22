package com.luanpaiva.observador_de_precos.modules.dashboard.dto;

import java.math.BigDecimal;

public record DashboardResponseDTO(

        Long totalProducts,

        Long totalMonitoring,

        Long activeMonitoring,

        Long inactiveMonitoring,

        Long availableProducts,

        Long unavailableProducts,

        Long unreadAlerts,

        BigDecimal lowestPrice,

        BigDecimal highestPrice
) {
}