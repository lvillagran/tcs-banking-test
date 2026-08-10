package com.banking.operaciones.messaging;

import com.banking.operaciones.event.MovimientoRealizadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MovimientoEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(MovimientoEventPublisher.class);

    private final KafkaTemplate<String, MovimientoRealizadoEvent> kafkaTemplate;

    public MovimientoEventPublisher(KafkaTemplate<String, MovimientoRealizadoEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publicarMovimientoRealizado(MovimientoRealizadoEvent event) {
        kafkaTemplate.send(KafkaTopics.MOVIMIENTOS_REALIZADOS, event.numeroCuenta(), event)
                .whenComplete((resultado, error) -> {
                    if (error == null) {
                        String operacion = "DEP".equals(event.tipoMovimiento()) ? "depósito" : "retiro";
                        log.info("TRANSACCIÓN REALIZADA: evento de {} encolado en Kafka. eventId={}, numeroCuenta={}",
                                operacion, event.eventId(), event.numeroCuenta());
                    } else {
                        log.error("No se pudo registrar el evento de movimiento en Kafka: eventId={}, numeroCuenta={}",
                                event.eventId(), event.numeroCuenta(), error);
                    }
                });
    }
}
