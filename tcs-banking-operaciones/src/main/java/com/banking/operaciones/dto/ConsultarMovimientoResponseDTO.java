package com.banking.operaciones.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultarMovimientoResponseDTO extends ResponseDTO {

    private MovimientoDetalleResponseDTO movimiento;
}
