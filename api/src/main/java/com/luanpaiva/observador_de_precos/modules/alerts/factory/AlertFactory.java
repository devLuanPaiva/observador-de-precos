package com.luanpaiva.observador_de_precos.modules.alerts.factory;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.luanpaiva.observador_de_precos.modules.alerts.entity.Alert;
import com.luanpaiva.observador_de_precos.modules.alerts.enums.AlertType;
import com.luanpaiva.observador_de_precos.modules.monitoring.entity.Monitoring;

@Component
public class AlertFactory {

    public Alert createPriceDropAlert(
            Monitoring monitoring,
            BigDecimal oldPrice,
            BigDecimal newPrice) {

        return Alert.builder()
                .monitoring(monitoring)
                .type(AlertType.PRICE_DROP)
                .message(String.format(
                        "O produto '%s' caiu de R$ %.2f para R$ %.2f",
                        monitoring.getProduct().getTitle(),
                        oldPrice,
                        newPrice))
                .read(false)
                .build();
    }

    public Alert createTargetReachedAlert(
            Monitoring monitoring,
            BigDecimal currentPrice) {

        return Alert.builder()
                .monitoring(monitoring)
                .type(AlertType.TARGET_REACHED)
                .message(String.format(
                        "O produto '%s' atingiu o preço alvo de R$ %.2f",
                        monitoring.getProduct().getTitle(),
                        currentPrice))
                .read(false)
                .build();
    }

    public Alert createBackInStockAlert(
            Monitoring monitoring) {

        return Alert.builder()
                .monitoring(monitoring)
                .type(AlertType.BACK_IN_STOCK)
                .message(String.format(
                        "O produto '%s' voltou ao estoque",
                        monitoring.getProduct().getTitle()))
                .read(false)
                .build();
    }

    public Alert createPromotionAlert(
            Monitoring monitoring) {

        return Alert.builder()
                .monitoring(monitoring)
                .type(AlertType.PROMOTION)
                .message(String.format(
                        "Promoção detectada para '%s'",
                        monitoring.getProduct().getTitle()))
                .read(false)
                .build();
    }
}
