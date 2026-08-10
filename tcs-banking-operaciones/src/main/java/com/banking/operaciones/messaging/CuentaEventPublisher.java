package com.banking.operaciones.messaging;

import com.banking.operaciones.event.CuentaCreadaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CuentaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(CuentaEventPublisher.class);

    private final KafkaTemplate<String, CuentaCreadaEvent> kafkaTemplate;

    public CuentaEventPublisher(KafkaTemplate<String, CuentaCreadaEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publicarCuentaCreada(CuentaCreadaEvent event) {
        // La cuenta es la key para conservar el orden de sus eventos en una partición.
        kafkaTemplate.send(KafkaTopics.CUENTAS_CREADAS, event.numeroCuenta(), event)
                .whenComplete((resultado, error) -> {
                    if (error == null) {
                        log.info("Evento de creación de cuenta registrado en Kafka: eventId={}, numeroCuenta={}",
                                event.eventId(), event.numeroCuenta());
                    } else {
                        log.error("No se pudo enviar CuentaCreadaEvent: eventId={}, numeroCuenta={}",
                                event.eventId(), event.numeroCuenta(), error);
                    }
                });
    }
}
