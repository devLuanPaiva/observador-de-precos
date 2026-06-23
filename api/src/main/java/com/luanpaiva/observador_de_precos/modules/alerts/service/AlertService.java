package com.luanpaiva.observador_de_precos.modules.alerts.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.luanpaiva.observador_de_precos.modules.alerts.dto.AlertResponseDTO;
import com.luanpaiva.observador_de_precos.modules.monitoring.entity.Monitoring;

public interface AlertService {
        List<AlertResponseDTO> findAll();

        AlertResponseDTO findById(
                        UUID id);

        void markAsRead(
                        UUID id);

        void markAllAsRead();

        void createPriceDropAlert(
                        Monitoring monitoring,
                        BigDecimal oldPrice,
                        BigDecimal newPrice);

        void createTargetReachedAlert(
                        Monitoring monitoring,
                        BigDecimal currentPrice);

        void createBackInStockAlert(
                        Monitoring monitoring);

        void createPromotionAlert(
                        Monitoring monitoring);
}
