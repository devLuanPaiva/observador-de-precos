package com.luanpaiva.observador_de_precos.modules.scraping.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luanpaiva.observador_de_precos.modules.scraping.service.MonitoringExecutionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/scraping")
@RequiredArgsConstructor
public class ScrapingController {

    private final MonitoringExecutionService service;

    @PostMapping("/execute/{monitoringId}")
    public void execute(
            @PathVariable UUID monitoringId) {

        service.execute(
                monitoringId);
    }
}
