package com.banking.notificaciones.messaging;

import com.banking.notificaciones.event.CuentaCreadaEvent;
import com.banking.notificaciones.service.NotificacionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CuentaCreadaEventConsumerTest {

    @Test
    void debeDelegarLaNotificacionAlServicio() {
        NotificacionService notificacionService = mock(NotificacionService.class);
        CuentaCreadaEventConsumer consumer = new CuentaCreadaEventConsumer(notificacionService);
        CuentaCreadaEvent event = new CuentaCreadaEvent(
                UUID.randomUUID(), 1, Instant.now(), 25L, 12L,
                "0945678903", "7909950040", "AHO", new BigDecimal("10.00"));

        consumer.consumir(event);

        verify(notificacionService).procesarCuentaCreada(event);
    }
}
