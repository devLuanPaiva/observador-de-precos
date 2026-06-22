package com.luanpaiva.observador_de_precos.modules.alerts.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.luanpaiva.observador_de_precos.modules.alerts.enums.AlertType;

public record AlertResponseDTO(

        UUID id,

        UUID monitoringId,

        AlertType type,

        String message,

        Boolean read,

        LocalDateTime createdAt) {
}
