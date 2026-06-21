package com.luanpaiva.observador_de_precos.modules.dashboard.dto;

import java.math.BigDecimal;

public record DashboardResponseDTO(

        Long totalProducts,

        Long totalMonitoring,

        Long activeMonitoring,

        Long inactiveMonitoring,

        Long availableProducts,

        Long unavailableProducts,

        BigDecimal lowestPrice,

        BigDecimal highestPrice
) {
}