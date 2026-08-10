package com.banking.notificaciones.messaging;

import com.banking.notificaciones.event.MovimientoRealizadoEvent;
import com.banking.notificaciones.service.NotificacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MovimientoRealizadoEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(MovimientoRealizadoEventConsumer.class);
    private final NotificacionService notificacionService;

    public MovimientoRealizadoEventConsumer(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @KafkaListener(topics = KafkaTopics.MOVIMIENTOS_REALIZADOS, groupId = "tcs-banking-notificaciones")
    public void consumir(MovimientoRealizadoEvent event) {
        log.info("TRANSACCIÓN NOTIFICADA: se realizó un {}. Evento recibido desde Kafka. eventId={}, numeroCuenta={}",
                "DEP".equals(event.tipoMovimiento()) ? "depósito" : "retiro",
                event.eventId(), event.numeroCuenta());
        notificacionService.procesarMovimientoRealizado(event);
    }
}
