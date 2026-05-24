package com.luanpaiva.observador_de_precos.security;

import com.luanpaiva.observador_de_precos.modules.users.entity.User;
import com.luanpaiva.observador_de_precos.modules.users.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String token = authHeader.substring(7);

            UUID userId = jwtService.extractUserId(token);

            User user = userRepository.findById(userId)
                    .orElse(null);

            if (user != null) {

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities());

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }

        } catch (Exception ignored) {
            throw new BadCredentialsException("Token inválido");
        }

        filterChain.doFilter(request, response);
    }
}