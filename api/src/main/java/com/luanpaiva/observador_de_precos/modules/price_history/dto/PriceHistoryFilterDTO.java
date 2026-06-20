package com.luanpaiva.observador_de_precos.modules.price_history.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PriceHistoryFilterDTO(

        UUID monitoringId,

        BigDecimal priceEq,

        BigDecimal priceGt,

        BigDecimal priceLt

) {
}
