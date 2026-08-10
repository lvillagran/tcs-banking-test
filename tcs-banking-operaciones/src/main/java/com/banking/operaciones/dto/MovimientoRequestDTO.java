package com.banking.operaciones.dto;

import com.banking.operaciones.model.enums.TipoMovimiento;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class MovimientoRequestDTO {

    @NotNull(message = "El tipo de movimiento es obligatorio.")
    private TipoMovimiento tipoMovimiento;
    private BigDecimal valor;
    private String numeroCuenta;

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
}
