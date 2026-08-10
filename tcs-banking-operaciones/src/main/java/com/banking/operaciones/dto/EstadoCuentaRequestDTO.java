package com.banking.operaciones.dto;

import jakarta.validation.constraints.NotBlank;

public record EstadoCuentaRequestDTO(
        @NotBlank(message = "La identificación del cliente es obligatoria.")
        String identificacionCliente,
        @NotBlank(message = "La fecha inicial es obligatoria.")
        String fechaInicio,
        @NotBlank(message = "La fecha final es obligatoria.")
        String fechaFin
) {
}
