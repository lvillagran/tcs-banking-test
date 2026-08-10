package com.banking.operaciones.model.enums;

public enum TipoCuenta {

    AHO("Cuenta de Ahorro"),
    CTE("Cuenta Corriente");

    private final String descripcion;

    TipoCuenta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
