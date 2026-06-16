package com.luanpaiva.observador_de_precos.modules.products.service.impl;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import com.luanpaiva.observador_de_precos.modules.products.dto.CreateProductRequestDTO;
import com.luanpaiva.observador_de_precos.modules.products.dto.ProductFilterDTO;
import com.luanpaiva.observador_de_precos.modules.products.dto.ProductResponseDTO;
import com.luanpaiva.observador_de_precos.modules.products.dto.UpdateProductRequestDTO;
import com.luanpaiva.observador_de_precos.modules.products.entity.Product;
import com.luanpaiva.observador_de_precos.modules.products.mapper.ProductMapper;
import com.luanpaiva.observador_de_precos.modules.products.repository.ProductRepository;
import com.luanpaiva.observador_de_precos.modules.products.service.ProductService;
import com.luanpaiva.observador_de_precos.modules.products.specification.ProductSpecification;
import com.luanpaiva.observador_de_precos.modules.users.entity.User;
import com.luanpaiva.observador_de_precos.security.SecurityContextHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
        private final ProductRepository productRepository;
        private final ProductMapper productMapper;
        private final SecurityContextHelper securityContextHelper;

        @Override
        public ProductResponseDTO createProduct(
                        CreateProductRequestDTO dto) {

                User currentUser = securityContextHelper.getCurrentUser();

                Product product = Product.builder()
                                .title(dto.title())
                                .url(dto.url())
                                .imageUrl(dto.imageUrl())
                                .store(dto.store())
                                .sku(dto.sku())
                                .active(true)
                                .available(true)
                                .user(currentUser)
                                .build();

                Product saved = productRepository.save(product);

                return productMapper.toResponse(saved);
        }

        @Override
        public Page<ProductResponseDTO> findAll(
                        ProductFilterDTO filter,
                        Pageable pageable) {

                UUID userId = securityContextHelper.getCurrentUserId();

                return productRepository
                                .findAll(
                                                ProductSpecification.filter(
                                                                userId,
                                                                filter),
                                                pageable)
                                .map(productMapper::toResponse);
        }

        @Override
        public ProductResponseDTO update(
                        UUID id,
                        UpdateProductRequestDTO dto) {

                UUID currentUserId = securityContextHelper.getCurrentUserId();

                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Produto não encontrado"));

                if (!product.getUser().getId().equals(currentUserId)) {

                        throw new ResponseStatusException(
                                        HttpStatus.FORBIDDEN,
                                        "Acesso negado");
                }

                if (dto.title() != null) {
                        product.setTitle(dto.title());
                }

                if (dto.imageUrl() != null) {
                        product.setImageUrl(dto.imageUrl());
                }

                if (dto.store() != null) {
                        product.setStore(dto.store());
                }

                if (dto.active() != null) {
                        product.setActive(dto.active());
                }

                Product updated = productRepository.save(product);

                return productMapper.toResponse(updated);
        }

        @Override
        public ProductResponseDTO findById(
                        UUID id) {

                UUID currentUserId = securityContextHelper.getCurrentUserId();

                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Produto não encontrado"));

                if (!product.getUser().getId().equals(currentUserId)) {

                        throw new ResponseStatusException(
                                        HttpStatus.FORBIDDEN,
                                        "Acesso negado");
                }

                return productMapper.toResponse(product);
        }

        @Override
        public void delete(
                        UUID id) {

                UUID currentUserId = securityContextHelper.getCurrentUserId();

                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Produto não encontrado"));

                if (!product.getUser().getId().equals(currentUserId)) {

                        throw new ResponseStatusException(
                                        HttpStatus.FORBIDDEN,
                                        "Acesso negado");
                }

                productRepository.delete(product);
        }

}
