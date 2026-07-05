package com.luanpaiva.observador_de_precos.modules.scraping.service.impl;

import org.springframework.stereotype.Service;

import com.luanpaiva.observador_de_precos.modules.scraping.dto.ScrapingResultDTO;
import com.luanpaiva.observador_de_precos.modules.scraping.factory.ScraperFactory;
import com.luanpaiva.observador_de_precos.modules.scraping.service.ScrapingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrapingServiceImpl implements ScrapingService {

    private final ScraperFactory factory;

    @Override
    public ScrapingResultDTO scrape(String url) {

        return factory
                .getStrategy(url)
                .scrape(url);
    }
}