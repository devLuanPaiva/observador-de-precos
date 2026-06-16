package com.luanpaiva.observador_de_precos.modules.products.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import com.luanpaiva.observador_de_precos.modules.products.dto.CreateProductRequestDTO;
import com.luanpaiva.observador_de_precos.modules.products.dto.ProductResponseDTO;
import com.luanpaiva.observador_de_precos.modules.products.dto.UpdateProductRequestDTO;
import com.luanpaiva.observador_de_precos.modules.products.service.ProductService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDTO createProduct(
            @RequestBody @Valid CreateProductRequestDTO createProductRequestDTO) {
        return productService.createProduct(createProductRequestDTO);
    }

    @GetMapping
    public Page<ProductResponseDTO> findAll(
            @RequestParam(required = false) String search,

            Pageable pageable) {

        return productService.findAll(
                search,
                pageable);
    }

    @GetMapping("/{id}")
    public ProductResponseDTO findById(
            @PathVariable UUID id) {

        return productService.findById(id);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO update(
            @PathVariable UUID id,

            @RequestBody UpdateProductRequestDTO dto) {

        return productService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id) {

        productService.delete(id);
    }
}
