package com.luanpaiva.observador_de_precos.modules.products.dto;

import java.math.BigDecimal;

public record ProductFilterDTO(

        String title,

        String url,

        String store,

        String sku,

        Boolean active,

        Boolean available,

        BigDecimal currentPriceEq,

        BigDecimal currentPriceGt,

        BigDecimal currentPriceLt

) {
}
