package com.luanpaiva.observador_de_precos.modules.monitoring.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateMonitoringStatusDTO(

        @NotNull(message = "O campo 'active' é obrigatório.") 
        Boolean active

) {
}
