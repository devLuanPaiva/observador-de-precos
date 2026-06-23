package com.luanpaiva.observador_de_precos.modules.monitoring.service;

import java.util.List;
import java.util.UUID;

import com.luanpaiva.observador_de_precos.modules.monitoring.dto.CreateMonitoringRequestDTO;
import com.luanpaiva.observador_de_precos.modules.monitoring.dto.MonitoringFilterDTO;
import com.luanpaiva.observador_de_precos.modules.monitoring.dto.MonitoringResponseDTO;

public interface MonitoringService {
    MonitoringResponseDTO createMonitoring(
            CreateMonitoringRequestDTO dto);

    MonitoringResponseDTO findById(
            UUID id);

    List<MonitoringResponseDTO> findAll(
            MonitoringFilterDTO filter);

    MonitoringResponseDTO updateStatus(
            UUID id,
            Boolean active);

    void delete(
            UUID id);
}
