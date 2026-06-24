package com.luanpaiva.observador_de_precos.modules.scraping.strategy;

import com.luanpaiva.observador_de_precos.modules.scraping.dto.ScrapingResultDTO;

public interface ScraperStrategy {

    ScrapingResultDTO scrape(String url);
}
