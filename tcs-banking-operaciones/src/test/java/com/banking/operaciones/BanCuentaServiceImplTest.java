package com.banking.operaciones;

import com.banking.operaciones.client.ClienteClient;
import com.banking.operaciones.client.ClienteResponseDTO;
import com.banking.operaciones.dto.CuentaRequestDTO;
import com.banking.operaciones.exception.BackofficeNoDisponibleException;
import com.banking.operaciones.exception.ClienteInactivoException;
import com.banking.operaciones.exception.ClienteNoEncontradoException;
import com.banking.operaciones.model.BanCuenta;
import com.banking.operaciones.model.enums.TipoCuenta;
import com.banking.operaciones.repository.BanCuentaRepository;
import com.banking.operaciones.serviceImpl.BanCuentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BanCuentaServiceImplTest {

    private static final String IDENTIFICACION = "1712345678";

    @Mock
    private BanCuentaRepository cuentaRepository;
    @Mock
    private ClienteClient clienteClient;

    private BanCuentaServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BanCuentaServiceImpl(cuentaRepository, clienteClient);
    }

    @Test
    void debePersistirElPrimaryKeyDelClienteEnLaCuenta() {
        when(clienteClient.buscarPorIdentificacion(IDENTIFICACION))
                .thenReturn(new ClienteResponseDTO(25L, IDENTIFICACION, "Jose Lema", true));
        when(cuentaRepository.save(any(BanCuenta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BanCuenta resultado = service.crearCuenta(requestValido());

        ArgumentCaptor<BanCuenta> captor = ArgumentCaptor.forClass(BanCuenta.class);
        verify(cuentaRepository).save(captor.capture());
        assertThat(resultado.getClienteId()).isEqualTo(25L);
        assertThat(captor.getValue().getClienteId()).isEqualTo(25L);
        assertThat(captor.getValue().getIdentificacionCliente()).isEqualTo(IDENTIFICACION);
        assertThat(captor.getValue().getTipoCuenta()).isEqualTo(TipoCuenta.AHO);
    }

    @Test
    void debeCrearCuentaCteConTipoFuertementeTipado() {
        when(clienteClient.buscarPorIdentificacion(IDENTIFICACION))
                .thenReturn(new ClienteResponseDTO(25L, IDENTIFICACION, "Jose Lema", true));
        when(cuentaRepository.save(any(BanCuenta.class))).thenAnswer(invocation -> invocation.getArgument(0));
        CuentaRequestDTO request = requestValido();
        request.setTipoCuenta(TipoCuenta.CTE);

        BanCuenta resultado = service.crearCuenta(request);

        assertThat(resultado.getTipoCuenta()).isEqualTo(TipoCuenta.CTE);
    }

    @Test
    void noDebeGuardarCuandoElClienteNoExiste() {
        when(clienteClient.buscarPorIdentificacion(IDENTIFICACION))
                .thenThrow(new ClienteNoEncontradoException(IDENTIFICACION));

        assertThatThrownBy(() -> service.crearCuenta(requestValido()))
                .isInstanceOf(ClienteNoEncontradoException.class);
        verify(cuentaRepository, never()).save(any());
    }

    @Test
    void noDebeGuardarCuandoElClienteEstaInactivo() {
        when(clienteClient.buscarPorIdentificacion(IDENTIFICACION))
                .thenReturn(new ClienteResponseDTO(25L, IDENTIFICACION, "Jose Lema", false));

        assertThatThrownBy(() -> service.crearCuenta(requestValido()))
                .isInstanceOf(ClienteInactivoException.class);
        verify(cuentaRepository, never()).save(any());
    }

    @Test
    void debePropagarErrorControladoCuandoBackofficeNoEstaDisponible() {
        when(clienteClient.buscarPorIdentificacion(IDENTIFICACION))
                .thenThrow(new BackofficeNoDisponibleException("Servicio Backoffice no disponible"));

        assertThatThrownBy(() -> service.crearCuenta(requestValido()))
                .isInstanceOf(BackofficeNoDisponibleException.class)
                .hasMessage("Servicio Backoffice no disponible");
        verify(cuentaRepository, never()).save(any());
    }

    private CuentaRequestDTO requestValido() {
        CuentaRequestDTO request = new CuentaRequestDTO();
        request.setIdentificacionCliente(IDENTIFICACION);
        request.setTipoCuenta(TipoCuenta.AHO);
        request.setSaldoInicial(new BigDecimal("500.00"));
        return request;
    }
}
