package com.banking.operaciones.dto;

import com.banking.operaciones.model.enums.TipoCuenta;

import java.math.BigDecimal;
import java.util.List;

public record ReporteCuentaResponseDTO(
        String numeroCuenta,
        TipoCuenta tipoCuenta,
        BigDecimal saldoInicial,
        BigDecimal saldoDisponible,
        boolean estado,
        List<ReporteMovimientoResponseDTO> movimientos
) {
}
