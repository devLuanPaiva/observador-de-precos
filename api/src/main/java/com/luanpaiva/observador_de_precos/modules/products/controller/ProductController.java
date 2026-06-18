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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.luanpaiva.observador_de_precos.modules.products.dto.CreateProductRequestDTO;
import com.luanpaiva.observador_de_precos.modules.products.dto.ProductFilterDTO;
import com.luanpaiva.observador_de_precos.modules.products.dto.ProductResponseDTO;
import com.luanpaiva.observador_de_precos.modules.products.dto.UpdateProductRequestDTO;
import com.luanpaiva.observador_de_precos.modules.products.service.ProductService;
import com.luanpaiva.observador_de_precos.shared.responses.ApiResponse;
import com.luanpaiva.observador_de_precos.shared.responses.ApiResponseFactory;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductResponseDTO> create(
            @RequestBody @Valid CreateProductRequestDTO dto) {

        return ApiResponseFactory.success(
                "Produto criado com sucesso",
                productService.createProduct(dto));
    }

    @GetMapping
    public ApiResponse<List<ProductResponseDTO>> findAll(

            @RequestParam(required = false) String title,

            @RequestParam(required = false) String url,

            @RequestParam(required = false) String store,

            @RequestParam(required = false) String sku,

            @RequestParam(required = false) Boolean active,

            @RequestParam(required = false) Boolean available,

            @RequestParam(required = false) BigDecimal currentPriceEq,

            @RequestParam(required = false) BigDecimal currentPriceGt,

            @RequestParam(required = false) BigDecimal currentPriceLt) {

        ProductFilterDTO filter = new ProductFilterDTO(
                title,
                url,
                store,
                sku,
                active,
                available,
                currentPriceEq,
                currentPriceGt,
                currentPriceLt);

        return ApiResponseFactory.list(
                "Produtos encontrados",
                productService.findAll(filter));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponseDTO> findById(
            @PathVariable UUID id) {

        return ApiResponseFactory.success(
                "Produto encontrado",
                productService.findById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponseDTO> update(
            @PathVariable UUID id,

            @RequestBody UpdateProductRequestDTO dto) {

        return ApiResponseFactory.success(
                "Produto atualizado com sucesso",
                productService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id) {

        productService.delete(id);
    }
}
