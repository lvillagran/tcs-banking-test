package com.banking.operaciones.dto;

import com.banking.operaciones.model.enums.TipoCuenta;

import java.math.BigDecimal;

public record CuentaDetalleResponseDTO(
        Long id,
        String numeroCuenta,
        TipoCuenta tipoCuenta,
        BigDecimal saldoInicial,
        BigDecimal saldoDisponible,
        boolean estado,
        Long clienteId,
        String identificacionCliente
) {
}
