package com.luanpaiva.observador_de_precos.shared.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class TraceIdFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);

        String userId = "anonymous";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null &&
                auth.isAuthenticated() &&
                !(auth instanceof AnonymousAuthenticationToken)) {
            userId = auth.getName();
        }

        MDC.put("userId", userId);

        log.info(
                "Request iniciada: {} {}",
                httpRequest.getMethod(),
                httpRequest.getRequestURI());

        try {
            chain.doFilter(request, response);

        } finally {

            MDC.clear();
        }

    }

}
