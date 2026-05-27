package com.luanpaiva.observador_de_precos.shared.exceptions;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiExceptionResponse> handleException(
                        Exception ex,
                        HttpServletRequest request) {

                ApiExceptionResponse response = new ApiExceptionResponse(
                                Instant.now(),
                                500,
                                "Internal Server Error",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity
                                .status(500)
                                .body(response);
        }
}
