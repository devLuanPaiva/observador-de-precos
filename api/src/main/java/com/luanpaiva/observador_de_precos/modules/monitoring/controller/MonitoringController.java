package com.luanpaiva.observador_de_precos.modules.monitoring.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.luanpaiva.observador_de_precos.modules.monitoring.dto.CreateMonitoringRequestDTO;
import com.luanpaiva.observador_de_precos.modules.monitoring.dto.MonitoringFilterDTO;
import com.luanpaiva.observador_de_precos.modules.monitoring.dto.MonitoringResponseDTO;
import com.luanpaiva.observador_de_precos.modules.monitoring.dto.UpdateMonitoringStatusDTO;
import com.luanpaiva.observador_de_precos.modules.monitoring.service.MonitoringService;
import com.luanpaiva.observador_de_precos.shared.responses.ApiResponse;
import com.luanpaiva.observador_de_precos.shared.responses.ApiResponseFactory;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/monitoring")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MonitoringController {
    private final MonitoringService monitoringService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MonitoringResponseDTO> create(
            @RequestBody @Valid CreateMonitoringRequestDTO dto) {

        return ApiResponseFactory.success(
                "Monitoramento criado com sucesso",
                monitoringService.createMonitoring(dto));
    }

    @GetMapping
    public ApiResponse<List<MonitoringResponseDTO>> findAll(
                @RequestParam(required = false) Boolean active,
                @RequestParam(required = false) Boolean notifyStock,
                @RequestParam(required = false) Boolean notifyPromotion) {

        MonitoringFilterDTO filter = new MonitoringFilterDTO(
                active,
                notifyStock,
                notifyPromotion
        );

        return ApiResponseFactory.list(
                "Monitoramentos encontrados",
                monitoringService.findAll(filter));
    }

    @GetMapping("/{id}")
    public ApiResponse<MonitoringResponseDTO> findById(
            @PathVariable UUID id) {

        return ApiResponseFactory.success(
                "Monitoramento encontrado",
                monitoringService.findById(id));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<MonitoringResponseDTO> updateStatus(
            @PathVariable UUID id,
            @RequestBody UpdateMonitoringStatusDTO dto) {

        return ApiResponseFactory.success(
                "Status atualizado com sucesso",
                monitoringService.updateStatus(
                        id,
                        dto.active()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id) {

        monitoringService.delete(id);
    }
}
