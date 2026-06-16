package com.luanpaiva.observador_de_precos.modules.products.dto;


public record UpdateProductRequestDTO(

        String title,

        String imageUrl,

        String store,

        Boolean active) {
}
