package com.banking.backoffice.repository;

import com.banking.backoffice.model.BanCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BanClienteRepository extends JpaRepository<BanCliente, Long> {

    @Query("SELECT b FROM BanCliente b WHERE b.identificacion = :identificacion")
    Optional<BanCliente> findByIdentificacion(@Param("identificacion") String identificacion);


}
