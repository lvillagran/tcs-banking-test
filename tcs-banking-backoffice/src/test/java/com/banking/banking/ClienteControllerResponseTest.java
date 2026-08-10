package com.banking.banking;

import com.banking.backoffice.Controller.ClienteController;
import com.banking.backoffice.model.BanCliente;
import com.banking.backoffice.model.TipoIdentificacion;
import com.banking.backoffice.serviceImpl.BanClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClienteControllerResponseTest {

    @Mock
    private BanClienteServiceImpl clienteService;

    @InjectMocks
    private ClienteController controller;

    private MockMvc mockMvc;
    private BanCliente cliente;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        cliente = new BanCliente();
        cliente.setId(12L);
        cliente.setNombre("JOSE LEMA");
        cliente.setGenero("M");
        cliente.setEdad("35");
        cliente.setIdentificacion("0945678903");
        cliente.setTipoIdentificacion(TipoIdentificacion.CED);
        cliente.setDireccion("OTAVALO SN Y PRINCIPAL");
        cliente.setTelefono("098254785");
        cliente.setEstado(true);
        cliente.setUsuario("0945678903");
        cliente.setContrasena("secreto");
        cliente.setIp("127.0.0.1");
        cliente.setObservacion("INTERNO");
    }

    @Test
    void crearClienteDebeRetornarContratoSeguro() throws Throwable {
        when(clienteService.findByIdentificacion("0945678903")).thenReturn(Optional.empty());
        when(clienteService.crearCliente(any())).thenReturn(cliente);

        mockMvc.perform(post("/api/v1/mantenimiento/clientes/crear")
                        .contentType("application/json")
                        .content("""
                                {"identificacion":"0945678903","tipoIdentificacion":"CED","nombre":"Jose Lema","genero":"M",
                                 "edad":"35","direccion":"Otavalo sn y principal","telefono":"098254785"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cliente.id").value(12))
                .andExpect(jsonPath("$.cliente.identificacion").value("0945678903"))
                .andExpect(jsonPath("$.cliente.tipoIdentificacion").value("CED"))
                .andExpect(jsonPath("$.cliente.estado").value(true))
                .andExpect(jsonPath("$.cliente.contrasena").doesNotExist())
                .andExpect(jsonPath("$.cliente.password").doesNotExist())
                .andExpect(jsonPath("$.cliente.ip").doesNotExist())
                .andExpect(jsonPath("$.cliente.observacion").doesNotExist())
                .andExpect(jsonPath("$.cliente.fechaRegistro").doesNotExist());
    }

    @Test
    void crearClienteDebeRechazarTipoIdentificacionAusente() throws Exception {
        mockMvc.perform(post("/api/v1/mantenimiento/clientes/crear")
                        .contentType("application/json")
                        .content("""
                                {"identificacion":"0945678903","nombre":"Jose Lema","genero":"M",
                                 "edad":"35","direccion":"Otavalo sn y principal","telefono":"098254785"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(
                        "El tipo de identificación es requerido. Valores permitidos: CED, RUC."));
    }

    @Test
    void crearClienteDebeRechazarCedulaConLongitudIncorrecta() throws Exception {
        mockMvc.perform(post("/api/v1/mantenimiento/clientes/crear")
                        .contentType("application/json")
                        .content("""
                                {"identificacion":"0945678903123","tipoIdentificacion":"CED",
                                 "nombre":"Jose Lema","genero":"M","edad":"35",
                                 "direccion":"Otavalo sn y principal","telefono":"098254785"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(
                        "La identificación tipo CED debe contener exactamente 10 dígitos."));
    }

    @Test
    void crearClienteDebeRechazarRucConLongitudIncorrecta() throws Exception {
        mockMvc.perform(post("/api/v1/mantenimiento/clientes/crear")
                        .contentType("application/json")
                        .content("""
                                {"identificacion":"0945678903","tipoIdentificacion":"RUC",
                                 "nombre":"Empresa Uno","genero":"M","edad":"1",
                                 "direccion":"Quito","telefono":"098254785"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(
                        "La identificación tipo RUC debe contener exactamente 13 dígitos."));
    }

    @Test
    void listarClientesDebeRetornarEnvelopeSeguro() throws Exception {
        when(clienteService.findAll()).thenReturn(List.of(cliente));

        mockMvc.perform(get("/api/v1/mantenimiento/clientes/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.clientes[0].id").value(12))
                .andExpect(jsonPath("$.clientes[0].tipoIdentificacion").value("CED"))
                .andExpect(jsonPath("$.clientes[0].contrasena").doesNotExist())
                .andExpect(jsonPath("$.clientes[0].ip").doesNotExist())
                .andExpect(jsonPath("$.clientes[0].observacion").doesNotExist());
    }

    @Test
    void consultarClienteDebeRetornarEnvelopeSeguro() throws Exception {
        when(clienteService.findByIdentificacion("0945678903")).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/v1/mantenimiento/clientes/consultarPersonaPorIdentificacion/0945678903"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cliente.nombre").value("JOSE LEMA"))
                .andExpect(jsonPath("$.cliente.tipoIdentificacion").value("CED"))
                .andExpect(jsonPath("$.cliente.contrasena").doesNotExist())
                .andExpect(jsonPath("$.cliente.ip").doesNotExist());
    }
}
