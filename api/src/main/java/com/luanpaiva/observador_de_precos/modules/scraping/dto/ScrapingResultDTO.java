package com.luanpaiva.observador_de_precos.modules.scraping.dto;

import java.math.BigDecimal;

public record ScrapingResultDTO(
        String title,

        BigDecimal price,

        Boolean available,

        String imageUrl,

        String store,

        String sku) {

}
