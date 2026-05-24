package com.luanpaiva.observador_de_precos.modules.users.dto;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email) {
}
