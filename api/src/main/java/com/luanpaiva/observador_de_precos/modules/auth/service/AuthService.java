package com.luanpaiva.observador_de_precos.modules.auth.service;

import com.luanpaiva.observador_de_precos.modules.auth.dto.AuthResponseDTO;
import com.luanpaiva.observador_de_precos.modules.auth.dto.LoginRequestDTO;
import com.luanpaiva.observador_de_precos.modules.auth.dto.RegisterRequestDTO;
import com.luanpaiva.observador_de_precos.modules.users.entity.User;
import com.luanpaiva.observador_de_precos.modules.users.repository.UserRepository;
import com.luanpaiva.observador_de_precos.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(RegisterRequestDTO dto) {

        if (userRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        boolean passwordMatches = passwordEncoder.matches(
                dto.password(),
                user.getPassword());

        if (!passwordMatches) {
            throw new RuntimeException("Credenciais inválidas");
        }

        String accessToken = jwtService.generateAccessToken(
            user.getId(),
            user.getName(),
            user.getEmail()
        );

        String refreshToken = jwtService.generateRefreshToken(
            user.getId(),
            user.getName(),
            user.getEmail()
        );

        return new AuthResponseDTO(
                accessToken,
                refreshToken);
    }
}