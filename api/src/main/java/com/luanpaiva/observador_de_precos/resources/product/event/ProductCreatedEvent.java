package com.luanpaiva.observador_de_precos.resources.product.event;

public class ProductCreatedEvent {

    private final String productId;

    public ProductCreatedEvent(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
