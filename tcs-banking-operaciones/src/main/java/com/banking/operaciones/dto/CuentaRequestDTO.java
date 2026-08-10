package com.banking.operaciones.dto;

import com.banking.operaciones.model.enums.TipoCuenta;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CuentaRequestDTO extends RequestDTO {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "El tipo de cuenta es obligatorio.")
    private TipoCuenta tipoCuenta;
    private BigDecimal saldoInicial;
    private boolean estado;
    private String identificacionCliente;

    // Getters y Setters

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getIdentificacionCliente() {
        return identificacionCliente;
    }

    public void setIdentificacionCliente(String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }
}
