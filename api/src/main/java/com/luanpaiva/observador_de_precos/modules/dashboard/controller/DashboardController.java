package com.luanpaiva.observador_de_precos.modules.dashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luanpaiva.observador_de_precos.modules.dashboard.dto.DashboardResponseDTO;
import com.luanpaiva.observador_de_precos.modules.dashboard.service.DashboardService;
import com.luanpaiva.observador_de_precos.shared.responses.ApiResponse;
import com.luanpaiva.observador_de_precos.shared.responses.ApiResponseFactory;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ApiResponse<DashboardResponseDTO> dashboard() {

        return ApiResponseFactory.success(
                "Dashboard carregado com sucesso",
                dashboardService.getDashboard());
    }
}
