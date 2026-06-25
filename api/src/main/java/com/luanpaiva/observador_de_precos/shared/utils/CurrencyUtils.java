package com.luanpaiva.observador_de_precos.shared.utils;

import java.math.BigDecimal;

public final class CurrencyUtils {

    private CurrencyUtils() {
    }

    public static BigDecimal parseBrazilianPrice(String value) {

        if (value == null || value.isBlank()) {
            return BigDecimal.ZERO;
        }

        String normalized = value
                .replace("R$", "")
                .replace(".", "")
                .replace(",", ".")
                .trim();

        return new BigDecimal(normalized);
    }
}
