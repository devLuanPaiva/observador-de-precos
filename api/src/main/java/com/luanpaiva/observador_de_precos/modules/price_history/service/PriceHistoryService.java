package com.luanpaiva.observador_de_precos.modules.price_history.service;

import java.util.List;
import java.util.UUID;

import com.luanpaiva.observador_de_precos.modules.price_history.dto.PriceHistoryFilterDTO;
import com.luanpaiva.observador_de_precos.modules.price_history.dto.PriceHistoryResponseDTO;

public interface PriceHistoryService {
    List<PriceHistoryResponseDTO> findAll(PriceHistoryFilterDTO filter);

    PriceHistoryResponseDTO findById(UUID id);

    List<PriceHistoryResponseDTO> findByMonitoringId(UUID monitoringId);
}
