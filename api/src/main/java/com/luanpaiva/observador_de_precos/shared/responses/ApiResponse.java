package com.luanpaiva.observador_de_precos.shared.responses;

public record ApiResponse<T>(
        Boolean success,
        String message,
        T data) {

}
