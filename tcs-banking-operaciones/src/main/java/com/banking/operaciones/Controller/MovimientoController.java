package com.banking.operaciones.Controller;

import com.banking.operaciones.dto.CrearMovimientoResponseDTO;
import com.banking.operaciones.dto.ConsultarMovimientoResponseDTO;
import com.banking.operaciones.dto.MovimientoDetalleResponseDTO;
import com.banking.operaciones.dto.MovimientosResponseDTO;
import com.banking.operaciones.dto.MovimientoRequestDTO;
import com.banking.operaciones.dto.MovimientoResponseDTO;
import com.banking.operaciones.model.BanMovimientos;
import com.banking.operaciones.mapper.MovimientoMapper;
import com.banking.operaciones.serviceImpl.BanMovimientoServiceImpl;
import com.banking.operaciones.serviceImpl.MovimientoCreadoResultado;
import com.banking.operaciones.event.MovimientoRealizadoEvent;
import com.banking.operaciones.messaging.MovimientoEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/operaciones/movimientos")
public class MovimientoController {

    private final BanMovimientoServiceImpl banMovimientoService;
    private final MovimientoEventPublisher movimientoEventPublisher;

    public MovimientoController(
            BanMovimientoServiceImpl banMovimientoService,
            MovimientoEventPublisher movimientoEventPublisher) {
        this.banMovimientoService = banMovimientoService;
        this.movimientoEventPublisher = movimientoEventPublisher;
    }

    /** Crear movimiento */
    @PostMapping("/transaccion")
    public ResponseEntity<CrearMovimientoResponseDTO> crearMovimiento(@Valid @RequestBody MovimientoRequestDTO request) {
        CrearMovimientoResponseDTO response = new CrearMovimientoResponseDTO();

        if (request.getNumeroCuenta() == null || request.getNumeroCuenta().isBlank()) {
            response.setMensaje("El número de cuenta es obligatorio.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        MovimientoCreadoResultado resultado = banMovimientoService.procesarMovimiento(request);
        MovimientoRealizadoEvent event = new MovimientoRealizadoEvent(
                UUID.randomUUID(), 1, Instant.now(),
                resultado.movimiento().getId(), resultado.cuenta().getId(),
                resultado.cuenta().getClienteId(), resultado.cuenta().getNumeroCuenta(),
                resultado.movimiento().getTipoMovimiento().name(), resultado.movimiento().getValor(),
                resultado.cuenta().getSaldoDisponible());
        movimientoEventPublisher.publicarMovimientoRealizado(event);
        response.setMovimiento(MovimientoMapper.toDetalleResponse(
                resultado.movimiento(),
                resultado.cuenta(),
                resultado.movimiento().getTipoMovimiento(),
                resultado.saldoAnterior()));
        response.setMensaje(request.getTipoMovimiento().getDescripcion() + " creado correctamente.");
        response.setFechaEjecucion(new Date());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /** Listar todos los movimientos */
    @GetMapping("/listar")
    public ResponseEntity<MovimientosResponseDTO> listarMovimientos() {
        List<BanMovimientos> movimientos = banMovimientoService.findAll();
        if (movimientos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<MovimientoDetalleResponseDTO> detalles = movimientos.stream()
                .map(MovimientoMapper::toDetalleResponse)
                .toList();
        MovimientosResponseDTO response = new MovimientosResponseDTO();
        response.setFechaEjecucion(new Date());
        response.setMensaje("Movimientos consultados correctamente.");
        response.setTotal(detalles.size());
        response.setMovimientos(detalles);
        return ResponseEntity.ok(response);
    }

    /** Consultar un movimiento por su identificador */
    @GetMapping("/consultar/{idMovimiento}")
    public ResponseEntity<ConsultarMovimientoResponseDTO> consultarMovimiento(
            @PathVariable Long idMovimiento) {
        ConsultarMovimientoResponseDTO response = new ConsultarMovimientoResponseDTO();
        response.setFechaEjecucion(new Date());

        return banMovimientoService.findById(idMovimiento)
                .map(movimiento -> {
                    response.setMensaje("Movimiento consultado correctamente.");
                    response.setMovimiento(MovimientoMapper.toDetalleResponse(movimiento));
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.setMensaje("Movimiento no encontrado.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    /** Eliminar un movimiento por su identificador */
    @DeleteMapping("/eliminar/{idMovimiento}")
    public ResponseEntity<MovimientoResponseDTO> eliminarMovimiento(@PathVariable Long idMovimiento) {
        MovimientoResponseDTO response = new MovimientoResponseDTO();

        Optional<BanMovimientos> movimientoOpt = banMovimientoService.findById(idMovimiento);
        if (movimientoOpt.isEmpty()) {
            response.setMensaje("Movimiento no encontrado.");
            response.setFechaEjecucion(new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        banMovimientoService.eliminarMovimiento(movimientoOpt.get());

        response.setMensaje("Movimiento eliminado correctamente.");
        response.setFechaEjecucion(new Date());
        return ResponseEntity.ok(response);
    }

}
