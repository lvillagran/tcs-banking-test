package com.banking.operaciones;

import com.banking.operaciones.OperacionesMain;
import com.banking.operaciones.model.BanCuenta;
import com.banking.operaciones.model.BanMovimientos;
import com.banking.operaciones.model.enums.TipoCuenta;
import com.banking.operaciones.model.enums.TipoMovimiento;
import com.banking.operaciones.repository.BanCuentaRepository;
import com.banking.operaciones.repository.BanMovimientoRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = OperacionesMain.class,
        properties = "spring.kafka.admin.auto-create=false")
@Transactional
class TipoMovimientoJpaPostgreSqlIntegrationTest {

    @Autowired
    private BanCuentaRepository cuentaRepository;

    @Autowired
    private BanMovimientoRepository movimientoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Test
    void debePersistirFisicamenteDepYRet() {
        BanCuenta cuenta = cuentaRepository.saveAndFlush(nuevaCuenta());
        BanMovimientos movimiento = nuevoMovimiento(cuenta);

        for (TipoMovimiento tipo : TipoMovimiento.values()) {
            movimiento.setTipoMovimiento(tipo);
            movimiento.setMovimiento(tipo.getDescripcion() + " valor 10.00");
            movimiento = movimientoRepository.saveAndFlush(movimiento);

            String valorFisico = jdbcTemplate.queryForObject(
                    "SELECT tipo_movimiento FROM operaciones.tab_movimientos WHERE id_movimiento = ?",
                    String.class,
                    movimiento.getId());
            assertThat(valorFisico).isEqualTo(tipo.name());

            Long movimientoId = movimiento.getId();
            entityManager.clear();
            movimiento = movimientoRepository.findById(movimientoId).orElseThrow();
            assertThat(movimiento.getTipoMovimiento()).isEqualTo(tipo);
        }
    }

    private BanCuenta nuevaCuenta() {
        String numero = Long.toString(Math.abs(System.nanoTime()));
        numero = numero.substring(numero.length() - 10);
        BanCuenta cuenta = new BanCuenta();
        cuenta.setNumeroCuenta(numero);
        cuenta.setTipoCuenta(TipoCuenta.AHO);
        cuenta.setSaldoInicial(new BigDecimal("100.00"));
        cuenta.setSaldoDisponible(new BigDecimal("100.00"));
        cuenta.setEstado(true);
        cuenta.setIdentificacionCliente(numero);
        cuenta.setClienteId(1L);
        return cuenta;
    }

    private BanMovimientos nuevoMovimiento(BanCuenta cuenta) {
        BanMovimientos movimiento = new BanMovimientos();
        movimiento.setFechaMovimiento(new Date());
        movimiento.setValor(new BigDecimal("10.00"));
        movimiento.setSaldo(new BigDecimal("110.00"));
        movimiento.setNumeroCuenta(cuenta.getNumeroCuenta());
        movimiento.setEstado(true);
        movimiento.setCuenta(cuenta);
        return movimiento;
    }
}
