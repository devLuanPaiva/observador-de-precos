package com.luanpaiva.observador_de_precos.modules.products.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateProductRequestDTO(
        @NotBlank String title,

        String url,

        String imageUrl,

        String store,

        String sku
    ) {

}
