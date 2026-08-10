package com.banking.operaciones.Controller;

import com.banking.operaciones.dto.EstadoCuentaRequestDTO;
import com.banking.operaciones.dto.ReporteResponseDTO;
import com.banking.operaciones.exception.SolicitudInvalidaException;
import com.banking.operaciones.serviceImpl.ReporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/operaciones/reportes")
public class ReporteController {

    private static final DateTimeFormatter FORMATO_BODY = DateTimeFormatter
            .ofPattern("dd-MM-uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @PostMapping("/estado-cuenta")
    public ResponseEntity<ReporteResponseDTO> obtenerEstadoCuenta(
            @Valid @RequestBody EstadoCuentaRequestDTO request) {
        try {
            LocalDate fechaDesde = LocalDate.parse(request.fechaInicio().trim(), FORMATO_BODY);
            LocalDate fechaHasta = LocalDate.parse(request.fechaFin().trim(), FORMATO_BODY);
            if (fechaDesde.isAfter(fechaHasta)) {
                throw new SolicitudInvalidaException(
                        "La fecha inicial no puede ser posterior a la fecha final.");
            }
            return generarResponse(
                    request.identificacionCliente().trim(), fechaDesde, fechaHasta);
        } catch (DateTimeParseException exception) {
            throw new SolicitudInvalidaException(
                    "Fechas inválidas. Formato esperado: dd-MM-yyyy");
        }
    }

    private ResponseEntity<ReporteResponseDTO> generarResponse(
            String identificacionCliente,
            LocalDate fechaDesde,
            LocalDate fechaHasta) {
        ReporteResponseDTO response = new ReporteResponseDTO();
        response.setFechaEjecucion(new Date());
        response.setMensaje("Reporte generado correctamente.");
        response.setReporte(reporteService.generar(
                identificacionCliente.trim(), fechaDesde, fechaHasta));
        return ResponseEntity.ok(response);
    }
}
