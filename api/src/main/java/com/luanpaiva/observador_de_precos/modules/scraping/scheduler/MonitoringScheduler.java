package com.luanpaiva.observador_de_precos.modules.scraping.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.luanpaiva.observador_de_precos.modules.monitoring.repository.MonitoringRepository;
import com.luanpaiva.observador_de_precos.modules.scraping.service.MonitoringExecutionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonitoringScheduler {
    private final MonitoringRepository monitoringRepository;
    private final MonitoringExecutionService monitoringExecutionService;

    @Scheduled(fixedDelay = 3000000)

    public void executeMonitoring() {
        monitoringRepository.findByActiveTrue()
            .forEach(monitoring -> {
                try {
                    monitoringExecutionService.execute(monitoring.getId());
                } catch (Exception e) {
                    System.out.println("Erro ao executar monitoramento: " + monitoring.getId() + " - " + e.getMessage());
                }
            });
    }
}
