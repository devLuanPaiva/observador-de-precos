package com.luanpaiva.observador_de_precos.modules.auth.dto;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken) {
}