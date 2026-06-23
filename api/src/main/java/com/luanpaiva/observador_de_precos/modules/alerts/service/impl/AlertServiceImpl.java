package com.luanpaiva.observador_de_precos.modules.alerts.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.luanpaiva.observador_de_precos.modules.alerts.dto.AlertResponseDTO;
import com.luanpaiva.observador_de_precos.modules.alerts.entity.Alert;
import com.luanpaiva.observador_de_precos.modules.alerts.factory.AlertFactory;
import com.luanpaiva.observador_de_precos.modules.alerts.mapper.AlertMapper;
import com.luanpaiva.observador_de_precos.modules.alerts.repository.AlertRepository;
import com.luanpaiva.observador_de_precos.modules.alerts.service.AlertService;
import com.luanpaiva.observador_de_precos.modules.monitoring.entity.Monitoring;
import com.luanpaiva.observador_de_precos.security.SecurityContextHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {
    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;
    private final SecurityContextHelper securityContextHelper;
    private final AlertFactory alertFactory;

    @Override
    public List<AlertResponseDTO> findAll() {
        UUID userId = securityContextHelper.getCurrentUserId();

        return alertRepository
                .findByMonitoringUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(alertMapper::toResponse)
                .toList();
    }

    @Override
    public AlertResponseDTO findById(UUID id) {
        UUID userId = securityContextHelper.getCurrentUserId();

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Alerta não encontrado"));

        if (!alert.getMonitoring().getUser().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não tem permissão para acessar este alerta");
        }

        return alertMapper.toResponse(alert);
    }

    @Override
    public void markAsRead(UUID id) {
        UUID userId = securityContextHelper.getCurrentUserId();

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Alerta não encontrado"));
        if (!alert.getMonitoring().getUser().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não tem permissão para acessar este alerta");
        }

        alert.setRead(true);
        alertRepository.save(alert);
    }

    @Override
    public void markAllAsRead() {
        UUID userId = securityContextHelper.getCurrentUserId();

        List<Alert> alerts = alertRepository
                .findByMonitoringUserIdAndReadFalseOrderByCreatedAtDesc(userId);

        alerts.forEach(alert -> alert.setRead(true));
        alertRepository.saveAll(alerts);
    }

    @Override
    public void createPriceDropAlert(
            Monitoring monitoring,
            BigDecimal oldPrice,
            BigDecimal newPrice) {

        alertRepository.save(
                alertFactory.createPriceDropAlert(
                        monitoring,
                        oldPrice,
                        newPrice));
    }

    @Override
    public void createTargetReachedAlert(
            Monitoring monitoring,
            BigDecimal currentPrice) {

        alertRepository.save(
                alertFactory.createTargetReachedAlert(
                        monitoring,
                        currentPrice));
    }

    @Override
    public void createBackInStockAlert(
            Monitoring monitoring) {

        alertRepository.save(
                alertFactory.createBackInStockAlert(
                        monitoring));
    }

    @Override
    public void createPromotionAlert(
            Monitoring monitoring) {

        alertRepository.save(
                alertFactory.createPromotionAlert(
                        monitoring));
    }
}
