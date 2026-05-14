package com.luanpaiva.observador_de_precos.resources.product.service;

import com.luanpaiva.observador_de_precos.resources.product.dto.CreateProductDto;
import com.luanpaiva.observador_de_precos.resources.product.dto.ProductDto;
import com.luanpaiva.observador_de_precos.resources.product.entity.Product;
import com.luanpaiva.observador_de_precos.resources.product.event.ProductCreatedEvent;
import com.luanpaiva.observador_de_precos.resources.product.exception.ResourceNotFoundException;
import com.luanpaiva.observador_de_precos.resources.product.mapper.ProductMapper;
import com.luanpaiva.observador_de_precos.resources.product.repository.ProductRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ApplicationEventPublisher publisher;

    public ProductService(ProductRepository repository, ApplicationEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    public ProductDto create(CreateProductDto dto) {
        Product product = ProductMapper.fromCreateDto(dto);
        product.setId(UUID.randomUUID().toString());
        Product saved = repository.save(product);
        publisher.publishEvent(new ProductCreatedEvent(saved.getId()));
        return ProductMapper.toDto(saved);
    }

    public List<ProductDto> findAll() {
        return repository.findAll().stream().map(ProductMapper::toDto).collect(Collectors.toList());
    }

    public ProductDto findById(String id) {
        Product p = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        return ProductMapper.toDto(p);
    }

    public ProductDto update(String id, CreateProductDto dto) {
        Product p = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        ProductMapper.updateFromDto(p, dto);
        Product saved = repository.save(p);
        return ProductMapper.toDto(saved);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Product not found: " + id);
        repository.deleteById(id);
    }
}
