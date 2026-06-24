package com.luanpaiva.observador_de_precos.modules.scraping.strategy;

import org.springframework.stereotype.Component;

import com.luanpaiva.observador_de_precos.modules.scraping.dto.ScrapingResultDTO;

@Component
public class GenericScraper implements ScraperStrategy {

    @Override
    public boolean supports(String url) {

        return true;
    }

    @Override
    public ScrapingResultDTO scrape(String url) {

        throw new IllegalArgumentException(
                "Loja não suportada");
    }
}