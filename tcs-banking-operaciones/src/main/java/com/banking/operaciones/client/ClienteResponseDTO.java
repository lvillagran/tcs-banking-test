package com.banking.operaciones.client;

public record ClienteResponseDTO(
        Long id,
        String identificacion,
        String nombre,
        Boolean estado
) {
}
