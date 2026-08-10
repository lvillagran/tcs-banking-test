package com.banking.operaciones.dto;

import com.banking.operaciones.model.enums.TipoMovimiento;

import java.math.BigDecimal;
import java.util.Date;

public record ReporteMovimientoResponseDTO(
        Long id,
        Date fechaMovimiento,
        TipoMovimiento tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldoDisponible
) {
}
