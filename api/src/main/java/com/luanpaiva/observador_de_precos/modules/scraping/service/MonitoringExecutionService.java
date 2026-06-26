package com.luanpaiva.observador_de_precos.modules.scraping.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.luanpaiva.observador_de_precos.modules.alerts.service.AlertService;
import com.luanpaiva.observador_de_precos.modules.monitoring.entity.Monitoring;
import com.luanpaiva.observador_de_precos.modules.monitoring.repository.MonitoringRepository;
import com.luanpaiva.observador_de_precos.modules.price_history.entity.PriceHistory;
import com.luanpaiva.observador_de_precos.modules.price_history.repository.PriceHistoryRepository;
import com.luanpaiva.observador_de_precos.modules.products.entity.Product;
import com.luanpaiva.observador_de_precos.modules.products.repository.ProductRepository;
import com.luanpaiva.observador_de_precos.modules.scraping.dto.ScrapingResultDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonitoringExecutionService {
    private final MonitoringRepository monitoringRepository;

    private final ProductRepository productRepository;

    private final PriceHistoryRepository historyRepository;

    private final AlertService alertService;

    private final ScrapingService scrapingService;

    public void execute(UUID monitoringId) {
        Monitoring monitoring = monitoringRepository.findById(monitoringId)
                .orElseThrow(
                    () -> new IllegalArgumentException("Monitoramento não encontrado")
                );

        Product product = monitoring.getProduct();

        BigDecimal oldPrice = product.getCurrentPrice();

        ScrapingResultDTO result = scrapingService.scrape(product.getUrl());

        product.setCurrentPrice(result.price());

        product.setAvailable(result.available());

        productRepository.save(product);

        PriceHistory history = PriceHistory.builder()
                .monitoring(monitoring)
                .price(result.price())
                .available(result.available())
                .checkedAt(LocalDateTime.now())
                .build();

        historyRepository.save(history);

        processAlerts(
                monitoring,
                oldPrice,
                result.price(),
                result.available());
    }

    private void processAlerts(Monitoring monitoring, BigDecimal oldPrice, BigDecimal newPrice, boolean available) {
        if (oldPrice != null && newPrice.compareTo(oldPrice) < 0) {
            alertService.createPriceDropAlert(monitoring, oldPrice, newPrice);
        }

        if (monitoring.getTargetPrice() != null && newPrice.compareTo(monitoring.getTargetPrice()) <= 0) {
            alertService.createTargetReachedAlert(monitoring, newPrice);
        }

        if (available && Boolean.TRUE.equals(monitoring.getNotifyStock())) {
            alertService.createBackInStockAlert(monitoring);
        }
    }
}
