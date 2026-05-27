package com.luanpaiva.observador_de_precos.shared.exceptions;

import java.time.Instant;

public record ApiExceptionResponse(

                Instant timestamp,

                Integer status,

                String error,

                String message,

                String path) {
}
