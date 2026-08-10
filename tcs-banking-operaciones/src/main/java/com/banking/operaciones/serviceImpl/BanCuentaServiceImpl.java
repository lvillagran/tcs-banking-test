package com.banking.operaciones.serviceImpl;

import com.banking.core.infraestrutura.util.ServerIpAddressResolver;
import com.banking.operaciones.client.ClienteClient;
import com.banking.operaciones.client.ClienteResponseDTO;
import com.banking.operaciones.dto.CuentaRequestDTO;
import com.banking.operaciones.exception.ClienteInactivoException;
import com.banking.operaciones.exception.ClienteNoEncontradoException;
import com.banking.operaciones.exception.SolicitudInvalidaException;
import com.banking.operaciones.model.BanCuenta;
import com.banking.operaciones.repository.BanCuentaRepository;
import com.banking.operaciones.servicioInterface.BanCuentaServiceInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class BanCuentaServiceImpl implements BanCuentaServiceInterface {

        private static final SecureRandom RANDOM = new SecureRandom();
        private final BanCuentaRepository banCuentaRepository;
        private final ClienteClient clienteClient;

        public BanCuentaServiceImpl(BanCuentaRepository banCuentaRepository, ClienteClient clienteClient) {
            this.banCuentaRepository = banCuentaRepository;
            this.clienteClient = clienteClient;
        }

        @Override
        @Transactional
        public BanCuenta crearCuenta(CuentaRequestDTO request) {
            validarRequest(request);
            String identificacion = request.getIdentificacionCliente().trim();
            ClienteResponseDTO cliente = clienteClient.buscarPorIdentificacion(identificacion);
            if (cliente.id() == null) {
                throw new ClienteNoEncontradoException(identificacion);
            }
            if (!Boolean.TRUE.equals(cliente.estado())) {
                throw new ClienteInactivoException(identificacion);
            }

            BigDecimal saldoInicial = request.getSaldoInicial();
            BanCuenta cuenta = new BanCuenta();
            cuenta.setNumeroCuenta(generarNumeroCuenta());
            cuenta.setTipoCuenta(request.getTipoCuenta());
            cuenta.setSaldoInicial(saldoInicial);
            cuenta.setSaldoDisponible(saldoInicial);
            cuenta.setEstado(true);
            cuenta.setIdentificacionCliente(identificacion);
            cuenta.setClienteId(cliente.id());
            cuenta.setFechaRegistro(new Date());
            cuenta.setObservacion("CREACION CUENTA");
            cuenta.setIp(ServerIpAddressResolver.resolve());
            return banCuentaRepository.save(cuenta);
        }

        private void validarRequest(CuentaRequestDTO request) {
            if (request == null || request.getIdentificacionCliente() == null
                    || request.getIdentificacionCliente().isBlank()) {
                throw new SolicitudInvalidaException("La identificación del cliente es obligatoria.");
            }
            if (request.getTipoCuenta() == null) {
                throw new SolicitudInvalidaException("El tipo de cuenta es obligatorio.");
            }
            if (request.getSaldoInicial() == null || request.getSaldoInicial().compareTo(BigDecimal.ZERO) < 0) {
                throw new SolicitudInvalidaException("El saldo inicial debe ser mayor o igual a cero.");
            }
        }

        private String generarNumeroCuenta() {
            StringBuilder numeroCuenta = new StringBuilder(10);
            for (int i = 0; i < 10; i++) {
                numeroCuenta.append(RANDOM.nextInt(10));
            }
            return numeroCuenta.toString();
        }

        @Override
        public Optional<BanCuenta> findByNumeroCuenta(String numeroCuenta) {
            return banCuentaRepository.findByNumeroCuenta(numeroCuenta.trim());
        }

        @Override
        public List<BanCuenta> findAll() {
            return banCuentaRepository.findAll();
        }

        @Override
        public void eliminarCuentaPorNumeroCuenta(String numeroCuenta) {
            Optional<BanCuenta> cuenta = banCuentaRepository.findByNumeroCuenta(numeroCuenta.trim());
            cuenta.ifPresent(banCuentaRepository::delete);
        }

        @Override
        public BanCuenta actualizarCuenta(BanCuenta cuenta) {
            return banCuentaRepository.save(cuenta);
        }
    }
