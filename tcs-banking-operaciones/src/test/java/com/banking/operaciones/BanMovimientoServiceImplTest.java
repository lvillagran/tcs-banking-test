package com.banking.operaciones;

import com.banking.operaciones.dto.MovimientoRequestDTO;
import com.banking.operaciones.exception.SaldoNoDisponibleException;
import com.banking.operaciones.model.BanCuenta;
import com.banking.operaciones.model.BanMovimientos;
import com.banking.operaciones.model.enums.TipoMovimiento;
import com.banking.operaciones.repository.BanCuentaRepository;
import com.banking.operaciones.repository.BanMovimientoRepository;
import com.banking.operaciones.serviceImpl.BanMovimientoServiceImpl;
import com.banking.operaciones.serviceImpl.MovimientoCreadoResultado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BanMovimientoServiceImplTest {

    @Mock
    private BanMovimientoRepository movimientoRepository;
    @Mock
    private BanCuentaRepository cuentaRepository;

    private BanMovimientoServiceImpl service;
    private BanCuenta cuenta;

    @BeforeEach
    void setUp() {
        service = new BanMovimientoServiceImpl(movimientoRepository, cuentaRepository);
        cuenta = new BanCuenta();
        cuenta.setNumeroCuenta("7909950040");
        cuenta.setSaldoDisponible(new BigDecimal("410.00"));
    }

    @Test
    void debeBloquearCuentaYGuardarDepositoConSaldoActualizado() {
        when(cuentaRepository.findByNumeroCuentaForUpdate("7909950040"))
                .thenReturn(Optional.of(cuenta));
        when(movimientoRepository.save(any(BanMovimientos.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MovimientoCreadoResultado resultado = service.procesarMovimiento(
                request(TipoMovimiento.DEP, "100.00"));

        ArgumentCaptor<BanMovimientos> movimientoCaptor =
                ArgumentCaptor.forClass(BanMovimientos.class);
        verify(cuentaRepository).findByNumeroCuentaForUpdate("7909950040");
        verify(movimientoRepository).save(movimientoCaptor.capture());
        verify(cuentaRepository).save(cuenta);
        assertThat(resultado.saldoAnterior()).isEqualByComparingTo("410.00");
        assertThat(cuenta.getSaldoDisponible()).isEqualByComparingTo("510.00");
        assertThat(movimientoCaptor.getValue().getSaldo()).isEqualByComparingTo("510.00");
    }

    @Test
    void noDebeEscribirCuandoElRetiroSuperaElSaldoDisponible() {
        cuenta.setSaldoDisponible(new BigDecimal("50.00"));
        when(cuentaRepository.findByNumeroCuentaForUpdate("7909950040"))
                .thenReturn(Optional.of(cuenta));

        assertThatThrownBy(() -> service.procesarMovimiento(
                request(TipoMovimiento.RET, "100.00")))
                .isInstanceOf(SaldoNoDisponibleException.class)
                .hasMessage("Saldo no disponible");

        verify(movimientoRepository, never()).save(any());
        verify(cuentaRepository, never()).save(any());
        assertThat(cuenta.getSaldoDisponible()).isEqualByComparingTo("50.00");
    }

    private MovimientoRequestDTO request(TipoMovimiento tipo, String valor) {
        MovimientoRequestDTO request = new MovimientoRequestDTO();
        request.setNumeroCuenta("7909950040");
        request.setTipoMovimiento(tipo);
        request.setValor(new BigDecimal(valor));
        return request;
    }
}
