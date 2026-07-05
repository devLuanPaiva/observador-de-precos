package com.luanpaiva.observador_de_precos.modules.scraping.service;

import com.luanpaiva.observador_de_precos.modules.scraping.dto.ScrapingResultDTO;

public interface ScrapingService {

    ScrapingResultDTO scrape(String url);
}
