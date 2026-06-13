package com.luanpaiva.observador_de_precos.modules.users.dto;

import java.util.UUID;

import com.luanpaiva.observador_de_precos.modules.users.enums.UserRole;

public record UserResponseDTO(
                UUID id,
                String name,
                String email,
                UserRole role) {
}
