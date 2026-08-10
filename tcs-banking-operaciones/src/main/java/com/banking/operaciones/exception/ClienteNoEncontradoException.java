package com.banking.operaciones.exception;

public class ClienteNoEncontradoException extends RuntimeException {
    public ClienteNoEncontradoException(String identificacion) {
        super("Cliente no encontrado para la identificación " + identificacion);
    }
}
