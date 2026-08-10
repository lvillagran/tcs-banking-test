package com.banking.banking;

import com.banking.backoffice.model.BanCliente;
import com.banking.backoffice.model.TipoIdentificacion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

        public class BanClienteTest {

            @Test
            @DisplayName("Debe crear un BanCliente con los valores proporcionados")
            public void testBanClienteConstructor() {
                BanCliente cliente = new BanCliente(1L, "123456789", "LVILLAGRAN", true);

                assertEquals(1L, cliente.getId());
                assertEquals("123456789", cliente.getContrasena());
                assertEquals("LVILLAGRAN", cliente.getUsuario());
                assertTrue(cliente.getEstado());
            }

            @Test
            @DisplayName("Debe establecer y obtener correctamente los atributos de BanCliente")
            public void testSettersAndGetters() {
                BanCliente cliente = new BanCliente();
                cliente.setId(10L);
                cliente.setUsuario("user10");
                cliente.setContrasena("pass10");
                cliente.setEstado(false);
                cliente.setTipoIdentificacion(TipoIdentificacion.CED);

                assertEquals(10L, cliente.getId());
                assertEquals("user10", cliente.getUsuario());
                assertEquals("pass10", cliente.getContrasena());
                assertFalse(cliente.getEstado());
                assertEquals(TipoIdentificacion.CED, cliente.getTipoIdentificacion());
            }
        }
