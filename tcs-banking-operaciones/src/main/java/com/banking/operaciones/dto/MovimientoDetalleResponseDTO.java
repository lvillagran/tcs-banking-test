package com.banking.operaciones.dto;

import com.banking.operaciones.model.enums.TipoCuenta;
import com.banking.operaciones.model.enums.TipoMovimiento;

import java.math.BigDecimal;
import java.util.Date;

public record MovimientoDetalleResponseDTO(
        Long id,
        Date fechaMovimiento,
        String numeroCuenta,
        TipoCuenta tipoCuenta,
        TipoMovimiento tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldoAnterior,
        BigDecimal saldoDisponible
) {
}
