package com.banking.operaciones.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MovimientosResponseDTO extends ResponseDTO {

    private int total;
    private List<MovimientoDetalleResponseDTO> movimientos;
}
