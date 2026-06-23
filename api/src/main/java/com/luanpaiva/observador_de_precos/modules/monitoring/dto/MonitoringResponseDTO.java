package com.luanpaiva.observador_de_precos.modules.monitoring.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record MonitoringResponseDTO(

        UUID id,

        UUID productId,

        String productTitle,

        BigDecimal currentPrice,

        BigDecimal targetPrice,

        Boolean notifyStock,

        Boolean notifyPromotion,

        Boolean active

) {
}
