package com.banking.operaciones.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record MovimientoRealizadoEvent(
        UUID eventId,
        Integer eventVersion,
        Instant occurredAt,
        Long movimientoId,
        Long cuentaId,
        Long clienteId,
        String numeroCuenta,
        String tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldoDisponible
) {
}
