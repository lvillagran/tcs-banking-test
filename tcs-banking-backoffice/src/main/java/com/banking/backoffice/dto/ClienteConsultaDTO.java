package com.banking.backoffice.dto;

import com.banking.backoffice.model.TipoIdentificacion;

public record ClienteConsultaDTO(
        Long id,
        String identificacion,
        TipoIdentificacion tipoIdentificacion,
        String nombre,
        Boolean estado
) {
}
