package com.banking.operaciones.messaging;

import com.banking.operaciones.event.CuentaCreadaEvent;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CuentaEventPublisherTest {

    @Test
    void debePublicarConTopicYNumeroCuentaComoKey() {
        @SuppressWarnings("unchecked")
        KafkaTemplate<String, CuentaCreadaEvent> kafkaTemplate = mock(KafkaTemplate.class);
        CuentaEventPublisher publisher = new CuentaEventPublisher(kafkaTemplate);
        CuentaCreadaEvent event = new CuentaCreadaEvent(
                UUID.randomUUID(), 1, Instant.now(), 25L, 12L,
                "0945678903", "7909950040", "AHO", new BigDecimal("10.00"));

        when(kafkaTemplate.send(KafkaTopics.CUENTAS_CREADAS, event.numeroCuenta(), event))
                .thenReturn(CompletableFuture.completedFuture(null));

        publisher.publicarCuentaCreada(event);

        verify(kafkaTemplate).send(KafkaTopics.CUENTAS_CREADAS, event.numeroCuenta(), event);
    }
}
