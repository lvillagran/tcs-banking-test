package com.banking.operaciones.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReporteResponseDTO extends ResponseDTO {

    private ReporteDetalleResponseDTO reporte;
}
