package com.luanpaiva.observador_de_precos.resources.product.repository;

import com.luanpaiva.observador_de_precos.resources.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

}
