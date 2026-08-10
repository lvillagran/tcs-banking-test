package com.banking.operaciones.repository;

import com.banking.operaciones.model.BanMovimientos;
import com.banking.operaciones.model.BanCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;


@Repository
public interface BanMovimientoRepository extends JpaRepository<BanMovimientos, Long> {

    // Buscar movimientos por cuenta ordenados por fecha descendente
    List<BanMovimientos> findByCuentaOrderByFechaMovimientoDesc(BanCuenta cuenta);


    List<BanMovimientos> findByCuentaAndFechaMovimientoGreaterThanEqualAndFechaMovimientoLessThanOrderByFechaMovimientoAsc(
            BanCuenta cuenta,
            Date fechaDesde,
            Date fechaHastaExclusiva);
}
