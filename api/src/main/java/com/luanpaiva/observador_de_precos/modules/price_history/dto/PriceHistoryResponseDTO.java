package com.luanpaiva.observador_de_precos.modules.price_history.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PriceHistoryResponseDTO(

        UUID id,

        UUID monitoringId,

        BigDecimal price,

        Boolean available,

        LocalDateTime checkedAt

) {
}
