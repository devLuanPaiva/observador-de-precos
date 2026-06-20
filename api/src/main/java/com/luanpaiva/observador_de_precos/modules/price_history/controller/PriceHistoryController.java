package com.luanpaiva.observador_de_precos.modules.price_history.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luanpaiva.observador_de_precos.modules.price_history.dto.PriceHistoryFilterDTO;
import com.luanpaiva.observador_de_precos.modules.price_history.dto.PriceHistoryResponseDTO;
import com.luanpaiva.observador_de_precos.modules.price_history.service.PriceHistoryService;
import com.luanpaiva.observador_de_precos.shared.responses.ApiResponse;
import com.luanpaiva.observador_de_precos.shared.responses.ApiResponseFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/price-history")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PriceHistoryController {

    private final PriceHistoryService service;

    @GetMapping
    public ApiResponse<List<PriceHistoryResponseDTO>> findAll(
            @RequestParam(required = false) UUID monitoringId,
            @RequestParam(required = false) BigDecimal priceEq,
            @RequestParam(required = false) BigDecimal priceGt,
            @RequestParam(required = false) BigDecimal priceLt) {
        PriceHistoryFilterDTO filter = new PriceHistoryFilterDTO(
                monitoringId,
                priceEq,
                priceGt,
                priceLt);
        return ApiResponseFactory.list(
                "Histórico encontrado",
                service.findAll(filter));
    }

    @GetMapping("/{id}")
    public ApiResponse<PriceHistoryResponseDTO> findById(
            @PathVariable UUID id) {

        return ApiResponseFactory.success(
                "Histórico encontrado",
                service.findById(id));
    }
}
