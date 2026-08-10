package com.banking.operaciones.servicioInterface;

import com.banking.operaciones.model.BanMovimientos;
import java.util.List;
import java.util.Optional;

public interface BanMovimientoServiceInterface {

    /** Obtener todos los movimientos */
    List<BanMovimientos> findAll();

    /** Buscar un movimiento por su identificador */
    Optional<BanMovimientos> findById(Long idMovimiento);

    /** Eliminar un único movimiento */
    void eliminarMovimiento(BanMovimientos movimiento);

}
