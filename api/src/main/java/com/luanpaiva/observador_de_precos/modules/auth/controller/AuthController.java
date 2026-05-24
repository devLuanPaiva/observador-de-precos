package com.luanpaiva.observador_de_precos.modules.auth.controller;

import com.luanpaiva.observador_de_precos.modules.auth.dto.AuthResponseDTO;
import com.luanpaiva.observador_de_precos.modules.auth.dto.LoginRequestDTO;
import com.luanpaiva.observador_de_precos.modules.auth.dto.RegisterRequestDTO;
import com.luanpaiva.observador_de_precos.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
            @RequestBody @Valid RegisterRequestDTO dto) {
        authService.register(dto);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(
            @RequestBody @Valid LoginRequestDTO dto) {
        return authService.login(dto);
    }
}
