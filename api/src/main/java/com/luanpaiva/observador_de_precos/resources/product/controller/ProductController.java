package com.luanpaiva.observador_de_precos.resources.product.controller;

import com.luanpaiva.observador_de_precos.resources.product.dto.CreateProductDto;
import com.luanpaiva.observador_de_precos.resources.product.dto.ProductDto;
import com.luanpaiva.observador_de_precos.resources.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody CreateProductDto dto) {
        ProductDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/api/products/" + created.getId())).body(created);
    }

    @GetMapping
    public List<ProductDto> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProductDto get(@PathVariable String id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public ProductDto update(@PathVariable String id, @RequestBody CreateProductDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
