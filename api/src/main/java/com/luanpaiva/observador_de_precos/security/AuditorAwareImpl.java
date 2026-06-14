package com.luanpaiva.observador_de_precos.security;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<UUID> {

    private final SecurityContextHelper security;

    @Override
    public Optional<UUID> getCurrentAuditor() {
        try {

            return Optional.of(
                    security.getCurrentUserId());

        } catch (Exception ex) {

            return Optional.empty();
        }
    }
}
