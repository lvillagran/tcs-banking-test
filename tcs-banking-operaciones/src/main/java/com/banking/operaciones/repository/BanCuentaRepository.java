package com.banking.operaciones.repository;

import com.banking.operaciones.model.BanCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;

import java.util.Optional;
import java.util.List;

@Repository
public interface BanCuentaRepository extends JpaRepository<BanCuenta, Long> {

    Optional<BanCuenta> findByNumeroCuenta(String numeroCuenta);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT cuenta FROM BanCuenta cuenta WHERE cuenta.numeroCuenta = :numeroCuenta")
    Optional<BanCuenta> findByNumeroCuentaForUpdate(@Param("numeroCuenta") String numeroCuenta);

    List<BanCuenta> findByIdentificacionClienteOrderByNumeroCuentaAsc(String identificacionCliente);

}
