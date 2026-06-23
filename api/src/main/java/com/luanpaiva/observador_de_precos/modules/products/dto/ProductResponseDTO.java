package com.luanpaiva.observador_de_precos.modules.products.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponseDTO(
        UUID id,
        String title,
        String url,
        String imageUrl,
        String store,
        String sku,
        BigDecimal currentPrice,
        Boolean available,
        Boolean active) {

}
