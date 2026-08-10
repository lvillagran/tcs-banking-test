package com.banking.notificaciones.messaging;

import com.banking.notificaciones.event.MovimientoRealizadoEvent;
import com.banking.notificaciones.service.NotificacionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MovimientoRealizadoEventConsumerTest {

    @Test
    void debeDelegarElMovimientoAlServicio() {
        NotificacionService service = mock(NotificacionService.class);
        MovimientoRealizadoEventConsumer consumer = new MovimientoRealizadoEventConsumer(service);
        MovimientoRealizadoEvent event = new MovimientoRealizadoEvent(
                UUID.randomUUID(), 1, Instant.now(), 1L, 2L, 3L,
                "7909950040", "DEP", new BigDecimal("10.00"), new BigDecimal("20.00"));

        consumer.consumir(event);

        verify(service).procesarMovimientoRealizado(event);
    }
}
