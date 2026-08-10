package com.banking.operaciones.exception;

public class BackofficeNoDisponibleException extends RuntimeException {
    public BackofficeNoDisponibleException(String message) { super(message); }
    public BackofficeNoDisponibleException(String message, Throwable cause) { super(message, cause); }
}
