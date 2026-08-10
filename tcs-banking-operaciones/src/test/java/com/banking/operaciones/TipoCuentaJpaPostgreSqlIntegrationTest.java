package com.banking.operaciones;

import com.banking.operaciones.OperacionesMain;
import com.banking.operaciones.model.BanCuenta;
import com.banking.operaciones.model.enums.TipoCuenta;
import com.banking.operaciones.repository.BanCuentaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = OperacionesMain.class)
@Transactional
class TipoCuentaJpaPostgreSqlIntegrationTest {

    @Autowired
    private BanCuentaRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Test
    void debePersistirFisicamenteElCodigoYLeerElEnum() {
        BanCuenta cuenta = nuevaCuentaDePrueba();

        for (TipoCuenta tipoAProbar : TipoCuenta.values()) {
            cuenta.setTipoCuenta(tipoAProbar);
            cuenta = repository.saveAndFlush(cuenta);

            String valorFisico = jdbcTemplate.queryForObject(
                    "SELECT tipo_cuenta FROM operaciones.tab_cuenta WHERE id_cuenta = ?",
                    String.class,
                    cuenta.getId());

            assertThat(valorFisico).isEqualTo(tipoAProbar.name());
            Long cuentaId = cuenta.getId();
            entityManager.clear();
            cuenta = repository.findById(cuentaId).orElseThrow();
            assertThat(cuenta.getTipoCuenta()).isEqualTo(tipoAProbar);
        }
    }

    private BanCuenta nuevaCuentaDePrueba() {
        BanCuenta cuenta = new BanCuenta();
        cuenta.setNumeroCuenta("9960436083");
        cuenta.setTipoCuenta(TipoCuenta.AHO);
        cuenta.setSaldoInicial(new BigDecimal("10.00"));
        cuenta.setSaldoDisponible(new BigDecimal("10.00"));
        cuenta.setEstado(true);
        cuenta.setIdentificacionCliente("9960436083");
        cuenta.setClienteId(1L);
        return cuenta;
    }
}
