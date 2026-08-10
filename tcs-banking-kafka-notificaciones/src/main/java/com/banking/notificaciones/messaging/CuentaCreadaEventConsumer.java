package com.banking.notificaciones.messaging;

import com.banking.notificaciones.event.CuentaCreadaEvent;
import com.banking.notificaciones.service.NotificacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CuentaCreadaEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(CuentaCreadaEventConsumer.class);

    private final NotificacionService notificacionService;

    public CuentaCreadaEventConsumer(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @KafkaListener(
            topics = KafkaTopics.CUENTAS_CREADAS,
            groupId = "tcs-banking-notificaciones"
    )
    public void consumir(CuentaCreadaEvent event) {
        log.info("Evento CuentaCreadaEvent recibido: eventId={}, numeroCuenta={}",
                event.eventId(), event.numeroCuenta());
        notificacionService.procesarCuentaCreada(event);
    }
}
