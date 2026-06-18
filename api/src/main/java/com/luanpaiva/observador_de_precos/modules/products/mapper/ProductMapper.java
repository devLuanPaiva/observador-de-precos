package com.luanpaiva.observador_de_precos.modules.products.mapper;

import org.springframework.stereotype.Component;

import com.luanpaiva.observador_de_precos.modules.products.dto.ProductResponseDTO;
import com.luanpaiva.observador_de_precos.modules.products.entity.Product;

@Component
public class ProductMapper {

    public ProductResponseDTO toResponse(
            Product product) {

        return new ProductResponseDTO(
                product.getId(),
                product.getTitle(),
                product.getUrl(),
                product.getImageUrl(),
                product.getStore(),
                product.getSku(),
                product.getCurrentPrice(),
                product.getAvailable(),
                product.getActive());
    }
}