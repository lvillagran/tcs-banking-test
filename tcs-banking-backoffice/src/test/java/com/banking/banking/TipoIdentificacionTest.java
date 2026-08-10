package com.banking.banking;

import com.banking.backoffice.model.TipoIdentificacion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TipoIdentificacionTest {

    @Test
    void debeExponerLasDescripcionesDeLosTiposPermitidos() {
        assertEquals("Cédula", TipoIdentificacion.CED.getDescripcion());
        assertEquals("Registro Único de Contribuyentes", TipoIdentificacion.RUC.getDescripcion());
    }

    @Test
    void debeValidarLaLongitudYElFormatoNumerico() {
        assertEquals(true, TipoIdentificacion.CED.esIdentificacionValida("0945678903"));
        assertEquals(false, TipoIdentificacion.CED.esIdentificacionValida("1790012345001"));
        assertEquals(true, TipoIdentificacion.RUC.esIdentificacionValida("1790012345001"));
        assertEquals(false, TipoIdentificacion.RUC.esIdentificacionValida("0945678903"));
        assertEquals(false, TipoIdentificacion.CED.esIdentificacionValida("094567890A"));
    }
}
