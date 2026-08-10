package com.banking.operaciones.serviceImpl;

import com.banking.operaciones.client.ClienteClient;
import com.banking.operaciones.client.ClienteResponseDTO;
import com.banking.operaciones.dto.ReporteClienteResponseDTO;
import com.banking.operaciones.dto.ReporteCuentaResponseDTO;
import com.banking.operaciones.dto.ReporteDetalleResponseDTO;
import com.banking.operaciones.dto.ReporteMovimientoResponseDTO;
import com.banking.operaciones.model.BanCuenta;
import com.banking.operaciones.repository.BanCuentaRepository;
import com.banking.operaciones.repository.BanMovimientoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class ReporteService {

    private final BanCuentaRepository cuentaRepository;
    private final BanMovimientoRepository movimientoRepository;
    private final ClienteClient clienteClient;

    public ReporteService(
            BanCuentaRepository cuentaRepository,
            BanMovimientoRepository movimientoRepository,
            ClienteClient clienteClient) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
        this.clienteClient = clienteClient;
    }

    public ReporteDetalleResponseDTO generar(
            String identificacionCliente,
            LocalDate fechaDesde,
            LocalDate fechaHasta) {
        ClienteResponseDTO cliente = clienteClient.buscarPorIdentificacion(
                identificacionCliente);
        ZoneId zonaHoraria = ZoneId.systemDefault();
        Date desde = Date.from(fechaDesde.atStartOfDay(zonaHoraria).toInstant());
        Date hastaExclusiva = Date.from(fechaHasta.plusDays(1).atStartOfDay(zonaHoraria).toInstant());

        List<ReporteCuentaResponseDTO> cuentas = cuentaRepository
                .findByIdentificacionClienteOrderByNumeroCuentaAsc(identificacionCliente)
                .stream()
                .map(cuenta -> mapearCuenta(cuenta, desde, hastaExclusiva))
                .toList();

        return new ReporteDetalleResponseDTO(
                fechaDesde.toString(),
                fechaHasta.toString(),
                new ReporteClienteResponseDTO(cliente.id(), cliente.identificacion(), cliente.nombre()),
                cuentas);
    }

    private ReporteCuentaResponseDTO mapearCuenta(
            BanCuenta cuenta,
            Date fechaDesde,
            Date fechaHastaExclusiva) {
        List<ReporteMovimientoResponseDTO> movimientos = movimientoRepository
                .findByCuentaAndFechaMovimientoGreaterThanEqualAndFechaMovimientoLessThanOrderByFechaMovimientoAsc(
                        cuenta,
                        fechaDesde,
                        fechaHastaExclusiva)
                .stream()
                .map(movimiento -> new ReporteMovimientoResponseDTO(
                        movimiento.getId(),
                        movimiento.getFechaMovimiento(),
                        movimiento.getTipoMovimiento(),
                        movimiento.getValor(),
                        movimiento.getSaldo()))
                .toList();

        return new ReporteCuentaResponseDTO(
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldoInicial(),
                cuenta.getSaldoDisponible(),
                cuenta.getEstado(),
                movimientos);
    }
}
