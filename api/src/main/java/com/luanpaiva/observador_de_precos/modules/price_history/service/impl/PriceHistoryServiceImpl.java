package com.luanpaiva.observador_de_precos.modules.price_history.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.luanpaiva.observador_de_precos.modules.monitoring.entity.Monitoring;
import com.luanpaiva.observador_de_precos.modules.monitoring.repository.MonitoringRepository;
import com.luanpaiva.observador_de_precos.modules.price_history.dto.PriceHistoryFilterDTO;
import com.luanpaiva.observador_de_precos.modules.price_history.dto.PriceHistoryResponseDTO;
import com.luanpaiva.observador_de_precos.modules.price_history.entity.PriceHistory;
import com.luanpaiva.observador_de_precos.modules.price_history.mapper.PriceHistoryMapper;
import com.luanpaiva.observador_de_precos.modules.price_history.repository.PriceHistoryRepository;
import com.luanpaiva.observador_de_precos.modules.price_history.service.PriceHistoryService;
import com.luanpaiva.observador_de_precos.modules.price_history.specification.PriceHistorySpecification;
import com.luanpaiva.observador_de_precos.security.SecurityContextHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PriceHistoryServiceImpl implements PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;
    private final PriceHistoryMapper priceHistoryMapper;
    private final SecurityContextHelper securityContextHelper;
    private final MonitoringRepository monitoringRepository;

    @Override
    public List<PriceHistoryResponseDTO> findAll(
            PriceHistoryFilterDTO filter) {

        UUID currentUserId = securityContextHelper.getCurrentUserId();

        return priceHistoryRepository
                .findAll(
                        PriceHistorySpecification.filter(
                                currentUserId,
                                filter))
                .stream()
                .map(priceHistoryMapper::toResponse)
                .toList();
    }

    @Override
    public List<PriceHistoryResponseDTO> findByMonitoringId(
            UUID monitoringId) {

        UUID currentUserId = securityContextHelper.getCurrentUserId();

        Monitoring monitoring = monitoringRepository.findById(monitoringId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Monitoramento não encontrado"));

        if (!monitoring
                .getUser()
                .getId()
                .equals(currentUserId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Acesso negado");
        }

        return priceHistoryRepository
                .findByMonitoringIdOrderByCheckedAtDesc(
                        monitoringId)
                .stream()
                .map(priceHistoryMapper::toResponse)
                .toList();
    }

    @Override
    public PriceHistoryResponseDTO findById(
            UUID id) {

        UUID currentUserId = securityContextHelper.getCurrentUserId();

        PriceHistory history = priceHistoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Histórico não encontrado"));

        if (!history
                .getMonitoring()
                .getUser()
                .getId()
                .equals(currentUserId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Acesso negado");
        }

        return priceHistoryMapper.toResponse(history);
    }
}