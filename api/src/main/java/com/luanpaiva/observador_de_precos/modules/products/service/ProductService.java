package com.luanpaiva.observador_de_precos.modules.products.service;

import java.util.List;
import java.util.UUID;

import com.luanpaiva.observador_de_precos.modules.products.dto.CreateProductRequestDTO;
import com.luanpaiva.observador_de_precos.modules.products.dto.ProductFilterDTO;
import com.luanpaiva.observador_de_precos.modules.products.dto.ProductResponseDTO;
import com.luanpaiva.observador_de_precos.modules.products.dto.UpdateProductRequestDTO;

public interface ProductService {

        ProductResponseDTO createProduct(
                        CreateProductRequestDTO dto);

        ProductResponseDTO update(
                        UUID id,
                        UpdateProductRequestDTO dto);

        ProductResponseDTO findById(
                        UUID id);

        List<ProductResponseDTO> findAll(
                        ProductFilterDTO filter);

        void delete(
                        UUID id);
}
