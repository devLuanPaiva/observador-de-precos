package com.luanpaiva.observador_de_precos.modules.monitoring.dto;

public record MonitoringFilterDTO(

        Boolean active,

        Boolean notifyStock,

        Boolean notifyPromotion

) {
}
