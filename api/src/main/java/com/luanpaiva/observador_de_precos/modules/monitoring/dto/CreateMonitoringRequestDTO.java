package com.luanpaiva.observador_de_precos.modules.monitoring.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record CreateMonitoringRequestDTO(
        @NotNull(message = "O campo 'productId' é obrigatório.") UUID productId,

        BigDecimal targetPrice,

        Boolean notifyStock,

        Boolean notifyPromotion) {

}
