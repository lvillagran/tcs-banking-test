package com.banking.backoffice.dto;

import com.banking.backoffice.model.TipoIdentificacion;

public record ClienteDetalleResponseDTO(
        Long id,
        String nombre,
        String genero,
        String edad,
        String identificacion,
        TipoIdentificacion tipoIdentificacion,
        String direccion,
        String telefono,
        Boolean estado
) {
}
