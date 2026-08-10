package com.tata.banca.model;

import com.banking.operaciones.Controller.ReporteController;
import com.banking.operaciones.dto.ReporteClienteResponseDTO;
import com.banking.operaciones.dto.ReporteDetalleResponseDTO;
import com.banking.operaciones.exception.ApiExceptionHandler;
import com.banking.operaciones.serviceImpl.ReporteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReporteControllerTests {

    @Mock
    private ReporteService reporteService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ReporteController(reporteService))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void debeAceptarElContratoJsonDelEstadoDeCuenta() throws Exception {
        ReporteDetalleResponseDTO reporte = new ReporteDetalleResponseDTO(
                "2026-08-01", "2026-08-30",
                new ReporteClienteResponseDTO(14L, "0945678901", "Cliente Prueba"),
                List.of());
        when(reporteService.generar(
                "0945678901", LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 30)))
                .thenReturn(reporte);

        mockMvc.perform(post("/api/v1/operaciones/reportes/estado-cuenta")
                        .contentType("application/json")
                        .content("""
                                {
                                  "identificacionCliente": "0945678901",
                                  "fechaInicio": "01-08-2026",
                                  "fechaFin": "30-08-2026"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Reporte generado correctamente."))
                .andExpect(jsonPath("$.reporte.fechaDesde").value("2026-08-01"))
                .andExpect(jsonPath("$.reporte.fechaHasta").value("2026-08-30"))
                .andExpect(jsonPath("$.reporte.cliente.identificacion").value("0945678901"));
    }

    @Test
    void debeRechazarFechasConFormatoInvalido() throws Exception {
        mockMvc.perform(post("/api/v1/operaciones/reportes/estado-cuenta")
                        .contentType("application/json")
                        .content("""
                                {
                                  "identificacionCliente": "0945678901",
                                  "fechaInicio": "2026-08-01",
                                  "fechaFin": "2026-08-30"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje")
                        .value("Fechas inválidas. Formato esperado: dd-MM-yyyy"));
    }
}
