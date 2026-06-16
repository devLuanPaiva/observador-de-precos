package com.luanpaiva.observador_de_precos.modules.products.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luanpaiva.observador_de_precos.modules.products.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByTitleContainingIgnoreCase(
            String title,
            Pageable pageable);

    Optional<Product> findByUrl(
            String url);
}
