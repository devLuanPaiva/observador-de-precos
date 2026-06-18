package com.luanpaiva.observador_de_precos.shared.responses;

import java.util.List;

public record CollectionResponse<T>(
    Long count,
    List<T> results
) {
    
}
