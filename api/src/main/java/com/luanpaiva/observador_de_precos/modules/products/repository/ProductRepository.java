package com.luanpaiva.observador_de_precos.modules.products.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.luanpaiva.observador_de_precos.modules.products.entity.Product;

@Repository
public interface ProductRepository
                extends JpaRepository<Product, UUID>,
                JpaSpecificationExecutor<Product> {

        Optional<Product> findByIdAndUserId(
                        UUID id,
                        UUID userId);
}
