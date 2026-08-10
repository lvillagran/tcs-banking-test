package com.banking.operaciones.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Contrato JSON publicado cuando una cuenta se crea correctamente.
 *
 * <p>Se limita a tipos simples para no exponer entidades JPA en Kafka.</p>
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
