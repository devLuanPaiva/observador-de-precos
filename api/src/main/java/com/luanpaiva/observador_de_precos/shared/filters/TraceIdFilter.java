package com.luanpaiva.observador_de_precos.shared.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;

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

        log.info(
                "Request iniciada: {} {}",
                httpRequest.getMethod(),
                httpRequest.getRequestURI());
        chain.doFilter(request, response);

        MDC.clear();
    }

}
