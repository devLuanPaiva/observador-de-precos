package com.luanpaiva.observador_de_precos.modules.price_history.mapper;

import org.springframework.stereotype.Component;

import com.luanpaiva.observador_de_precos.modules.price_history.dto.PriceHistoryResponseDTO;
import com.luanpaiva.observador_de_precos.modules.price_history.entity.PriceHistory;

@Component
public class PriceHistoryMapper {

    public PriceHistoryResponseDTO toResponse(PriceHistory priceHistory) {
    
        return new PriceHistoryResponseDTO(
                priceHistory.getId(),
                priceHistory.getMonitoring().getId(),
                priceHistory.getPrice(),
                priceHistory.getAvailable(),
                priceHistory.getCheckedAt());
    }
}
