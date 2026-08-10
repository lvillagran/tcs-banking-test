package com.banking.operaciones.serviceImpl;

import com.banking.operaciones.model.BanCuenta;
import com.banking.operaciones.model.BanMovimientos;

import java.math.BigDecimal;

public record MovimientoCreadoResultado(
        BanMovimientos movimiento,
        BanCuenta cuenta,
        BigDecimal saldoAnterior
) {
}
