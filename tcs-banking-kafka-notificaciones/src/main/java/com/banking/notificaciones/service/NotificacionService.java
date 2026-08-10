package com.banking.notificaciones.service;

import com.banking.notificaciones.event.CuentaCreadaEvent;
import com.banking.notificaciones.event.MovimientoRealizadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    public void procesarCuentaCreada(CuentaCreadaEvent event) {
        log.info(
                "Notificación generada: Bienvenido a TCS Bank. Cuenta {} creada correctamente. Tipo de cuenta: {}. Cliente: {}",
                event.numeroCuenta(), event.tipoCuenta(), event.clienteId()
        );
    }

    public void procesarMovimientoRealizado(MovimientoRealizadoEvent event) {
        String operacion = "DEP".equals(event.tipoMovimiento()) ? "Depósito" : "Retiro";
        log.info("Notificación generada: {} por {} realizado correctamente en la cuenta {}",
                operacion, event.valor(), event.numeroCuenta());
    }
}
