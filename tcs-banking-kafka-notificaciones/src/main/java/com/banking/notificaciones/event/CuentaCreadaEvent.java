package com.banking.notificaciones.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Copia compatible del contrato JSON producido por Operaciones.
 * En producción puede evolucionar a un contrato gobernado con Schema Registry.
 */
public record CuentaCreadaEvent(
        UUID eventId,
        Integer eventVersion,
        Instant occurredAt,
        Long cuentaId,
        Long clienteId,
        String identificacionCliente,
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoInicial
) {
}
