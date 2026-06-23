package com.luanpaiva.observador_de_precos.modules.monitoring.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.luanpaiva.observador_de_precos.modules.monitoring.dto.CreateMonitoringRequestDTO;
import com.luanpaiva.observador_de_precos.modules.monitoring.dto.MonitoringFilterDTO;
import com.luanpaiva.observador_de_precos.modules.monitoring.dto.MonitoringResponseDTO;
import com.luanpaiva.observador_de_precos.modules.monitoring.entity.Monitoring;
import com.luanpaiva.observador_de_precos.modules.monitoring.mapper.MonitoringMapper;
import com.luanpaiva.observador_de_precos.modules.monitoring.repository.MonitoringRepository;
import com.luanpaiva.observador_de_precos.modules.monitoring.service.MonitoringService;
import com.luanpaiva.observador_de_precos.modules.products.entity.Product;
import com.luanpaiva.observador_de_precos.modules.products.repository.ProductRepository;
import com.luanpaiva.observador_de_precos.modules.users.entity.User;
import com.luanpaiva.observador_de_precos.security.SecurityContextHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonitoringServiceIml implements MonitoringService {
    private final MonitoringRepository monitoringRepository;
    private final MonitoringMapper monitoringMapper;
    private final SecurityContextHelper securityContextHelper;
    private final ProductRepository productRepository;

    @Override
    public MonitoringResponseDTO createMonitoring(CreateMonitoringRequestDTO dto) {
        User currentUser = securityContextHelper.getCurrentUser();
        Product product = productRepository
                .findByIdAndUserId(
                        dto.productId(),
                        currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado"));

        Monitoring monitoring = Monitoring.builder()
                .user(currentUser)
                .product(product)
                .targetPrice(dto.targetPrice())
                .notifyPromotion(dto.notifyPromotion())
                .notifyStock(null)
                .build();

        monitoringRepository.save(monitoring);
        return monitoringMapper.toResponse(monitoring);
    }

    public List<MonitoringResponseDTO> findAll(MonitoringFilterDTO filter) {
        UUID userId = securityContextHelper.getCurrentUserId();

        return monitoringRepository
                .findAllByUserId(userId)
                .stream()
                .map(monitoringMapper::toResponse)
                .toList();
    }

    public MonitoringResponseDTO findById(UUID id) {
        UUID userId = securityContextHelper.getCurrentUserId();

        Monitoring monitoring = monitoringRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado"));

        return monitoringMapper.toResponse(monitoring);
    }

    public MonitoringResponseDTO updateStatus(UUID id, Boolean active) {
        UUID userId = securityContextHelper.getCurrentUserId();

        Monitoring monitoring = monitoringRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado"));

        monitoring.setActive(active);
        monitoringRepository.save(monitoring);

        return monitoringMapper.toResponse(monitoring);
    }

    public void delete(UUID id) {
        UUID userId = securityContextHelper.getCurrentUserId();

        Monitoring monitoring = monitoringRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado"));

        monitoringRepository.delete(monitoring);
    }

}
