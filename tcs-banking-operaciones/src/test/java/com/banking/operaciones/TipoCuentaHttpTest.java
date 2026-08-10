package com.banking.operaciones;

import com.banking.operaciones.Controller.CuentaController;
import com.banking.operaciones.exception.ApiExceptionHandler;
import com.banking.operaciones.model.BanCuenta;
import com.banking.operaciones.model.enums.TipoCuenta;
import com.banking.operaciones.messaging.CuentaEventPublisher;
import com.banking.operaciones.serviceImpl.BanCuentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TipoCuentaHttpTest {

    @Mock
    private BanCuentaServiceImpl cuentaService;

    @Mock
    private CuentaEventPublisher cuentaEventPublisher;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(new CuentaController(cuentaService, cuentaEventPublisher))
                .setControllerAdvice(new ApiExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void valoresNoOficialesDebenResponderBadRequest() throws Exception {
        String[] valoresInvalidos = {
                "AHORRO", "CORRIENTE", "Ahorro", "Corriente", "ahorro",
                "corriente", "AHORROS", "AHOORRO", "XXX"
        };

        for (String valor : valoresInvalidos) {
            mockMvc.perform(post("/api/v1/operaciones/cuentas/crear")
                            .contentType("application/json")
                            .content("""
                                    {"identificacionCliente":"1712345678","tipoCuenta":"%s","saldoInicial":1000.00}
                                    """.formatted(valor)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensaje")
                            .value("Tipo de cuenta inválido. Valores permitidos: AHO, CTE"));
        }
    }

    @Test
    void tipoCuentaNuloDebeResponderBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/operaciones/cuentas/crear")
                        .contentType("application/json")
                        .content("""
                                {"identificacionCliente":"1712345678","saldoInicial":1000.00}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El tipo de cuenta es obligatorio."));
    }

    @Test
    void valoresOficialesDelEnumDebenSerAceptados() throws Exception {
        for (TipoCuenta tipo : TipoCuenta.values()) {
            BanCuenta cuenta = new BanCuenta();
            cuenta.setTipoCuenta(tipo);
            when(cuentaService.crearCuenta(argThat(
                    request -> request != null && request.getTipoCuenta() == tipo)))
                    .thenReturn(cuenta);

            mockMvc.perform(post("/api/v1/operaciones/cuentas/crear")
                            .contentType("application/json")
                            .content("""
                                    {"identificacionCliente":"1712345678","tipoCuenta":"%s","saldoInicial":10.00}
                                    """.formatted(tipo.name())))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.cuenta.tipoCuenta").value(tipo.name()));
        }
    }
}
