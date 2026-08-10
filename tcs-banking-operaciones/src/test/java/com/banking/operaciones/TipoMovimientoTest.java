package com.banking.operaciones;

import com.banking.operaciones.model.enums.TipoMovimiento;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TipoMovimientoTest {

    @Test
    void debeDefinirCodigosYDescripcionesOficiales() {
        assertThat(TipoMovimiento.DEP.name()).isEqualTo("DEP");
        assertThat(TipoMovimiento.DEP.getDescripcion()).isEqualTo("Depósito");
        assertThat(TipoMovimiento.RET.name()).isEqualTo("RET");
        assertThat(TipoMovimiento.RET.getDescripcion()).isEqualTo("Retiro");
    }
}
