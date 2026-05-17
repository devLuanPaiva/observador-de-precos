package com.luanpaiva.observador_de_precos.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank @Size(max = 120) String name,

        @NotBlank @Email String email,

        @NotBlank @Size(min = 6, max = 255) String password) {
}
