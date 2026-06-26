package com.luanpaiva.observador_de_precos.modules.scraping.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import com.luanpaiva.observador_de_precos.modules.scraping.strategy.ScraperStrategy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScraperFactory {
    private final List<ScraperStrategy> strategies;

    public ScraperStrategy getStrategy(String url) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(url))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Estratégia não encontrada para a URL fornecida: " + url));
    }
}
