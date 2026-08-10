package com.banking.operaciones.mapper;

import com.banking.operaciones.dto.CuentaDetalleResponseDTO;
import com.banking.operaciones.model.BanCuenta;

public final class CuentaResponseMapper {

    private CuentaResponseMapper() {
    }

    public static CuentaDetalleResponseDTO toDetalle(BanCuenta cuenta) {
        return new CuentaDetalleResponseDTO(
                cuenta.getId(),
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldoInicial(),
                cuenta.getSaldoDisponible(),
                cuenta.getEstado(),
                cuenta.getClienteId(),
                cuenta.getIdentificacionCliente());
    }
}
