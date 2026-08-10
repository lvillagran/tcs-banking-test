package com.banking.operaciones;

import com.banking.operaciones.client.ClienteClient;
import com.banking.operaciones.client.ClienteResponseDTO;
import com.banking.operaciones.dto.ReporteDetalleResponseDTO;
import com.banking.operaciones.model.BanCuenta;
import com.banking.operaciones.model.BanMovimientos;
import com.banking.operaciones.model.enums.TipoCuenta;
import com.banking.operaciones.model.enums.TipoMovimiento;
import com.banking.operaciones.repository.BanCuentaRepository;
import com.banking.operaciones.repository.BanMovimientoRepository;
import com.banking.operaciones.serviceImpl.ReporteService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReporteServiceTest {

    @Test
    void debeGenerarElReporteConTodasLasCuentasDelCliente() {
        BanCuentaRepository cuentaRepository = mock(BanCuentaRepository.class);
        BanMovimientoRepository movimientoRepository = mock(BanMovimientoRepository.class);
        ClienteClient clienteClient = mock(ClienteClient.class);
        ReporteService service = new ReporteService(
                cuentaRepository, movimientoRepository, clienteClient);

        String identificacion = "0945678901";
        BanCuenta ahorro = cuenta("1000000001", TipoCuenta.AHO, "100.00");
        BanCuenta corriente = cuenta("2000000002", TipoCuenta.CTE, "250.00");
        BanMovimientos deposito = movimiento(ahorro, TipoMovimiento.DEP, "50.00", "100.00");

        when(clienteClient.buscarPorIdentificacion(identificacion))
                .thenReturn(new ClienteResponseDTO(12L, identificacion, "CLIENTE PRUEBA", true));
        when(cuentaRepository.findByIdentificacionClienteOrderByNumeroCuentaAsc(identificacion))
                .thenReturn(List.of(ahorro, corriente));
        when(movimientoRepository
                .findByCuentaAndFechaMovimientoGreaterThanEqualAndFechaMovimientoLessThanOrderByFechaMovimientoAsc(
                        eq(ahorro), any(), any()))
                .thenReturn(List.of(deposito));
        when(movimientoRepository
                .findByCuentaAndFechaMovimientoGreaterThanEqualAndFechaMovimientoLessThanOrderByFechaMovimientoAsc(
                        eq(corriente), any(), any()))
                .thenReturn(List.of());

        ReporteDetalleResponseDTO reporte = service.generar(
                identificacion, LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 30));

        assertThat(reporte.cliente().identificacion()).isEqualTo(identificacion);
        assertThat(reporte.cuentas()).hasSize(2);
        assertThat(reporte.cuentas().getFirst().numeroCuenta()).isEqualTo("1000000001");
        assertThat(reporte.cuentas().getFirst().movimientos()).hasSize(1);
        assertThat(reporte.cuentas().get(1).numeroCuenta()).isEqualTo("2000000002");
        verify(clienteClient).buscarPorIdentificacion(identificacion);
        verify(cuentaRepository).findByIdentificacionClienteOrderByNumeroCuentaAsc(identificacion);
    }

    private BanCuenta cuenta(String numero, TipoCuenta tipo, String saldo) {
        BanCuenta cuenta = new BanCuenta();
        cuenta.setNumeroCuenta(numero);
        cuenta.setTipoCuenta(tipo);
        cuenta.setSaldoInicial(new BigDecimal(saldo));
        cuenta.setSaldoDisponible(new BigDecimal(saldo));
        cuenta.setEstado(true);
        cuenta.setIdentificacionCliente("0945678901");
        cuenta.setClienteId(12L);
        return cuenta;
    }

    private BanMovimientos movimiento(
            BanCuenta cuenta, TipoMovimiento tipo, String valor, String saldo) {
        BanMovimientos movimiento = new BanMovimientos();
        movimiento.setId(1L);
        movimiento.setCuenta(cuenta);
        movimiento.setTipoMovimiento(tipo);
        movimiento.setValor(new BigDecimal(valor));
        movimiento.setSaldo(new BigDecimal(saldo));
        return movimiento;
    }
}
