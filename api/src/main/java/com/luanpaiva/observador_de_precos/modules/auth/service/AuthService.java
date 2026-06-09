package com.luanpaiva.observador_de_precos.modules.auth.service;

import com.luanpaiva.observador_de_precos.modules.auth.dto.AuthResponseDTO;
import com.luanpaiva.observador_de_precos.modules.auth.dto.LoginRequestDTO;
import com.luanpaiva.observador_de_precos.modules.auth.dto.RefreshTokenRequestDTO;
import com.luanpaiva.observador_de_precos.modules.auth.dto.RefreshTokenResponseDTO;
import com.luanpaiva.observador_de_precos.modules.auth.dto.RegisterRequestDTO;
import com.luanpaiva.observador_de_precos.modules.users.entity.User;
import com.luanpaiva.observador_de_precos.modules.users.enums.UserRole;
import com.luanpaiva.observador_de_precos.modules.users.repository.UserRepository;
import com.luanpaiva.observador_de_precos.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;

        public void register(RegisterRequestDTO dto) {

                if (userRepository.existsByEmail(dto.email())) {
                        throw new ResponseStatusException(
                                        HttpStatus.CONFLICT,
                                        "Email já cadastrado");
                }

                User user = User.builder()
                                .name(dto.name())
                                .email(dto.email())
                                .role(UserRole.USER)
                                .password(passwordEncoder.encode(dto.password()))
                                .build();

                userRepository.save(user);
        }

        public AuthResponseDTO login(LoginRequestDTO dto) {

                User user = userRepository.findByEmail(dto.email())
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.UNAUTHORIZED,
                                                "Credenciais inválidos"));

                boolean passwordMatches = passwordEncoder.matches(
                                dto.password(),
                                user.getPassword());

                if (!passwordMatches) {
                        throw new ResponseStatusException(
                                        HttpStatus.UNAUTHORIZED,
                                        "Credenciais inválidos");
                }

                String accessToken = jwtService.generateAccessToken(
                                user.getId(),
                                user.getName(),
                                user.getEmail(),
                                user.getRole());

                String refreshToken = jwtService.generateRefreshToken(
                                user.getId(),
                                user.getName(),
                                user.getEmail(),
                                user.getRole());

                return new AuthResponseDTO(
                                accessToken,
                                refreshToken);
        }

        public RefreshTokenResponseDTO refresh(
                        RefreshTokenRequestDTO dto) {

                if (!jwtService.isRefreshToken(
                                dto.refreshToken())) {

                        throw new ResponseStatusException(
                                        HttpStatus.UNAUTHORIZED,
                                        "Refresh token inválido");
                }

                UUID userId = jwtService.extractUserId(
                                dto.refreshToken());

                User user = userRepository.findById(userId)
                                .orElseThrow();

                String accessToken = jwtService.generateAccessToken(
                                user.getId(),
                                user.getName(),
                                user.getEmail(),
                                user.getRole());

                String refreshToken = jwtService.generateRefreshToken(
                                user.getId(),
                                user.getName(),
                                user.getEmail(),
                                user.getRole());

                return new RefreshTokenResponseDTO(
                                accessToken,
                                refreshToken);
        }
}