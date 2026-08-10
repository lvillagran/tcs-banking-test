package com.banking.operaciones.exception;

public class CuentaNoEncontradaException extends RuntimeException {

    public CuentaNoEncontradaException(String numeroCuenta) {
        super("Cuenta no encontrada para el número " + numeroCuenta);
    }
}
