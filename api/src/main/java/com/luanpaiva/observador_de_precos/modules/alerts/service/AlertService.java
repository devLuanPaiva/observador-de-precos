package com.luanpaiva.observador_de_precos.modules.alerts.service;

import java.util.List;
import java.util.UUID;

import com.luanpaiva.observador_de_precos.modules.alerts.dto.AlertResponseDTO;

public interface AlertService {
    List<AlertResponseDTO> findAll();

    AlertResponseDTO findById(
            UUID id);

    void markAsRead(
            UUID id);

    void markAllAsRead();
}
