package com.banking.operaciones.servicioInterface;

import com.banking.operaciones.dto.CuentaRequestDTO;
import com.banking.operaciones.model.BanCuenta;

import java.util.List;
import java.util.Optional;


public interface BanCuentaServiceInterface {

        // Método para crear una nueva cuenta
        BanCuenta crearCuenta(CuentaRequestDTO request);

        // Método para buscar una cuenta por su número de cuenta
        Optional<BanCuenta> findByNumeroCuenta(String numeroCuenta);

        // Método para obtener todas las cuentas
        List<BanCuenta> findAll();

        // Método para eliminar una cuenta por número de cuenta
        void eliminarCuentaPorNumeroCuenta(String numeroCuenta);

        // Método para actualizar una cuenta existente
        BanCuenta actualizarCuenta(BanCuenta cuenta);
    }
