package com.luanpaiva.observador_de_precos.resources.product.mapper;

import com.luanpaiva.observador_de_precos.resources.product.dto.CreateProductDto;
import com.luanpaiva.observador_de_precos.resources.product.dto.ProductDto;
import com.luanpaiva.observador_de_precos.resources.product.entity.Product;

public class ProductMapper {

    public static ProductDto toDto(Product e) {
        if (e == null) return null;
        ProductDto d = new ProductDto();
        d.setId(e.getId());
        d.setTitle(e.getTitle());
        d.setUrl(e.getUrl());
        d.setImageUrl(e.getImageUrl());
        d.setCurrentPrice(e.getCurrentPrice());
        d.setCreatedAt(e.getCreatedAt());
        d.setUpdatedAt(e.getUpdatedAt());
        d.setUserId(e.getUserId());
        return d;
    }

    public static Product fromCreateDto(CreateProductDto d) {
        if (d == null) return null;
        Product e = new Product();
        e.setTitle(d.getTitle());
        e.setUrl(d.getUrl());
        e.setImageUrl(d.getImageUrl());
        e.setCurrentPrice(d.getCurrentPrice());
        e.setUserId(d.getUserId());
        return e;
    }

    public static void updateFromDto(Product product, CreateProductDto d) {
        if (d == null || product == null) return;
        product.setTitle(d.getTitle());
        product.setUrl(d.getUrl());
        product.setImageUrl(d.getImageUrl());
        product.setCurrentPrice(d.getCurrentPrice());
        product.setUserId(d.getUserId());
    }
}
