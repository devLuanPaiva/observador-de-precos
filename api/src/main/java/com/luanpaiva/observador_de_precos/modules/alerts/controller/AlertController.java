package com.luanpaiva.observador_de_precos.modules.alerts.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import com.luanpaiva.observador_de_precos.modules.alerts.dto.AlertResponseDTO;
import com.luanpaiva.observador_de_precos.modules.alerts.service.AlertService;
import com.luanpaiva.observador_de_precos.shared.responses.ApiResponse;
import com.luanpaiva.observador_de_precos.shared.responses.ApiResponseFactory;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AlertController {
    private final AlertService alertService;

    @GetMapping
    public ApiResponse<List<AlertResponseDTO>> findAll() {

        return ApiResponseFactory.success(
                "Alertas encontrados",
                alertService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<AlertResponseDTO> findById(
            @PathVariable UUID id) {

        return ApiResponseFactory.success(
                "Alerta encontrado",
                alertService.findById(id));
    }

    @PatchMapping("/{id}/read")
    public ApiResponse<Void> markAsRead(
            @PathVariable UUID id) {

        alertService.markAsRead(id);

        return ApiResponseFactory.success(
                "Alerta marcado como lido",
                null);
    }

    @PatchMapping("/read-all")
    public ApiResponse<Void> markAllAsRead() {

        alertService.markAllAsRead();

        return ApiResponseFactory.success(
                "Todos os alertas foram marcados como lidos",
                null);
    }

}
