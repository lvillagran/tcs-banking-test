package com.banking.operaciones.model.enums;

public enum TipoMovimiento {

    DEP("Depósito"),
    RET("Retiro");

    private final String descripcion;

    TipoMovimiento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
