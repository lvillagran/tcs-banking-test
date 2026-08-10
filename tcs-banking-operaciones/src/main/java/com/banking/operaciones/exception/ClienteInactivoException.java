package com.banking.operaciones.exception;

public class ClienteInactivoException extends RuntimeException {
    public ClienteInactivoException(String identificacion) {
        super("El cliente con identificación " + identificacion + " se encuentra inactivo");
    }
}
