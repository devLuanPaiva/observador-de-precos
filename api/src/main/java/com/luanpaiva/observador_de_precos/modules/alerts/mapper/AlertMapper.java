package com.luanpaiva.observador_de_precos.modules.alerts.mapper;

import org.springframework.stereotype.Component;

import com.luanpaiva.observador_de_precos.modules.alerts.dto.AlertResponseDTO;
import com.luanpaiva.observador_de_precos.modules.alerts.entity.Alert;

@Component
public class AlertMapper {

    public AlertResponseDTO toResponse(
            Alert alert) {

        return new AlertResponseDTO(
                alert.getId(),
                alert.getMonitoring().getId(),
                alert.getType(),
                alert.getMessage(),
                alert.getRead(),
                alert.getCreatedAt());
    }
}
