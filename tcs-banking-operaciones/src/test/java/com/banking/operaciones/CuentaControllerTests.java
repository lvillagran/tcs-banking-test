package com.banking.operaciones;
import com.banking.operaciones.Controller.CuentaController;
import com.banking.operaciones.dto.CuentaRequestDTO;
import com.banking.operaciones.dto.CuentaResponseDTO;
import com.banking.operaciones.dto.CuentasResponseDTO;
import com.banking.operaciones.dto.MensajeResponseDTO;
import com.banking.operaciones.exception.SolicitudInvalidaException;
import com.banking.operaciones.model.BanCuenta;
import com.banking.operaciones.model.enums.TipoCuenta;
import com.banking.operaciones.serviceImpl.BanCuentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CuentaControllerTests {

    @Mock
    private BanCuentaServiceImpl cuentaService;

    @InjectMocks
    private CuentaController cuentaController;

    private BanCuenta cuenta;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cuenta = new BanCuenta();
        cuenta.setId(30L);
        cuenta.setNumeroCuenta("1234567890");
        cuenta.setSaldoDisponible(BigDecimal.valueOf(1000));
        cuenta.setSaldoInicial(BigDecimal.valueOf(1000));
        cuenta.setTipoCuenta(TipoCuenta.AHO);
        cuenta.setEstado(true);
        cuenta.setClienteId(12L);
        cuenta.setIdentificacionCliente("0945678903");
        cuenta.setIp("127.0.0.1");
        cuenta.setObservacion("INTERNO");
        mockMvc = MockMvcBuilders.standaloneSetup(cuentaController).build();
    }

    @Test
    @DisplayName("Debe crear una cuenta con éxito con los valores proporcionados")
    public void testCrearCuenta_Exitoso() {
        CuentaRequestDTO request = new CuentaRequestDTO();
        request.setIdentificacionCliente("123456789");
        request.setTipoCuenta(TipoCuenta.AHO);
        request.setSaldoInicial(BigDecimal.valueOf(1000));

        when(cuentaService.crearCuenta(any())).thenReturn(cuenta);

        ResponseEntity<CuentaResponseDTO> response = cuentaController.crearCuenta(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Cuenta creada correctamente.", response.getBody().getMensaje());
    }


    @Test
    @DisplayName("Debe devolver error al crear una cuenta sin identificación del cliente")
    public void testCrearCuenta_FaltaIdentificacion() {
        CuentaRequestDTO request = new CuentaRequestDTO();
        request.setIdentificacionCliente(null);
        request.setTipoCuenta(TipoCuenta.AHO);

        when(cuentaService.crearCuenta(any())).thenThrow(
                new SolicitudInvalidaException("La identificación del cliente es obligatoria."));

        org.junit.jupiter.api.Assertions.assertThrows(
                SolicitudInvalidaException.class,
                () -> cuentaController.crearCuenta(request));
    }

    @Test
    @DisplayName("Debe listar todas las cuentas con éxito")
    public void testListarCuentas_Exitoso() throws Exception {
        when(cuentaService.findAll()).thenReturn(List.of(cuenta));

        mockMvc.perform(get("/api/v1/operaciones/cuentas/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.cuentas[0].id").value(30))
                .andExpect(jsonPath("$.cuentas[0].tipoCuenta").value("AHO"))
                .andExpect(jsonPath("$.cuentas[0].clienteId").value(12))
                .andExpect(jsonPath("$.cuentas[0].ip").doesNotExist())
                .andExpect(jsonPath("$.cuentas[0].observacion").doesNotExist())
                .andExpect(jsonPath("$.cuentas[0].fechaRegistro").doesNotExist())
                .andExpect(jsonPath("$.cuentas[0].fechaActualizacion").doesNotExist());
    }

    @Test
    @DisplayName("Debe devolver un estado No Content cuando no haya cuentas")
    public void testListarCuentas_SinCuentas() {
        when(cuentaService.findAll()).thenReturn(List.of());

        ResponseEntity<CuentasResponseDTO> response = cuentaController.listarCuentas();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Debe eliminar una cuenta con éxito")
    public void testEliminarCuenta_Exitoso() {
        when(cuentaService.findByNumeroCuenta("1234567890")).thenReturn(Optional.of(cuenta));

        ResponseEntity<MensajeResponseDTO> response = cuentaController.eliminarCuenta("1234567890");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cuenta eliminada correctamente.", response.getBody().getMensaje());

        verify(cuentaService).findByNumeroCuenta("1234567890");
    }

    @Test
    @DisplayName("Debe devolver error si la cuenta no existe al intentar eliminarla")
    public void testEliminarCuenta_NoEncontrada() {
        when(cuentaService.findByNumeroCuenta("9876543210")).thenReturn(Optional.empty());

        ResponseEntity<MensajeResponseDTO> response = cuentaController.eliminarCuenta("9876543210");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cuenta con número: 9876543210 no encontrada.", response.getBody().getMensaje());
    }

    @Test
    @DisplayName("Debe actualizar una cuenta con éxito con los valores proporcionados")
    public void testActualizarCuenta_Exitoso() {
        CuentaRequestDTO request = new CuentaRequestDTO();
        request.setTipoCuenta(TipoCuenta.CTE);
        request.setSaldoInicial(BigDecimal.valueOf(2000));
        request.setEstado(true);

        when(cuentaService.findByNumeroCuenta("1234567890")).thenReturn(Optional.of(cuenta));
        when(cuentaService.actualizarCuenta(any())).thenReturn(cuenta);

        ResponseEntity<CuentaResponseDTO> response = cuentaController.actualizarCuenta("1234567890", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cuenta actualizada correctamente.", response.getBody().getMensaje());
    }

    @Test
    @DisplayName("Debe devolver error si la cuenta no existe al intentar actualizarla")
    public void testActualizarCuenta_NoEncontrada() {
        CuentaRequestDTO request = new CuentaRequestDTO();
        request.setTipoCuenta(TipoCuenta.CTE);
        request.setSaldoInicial(BigDecimal.valueOf(2000));
        request.setEstado(true);

        when(cuentaService.findByNumeroCuenta("9876543210")).thenReturn(Optional.empty());

        ResponseEntity<CuentaResponseDTO> response = cuentaController.actualizarCuenta("9876543210", request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cuenta con número: 9876543210 no encontrada.", response.getBody().getMensaje());
    }
}
