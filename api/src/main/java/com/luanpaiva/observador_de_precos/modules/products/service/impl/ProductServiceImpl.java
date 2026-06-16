package com.luanpaiva.observador_de_precos.modules.products.service.impl;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import com.luanpaiva.observador_de_precos.modules.products.dto.CreateProductRequestDTO;
import com.luanpaiva.observador_de_precos.modules.products.dto.ProductResponseDTO;
import com.luanpaiva.observador_de_precos.modules.products.dto.UpdateProductRequestDTO;
import com.luanpaiva.observador_de_precos.modules.products.entity.Product;
import com.luanpaiva.observador_de_precos.modules.products.mapper.ProductMapper;
import com.luanpaiva.observador_de_precos.modules.products.repository.ProductRepository;
import com.luanpaiva.observador_de_precos.modules.products.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDTO createProduct(
            CreateProductRequestDTO createProductRequestDTO) {
        if (createProductRequestDTO.url() != null
                && productRepository
                        .findByUrl(createProductRequestDTO.url())
                        .isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Produto já cadastrado");
        }

        Product product = Product.builder()
                .title(createProductRequestDTO.title())
                .url(createProductRequestDTO.url())
                .imageUrl(createProductRequestDTO.imageUrl())
                .store(createProductRequestDTO.store())
                .sku(createProductRequestDTO.sku())
                .available(true)
                .active(true)
                .build();

        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponseDTO update(
            UUID id,
            UpdateProductRequestDTO dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado"));

        if (dto.title() != null)
            product.setTitle(dto.title());

        if (dto.imageUrl() != null)
            product.setImageUrl(dto.imageUrl());

        if (dto.store() != null)
            product.setStore(dto.store());

        if (dto.active() != null)
            product.setActive(dto.active());

        return productMapper.toResponse(
                productRepository.save(product));
    }

    @Override
    public ProductResponseDTO findById(
            UUID id) {

        return productMapper
                .toResponse(
                        productRepository
                                .findById(id)
                                .orElseThrow(
                                        () -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Produto não encontrado")));
    }

    @Override
    public Page<ProductResponseDTO> findAll(
            String search,
            Pageable pageable) {

        Page<Product> page;

        if (search == null ||
                search.isBlank()) {

            page = productRepository.findAll(pageable);

        } else {

            page = productRepository.findByTitleContainingIgnoreCase(
                    search,
                    pageable);
        }

        return page.map(
                productMapper::toResponse);
    }

    @Override
    public void delete(
            UUID id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado"));

        productRepository.delete(product);
    }

}
