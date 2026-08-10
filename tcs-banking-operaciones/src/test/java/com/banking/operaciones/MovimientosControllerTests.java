package com.tata.banca.model;

import com.banking.operaciones.Controller.MovimientoController;
import com.banking.operaciones.dto.CrearMovimientoResponseDTO;
import com.banking.operaciones.dto.MovimientoDetalleResponseDTO;
import com.banking.operaciones.dto.MovimientosResponseDTO;
import com.banking.operaciones.dto.MovimientoRequestDTO;
import com.banking.operaciones.dto.MovimientoResponseDTO;
import com.banking.operaciones.model.BanCuenta;
import com.banking.operaciones.model.BanMovimientos;
import com.banking.operaciones.model.enums.TipoCuenta;
import com.banking.operaciones.model.enums.TipoMovimiento;
import com.banking.operaciones.exception.ApiExceptionHandler;
import com.banking.operaciones.exception.SaldoNoDisponibleException;
import com.banking.operaciones.serviceImpl.BanMovimientoServiceImpl;
import com.banking.operaciones.serviceImpl.MovimientoCreadoResultado;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MovimientosControllerTests {

    @Mock
    private BanMovimientoServiceImpl banMovimientoService;

    @InjectMocks
    private MovimientoController movimientoController;

    private BanCuenta cuenta;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cuenta = new BanCuenta();
        cuenta.setNumeroCuenta("1234567890");
        cuenta.setSaldoDisponible(BigDecimal.valueOf(1000));
        cuenta.setSaldoInicial(BigDecimal.valueOf(1000));
        cuenta.setTipoCuenta(TipoCuenta.AHO);
        mockMvc = MockMvcBuilders.standaloneSetup(movimientoController)
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Debe crear un movimiento de depósito con los valores proporcionados")
    public void testCrearMovimiento_Deposito_Exitoso() throws Exception {
        cuenta.setNumeroCuenta("7909950040");
        cuenta.setSaldoDisponible(new BigDecimal("410.00"));
        BanMovimientos movimiento = movimientoCreado(5L, TipoMovimiento.DEP, "100.00", "510.00");
        cuenta.setSaldoDisponible(new BigDecimal("510.00"));
        when(banMovimientoService.procesarMovimiento(any()))
                .thenReturn(new MovimientoCreadoResultado(
                        movimiento, cuenta, new BigDecimal("410.00")));

        mockMvc.perform(post("/api/v1/operaciones/movimientos/transaccion")
                        .contentType("application/json")
                        .content("""
                                {"tipoMovimiento":"DEP","valor":100.00,"numeroCuenta":"7909950040"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Depósito creado correctamente."))
                .andExpect(jsonPath("$.movimiento.id").value(5))
                .andExpect(jsonPath("$.movimiento.numeroCuenta").value("7909950040"))
                .andExpect(jsonPath("$.movimiento.tipoCuenta").value("AHO"))
                .andExpect(jsonPath("$.movimiento.tipoMovimiento").value("DEP"))
                .andExpect(jsonPath("$.movimiento.valor").value(100.00))
                .andExpect(jsonPath("$.movimiento.saldoAnterior").value(410.00))
                .andExpect(jsonPath("$.movimiento.saldoDisponible").value(510.00))
                .andExpect(jsonPath("$.movimiento.cuenta").doesNotExist())
                .andExpect(jsonPath("$.movimiento.ip").doesNotExist())
                .andExpect(jsonPath("$.movimiento.observacion").doesNotExist())
                .andExpect(jsonPath("$.movimiento.fechaRegistro").doesNotExist())
                .andExpect(jsonPath("$.movimientos").doesNotExist());
    }

    @Test
    @DisplayName("Debe crear un retiro utilizando el código RET")
    void testCrearMovimiento_Retiro_Exitoso() throws Exception {
        cuenta.setNumeroCuenta("7909950040");
        cuenta.setSaldoDisponible(new BigDecimal("510.00"));
        BanMovimientos movimiento = movimientoCreado(6L, TipoMovimiento.RET, "100.00", "410.00");
        cuenta.setSaldoDisponible(new BigDecimal("410.00"));
        when(banMovimientoService.procesarMovimiento(any()))
                .thenReturn(new MovimientoCreadoResultado(
                        movimiento, cuenta, new BigDecimal("510.00")));

        mockMvc.perform(post("/api/v1/operaciones/movimientos/transaccion")
                        .contentType("application/json")
                        .content("""
                                {"tipoMovimiento":"RET","valor":100.00,"numeroCuenta":"7909950040"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Retiro creado correctamente."))
                .andExpect(jsonPath("$.movimiento.tipoMovimiento").value("RET"))
                .andExpect(jsonPath("$.movimiento.saldoAnterior").value(510.00))
                .andExpect(jsonPath("$.movimiento.saldoDisponible").value(410.00));
    }

    @Test
    @DisplayName("Debe responder Saldo no disponible cuando el retiro supera el saldo")
    void testCrearMovimiento_RetiroSinSaldoDisponible() throws Exception {
        cuenta.setNumeroCuenta("7909950040");
        cuenta.setSaldoDisponible(new BigDecimal("50.00"));
        when(banMovimientoService.procesarMovimiento(any()))
                .thenThrow(new SaldoNoDisponibleException());

        mockMvc.perform(post("/api/v1/operaciones/movimientos/transaccion")
                        .contentType("application/json")
                        .content("""
                                {"tipoMovimiento":"RET","valor":100.00,"numeroCuenta":"7909950040"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Saldo no disponible"));
    }

    @Test
    @DisplayName("Debe rechazar la nomenclatura técnica anterior")
    void testCrearMovimiento_RechazaDepositoLegacy() throws Exception {
        for (String valorInvalido : List.of(
                "DEPOSITO", "RETIRO", "deposito", "retiro", "DEPÓSITO", "DEPO", "RETIROO", "XXX")) {
            mockMvc.perform(post("/api/v1/operaciones/movimientos/transaccion")
                            .contentType("application/json")
                            .content("""
                                    {"tipoMovimiento":"%s","valor":100.00,"numeroCuenta":"7909950040"}
                                    """.formatted(valorInvalido)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensaje")
                            .value("Tipo de movimiento inválido. Valores permitidos: DEP, RET"));
        }
    }

    @Test
    @DisplayName("Debe retornar error si el número de cuenta es obligatorio al crear un movimiento")
    public void testCrearMovimiento_FaltaNumeroCuenta() {
        MovimientoRequestDTO request = new MovimientoRequestDTO();
        request.setNumeroCuenta(null);
        request.setTipoMovimiento(TipoMovimiento.DEP);
        request.setValor(BigDecimal.valueOf(500));

        ResponseEntity<CrearMovimientoResponseDTO> response = movimientoController.crearMovimiento(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El número de cuenta es obligatorio.", response.getBody().getMensaje());
    }

    @Test
    @DisplayName("Debe listar todos los movimientos de la base de datos")
    public void testListarMovimientos_Exitoso() throws Exception {
        BanMovimientos movimiento = new BanMovimientos();
        movimiento.setId(5L);
        movimiento.setCuenta(cuenta);
        movimiento.setNumeroCuenta(cuenta.getNumeroCuenta());
        movimiento.setMovimiento("Depósito valor 100.00");
        movimiento.setTipoMovimiento(TipoMovimiento.DEP);
        movimiento.setValor(new BigDecimal("100.00"));
        movimiento.setSaldo(new BigDecimal("510.00"));
        when(banMovimientoService.findAll()).thenReturn(List.of(movimiento));

        mockMvc.perform(get("/api/v1/operaciones/movimientos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.movimientos[0].id").value(5))
                .andExpect(jsonPath("$.movimientos[0].numeroCuenta").value("1234567890"))
                .andExpect(jsonPath("$.movimientos[0].tipoCuenta").value("AHO"))
                .andExpect(jsonPath("$.movimientos[0].tipoMovimiento").value("DEP"))
                .andExpect(jsonPath("$.movimientos[0].valor").value(100.00))
                .andExpect(jsonPath("$.movimientos[0].saldoAnterior").value(410.00))
                .andExpect(jsonPath("$.movimientos[0].saldoDisponible").value(510.00))
                .andExpect(jsonPath("$.movimientos[0].cuenta").doesNotExist())
                .andExpect(jsonPath("$.movimientos[0].ip").doesNotExist())
                .andExpect(jsonPath("$.movimientos[0].observacion").doesNotExist())
                .andExpect(jsonPath("$.movimientos[0].fechaRegistro").doesNotExist());
    }

    @Test
    @DisplayName("Debe retornar sin contenido si no hay movimientos registrados")
    public void testListarMovimientos_SinContenido() {
        when(banMovimientoService.findAll()).thenReturn(List.of());

        ResponseEntity<MovimientosResponseDTO> response = movimientoController.listarMovimientos();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Debe consultar un movimiento por su ID sin exponer entidades")
    public void testConsultarMovimientoPorId_Exitoso() throws Exception {
        BanMovimientos movimiento = new BanMovimientos();
        movimiento.setId(5L);
        movimiento.setCuenta(cuenta);
        movimiento.setNumeroCuenta(cuenta.getNumeroCuenta());
        movimiento.setTipoMovimiento(TipoMovimiento.DEP);
        movimiento.setValor(new BigDecimal("100.00"));
        movimiento.setSaldo(new BigDecimal("1100.00"));
        when(banMovimientoService.findById(5L)).thenReturn(Optional.of(movimiento));

        mockMvc.perform(get("/api/v1/operaciones/movimientos/consultar/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Movimiento consultado correctamente."))
                .andExpect(jsonPath("$.movimiento.id").value(5))
                .andExpect(jsonPath("$.movimiento.numeroCuenta").value("1234567890"))
                .andExpect(jsonPath("$.movimiento.tipoCuenta").value("AHO"))
                .andExpect(jsonPath("$.movimiento.tipoMovimiento").value("DEP"))
                .andExpect(jsonPath("$.movimiento.valor").value(100.00))
                .andExpect(jsonPath("$.movimiento.saldoAnterior").value(1000.00))
                .andExpect(jsonPath("$.movimiento.saldoDisponible").value(1100.00))
                .andExpect(jsonPath("$.movimiento.cuenta").doesNotExist())
                .andExpect(jsonPath("$.movimiento.ip").doesNotExist())
                .andExpect(jsonPath("$.movimiento.observacion").doesNotExist());
    }

    @Test
    @DisplayName("Debe retornar 404 al consultar un movimiento inexistente")
    public void testConsultarMovimientoPorId_NoEncontrado() throws Exception {
        when(banMovimientoService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/operaciones/movimientos/consultar/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Movimiento no encontrado."))
                .andExpect(jsonPath("$.movimiento").doesNotExist());
    }

    @Test
    @DisplayName("Debe eliminar únicamente el movimiento indicado por su ID")
    public void testEliminarMovimientoPorId_Exitoso() throws Exception {
        BanMovimientos movimiento = new BanMovimientos();
        movimiento.setId(5L);
        when(banMovimientoService.findById(5L)).thenReturn(Optional.of(movimiento));

        mockMvc.perform(delete("/api/v1/operaciones/movimientos/eliminar/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Movimiento eliminado correctamente."));

        verify(banMovimientoService).eliminarMovimiento(movimiento);
    }

    @Test
    @DisplayName("Debe retornar error si el movimiento no existe")
    public void testEliminarMovimientoPorId_NoEncontrado() throws Exception {
        when(banMovimientoService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/operaciones/movimientos/eliminar/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Movimiento no encontrado."));
    }

    private BanMovimientos movimientoCreado(
            Long id, TipoMovimiento tipo, String valor, String saldo) {
        BanMovimientos movimiento = new BanMovimientos();
        movimiento.setId(id);
        movimiento.setCuenta(cuenta);
        movimiento.setNumeroCuenta(cuenta.getNumeroCuenta());
        movimiento.setTipoMovimiento(tipo);
        movimiento.setValor(new BigDecimal(valor));
        movimiento.setSaldo(new BigDecimal(saldo));
        return movimiento;
    }

}
