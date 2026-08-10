package com.banking.operaciones.serviceImpl;

import com.banking.operaciones.model.BanCuenta;
import com.banking.operaciones.model.BanMovimientos;
import com.banking.operaciones.dto.MovimientoRequestDTO;
import com.banking.operaciones.exception.CuentaNoEncontradaException;
import com.banking.operaciones.exception.SaldoNoDisponibleException;
import com.banking.operaciones.exception.SolicitudInvalidaException;
import com.banking.operaciones.model.enums.TipoMovimiento;
import com.banking.operaciones.repository.BanCuentaRepository;
import com.banking.operaciones.repository.BanMovimientoRepository;
import com.banking.operaciones.servicioInterface.BanMovimientoServiceInterface;
import com.banking.core.infraestrutura.util.ServerIpAddressResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BanMovimientoServiceImpl implements BanMovimientoServiceInterface {

    private final BanMovimientoRepository banMovimientosRepository;
    private final BanCuentaRepository banCuentaRepository;

    public BanMovimientoServiceImpl(
            BanMovimientoRepository banMovimientosRepository,
            BanCuentaRepository banCuentaRepository) {
        this.banMovimientosRepository = banMovimientosRepository;
        this.banCuentaRepository = banCuentaRepository;
    }

    @Transactional
    public MovimientoCreadoResultado procesarMovimiento(MovimientoRequestDTO request) {
        validarRequest(request);
        String numeroCuenta = request.getNumeroCuenta().trim();
        BanCuenta cuenta = banCuentaRepository.findByNumeroCuentaForUpdate(numeroCuenta)
                .orElseThrow(() -> new CuentaNoEncontradaException(numeroCuenta));

        BigDecimal saldoAnterior = cuenta.getSaldoDisponible();
        BigDecimal nuevoSaldo = request.getTipoMovimiento() == TipoMovimiento.DEP
                ? saldoAnterior.add(request.getValor())
                : saldoAnterior.subtract(request.getValor());

        if (request.getTipoMovimiento() == TipoMovimiento.RET
                && nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoNoDisponibleException();
        }

        BanMovimientos movimiento = crearEntidadMovimiento(request, cuenta, numeroCuenta, nuevoSaldo);
        cuenta.setSaldoDisponible(nuevoSaldo);
        BanMovimientos movimientoCreado = banMovimientosRepository.save(movimiento);
        banCuentaRepository.save(cuenta);

        return new MovimientoCreadoResultado(movimientoCreado, cuenta, saldoAnterior);
    }

    private void validarRequest(MovimientoRequestDTO request) {
        if (request == null || request.getNumeroCuenta() == null || request.getNumeroCuenta().isBlank()) {
            throw new SolicitudInvalidaException("El número de cuenta es obligatorio.");
        }
        if (request.getTipoMovimiento() == null) {
            throw new SolicitudInvalidaException("El tipo de movimiento es obligatorio.");
        }
        if (request.getValor() == null || request.getValor().compareTo(BigDecimal.ZERO) == 0) {
            throw new SolicitudInvalidaException("El valor del movimiento no puede ser nulo o cero.");
        }
    }

    private BanMovimientos crearEntidadMovimiento(
            MovimientoRequestDTO request,
            BanCuenta cuenta,
            String numeroCuenta,
            BigDecimal nuevoSaldo) {
        BanMovimientos movimiento = new BanMovimientos();
        movimiento.setFechaMovimiento(new Date());
        movimiento.setMovimiento(request.getTipoMovimiento().getDescripcion() + " valor " + request.getValor());
        movimiento.setTipoMovimiento(request.getTipoMovimiento());
        movimiento.setValor(request.getValor());
        movimiento.setCuenta(cuenta);
        movimiento.setFechaRegistro(new Date());
        movimiento.setEstado(true);
        movimiento.setObservacion("CREACION " + request.getTipoMovimiento().getDescripcion().toUpperCase());
        movimiento.setNumeroCuenta(numeroCuenta.toUpperCase());
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setIp(ServerIpAddressResolver.resolve());
        return movimiento;
    }

    @Override
    public List<BanMovimientos> findAll() {
        return banMovimientosRepository.findAll();
    }

    @Override
    public Optional<BanMovimientos> findById(Long idMovimiento) {
        return banMovimientosRepository.findById(idMovimiento);
    }

    @Override
    public void eliminarMovimiento(BanMovimientos movimiento) {
        banMovimientosRepository.delete(movimiento);
    }

}
