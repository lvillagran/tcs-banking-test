package com.banking.operaciones.mapper;

import com.banking.operaciones.dto.MovimientoDetalleResponseDTO;
import com.banking.operaciones.model.BanCuenta;
import com.banking.operaciones.model.BanMovimientos;
import com.banking.operaciones.model.enums.TipoMovimiento;

import java.math.BigDecimal;

public final class MovimientoMapper {

    private MovimientoMapper() {
    }

    public static MovimientoDetalleResponseDTO toDetalleResponse(
            BanMovimientos movimiento,
            BanCuenta cuenta,
            TipoMovimiento tipoMovimiento,
            BigDecimal saldoAnterior) {
        return new MovimientoDetalleResponseDTO(
                movimiento.getId(),
                movimiento.getFechaMovimiento(),
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                tipoMovimiento,
                movimiento.getValor(),
                saldoAnterior,
                movimiento.getSaldo());
    }

    public static MovimientoDetalleResponseDTO toDetalleResponse(BanMovimientos movimiento) {
        TipoMovimiento tipoMovimiento = movimiento.getTipoMovimiento();
        BigDecimal saldoAnterior = obtenerSaldoAnterior(
                tipoMovimiento, movimiento.getSaldo(), movimiento.getValor());

        return toDetalleResponse(
                movimiento,
                movimiento.getCuenta(),
                tipoMovimiento,
                saldoAnterior);
    }

    private static BigDecimal obtenerSaldoAnterior(
            TipoMovimiento tipoMovimiento,
            BigDecimal saldoDisponible,
            BigDecimal valor) {
        if (saldoDisponible == null || valor == null) {
            return null;
        }
        if (tipoMovimiento == TipoMovimiento.DEP) {
            return saldoDisponible.subtract(valor);
        }
        if (tipoMovimiento == TipoMovimiento.RET) {
            return saldoDisponible.add(valor);
        }
        return null;
    }
}
