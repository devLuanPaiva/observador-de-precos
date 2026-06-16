package com.luanpaiva.observador_de_precos.modules.products.service;

import java.util.UUID;

import com.luanpaiva.observador_de_precos.modules.products.dto.CreateProductRequestDTO;
import com.luanpaiva.observador_de_precos.modules.products.dto.ProductFilterDTO;
import com.luanpaiva.observador_de_precos.modules.products.dto.ProductResponseDTO;
import com.luanpaiva.observador_de_precos.modules.products.dto.UpdateProductRequestDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponseDTO createProduct(
            CreateProductRequestDTO createProductRequestDTO);

    ProductResponseDTO update(
            UUID id,
            UpdateProductRequestDTO dto);

    ProductResponseDTO findById(
            UUID id);

    Page<ProductResponseDTO> findAll(
            ProductFilterDTO filter,
            Pageable pageable);

    void delete(
            UUID id);
}
