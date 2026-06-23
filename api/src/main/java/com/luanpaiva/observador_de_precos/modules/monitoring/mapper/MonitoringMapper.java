package com.luanpaiva.observador_de_precos.modules.monitoring.mapper;

import org.springframework.stereotype.Component;

import com.luanpaiva.observador_de_precos.modules.monitoring.dto.MonitoringResponseDTO;
import com.luanpaiva.observador_de_precos.modules.monitoring.entity.Monitoring;

@Component
public class MonitoringMapper {

    public MonitoringResponseDTO toResponse(
            Monitoring monitoring) {

        return new MonitoringResponseDTO(

                monitoring.getId(),

                monitoring.getProduct().getId(),

                monitoring.getProduct().getTitle(),

                monitoring.getProduct().getCurrentPrice(),

                monitoring.getTargetPrice(),

                monitoring.getNotifyStock(),

                monitoring.getNotifyPromotion(),

                monitoring.getActive());
    }
}
