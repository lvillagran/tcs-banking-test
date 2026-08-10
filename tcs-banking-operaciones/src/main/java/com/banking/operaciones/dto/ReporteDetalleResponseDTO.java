package com.banking.operaciones.dto;

import java.util.List;

public record ReporteDetalleResponseDTO(
        String fechaDesde,
        String fechaHasta,
        ReporteClienteResponseDTO cliente,
        List<ReporteCuentaResponseDTO> cuentas
) {
}
