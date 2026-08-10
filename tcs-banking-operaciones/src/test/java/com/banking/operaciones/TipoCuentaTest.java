package com.banking.operaciones;

import com.banking.operaciones.model.enums.TipoCuenta;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TipoCuentaTest {

    @Test
    void ahoDebeExponerNombreYDescripcion() {
        assertThat(TipoCuenta.AHO.name()).isEqualTo("AHO");
        assertThat(TipoCuenta.AHO.getDescripcion()).isEqualTo("Cuenta de Ahorro");
    }

    @Test
    void cteDebeExponerNombreYDescripcion() {
        assertThat(TipoCuenta.CTE.name()).isEqualTo("CTE");
        assertThat(TipoCuenta.CTE.getDescripcion()).isEqualTo("Cuenta Corriente");
    }
}
