package com.banking.operaciones.exception;

import com.banking.operaciones.model.enums.TipoCuenta;
import com.banking.operaciones.model.enums.TipoMovimiento;
import tools.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.Instant;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ClienteNoEncontradoException.class)
    ResponseEntity<ApiErrorResponse> clienteNoEncontrado(ClienteNoEncontradoException exception) {
        return response(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(CuentaNoEncontradaException.class)
    ResponseEntity<ApiErrorResponse> cuentaNoEncontrada(CuentaNoEncontradaException exception) {
        return response(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(ClienteInactivoException.class)
    ResponseEntity<ApiErrorResponse> clienteInactivo(ClienteInactivoException exception) {
        return response(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(SolicitudInvalidaException.class)
    ResponseEntity<ApiErrorResponse> solicitudInvalida(SolicitudInvalidaException exception) {
        return response(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(SaldoNoDisponibleException.class)
    ResponseEntity<ApiErrorResponse> saldoNoDisponible(SaldoNoDisponibleException exception) {
        return response(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(BackofficeNoDisponibleException.class)
    ResponseEntity<ApiErrorResponse> backofficeNoDisponible(BackofficeNoDisponibleException exception) {
        return response(HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiErrorResponse> mensajeNoLegible(HttpMessageNotReadableException exception) {
        Throwable cause = exception.getCause();
        while (cause != null) {
            if (cause instanceof InvalidFormatException invalidFormat
                    && invalidFormat.getTargetType() == TipoCuenta.class) {
                return response(HttpStatus.BAD_REQUEST,
                        "Tipo de cuenta inválido. Valores permitidos: AHO, CTE");
            }
            if (cause instanceof InvalidFormatException invalidFormat
                    && invalidFormat.getTargetType() == TipoMovimiento.class) {
                return response(HttpStatus.BAD_REQUEST,
                        "Tipo de movimiento inválido. Valores permitidos: DEP, RET");
            }
            cause = cause.getCause();
        }
        return response(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no es válido.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorResponse> validacion(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Solicitud inválida.");
        return response(HttpStatus.BAD_REQUEST, message);
    }

    private ResponseEntity<ApiErrorResponse> response(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiErrorResponse(message, Instant.now()));
    }
}
