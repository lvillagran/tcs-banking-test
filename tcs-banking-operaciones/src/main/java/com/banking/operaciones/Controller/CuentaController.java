package com.banking.operaciones.Controller;

import com.banking.operaciones.dto.CuentaRequestDTO;
import com.banking.operaciones.dto.CuentaResponseDTO;
import com.banking.operaciones.dto.CuentasResponseDTO;
import com.banking.operaciones.dto.MensajeResponseDTO;
import com.banking.operaciones.mapper.CuentaResponseMapper;
import com.banking.operaciones.model.BanCuenta;
import com.banking.operaciones.serviceImpl.BanCuentaServiceImpl;
import com.banking.core.infraestrutura.util.ServerIpAddressResolver;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/operaciones/cuentas")
public class CuentaController {

    private final BanCuentaServiceImpl cuentaService;

    public CuentaController(BanCuentaServiceImpl cuentaService) {
        this.cuentaService = cuentaService;
    }

    /** Crear cuenta bancaria */
    @PostMapping("/crear")
    public ResponseEntity<CuentaResponseDTO> crearCuenta(@Valid @RequestBody CuentaRequestDTO request) {
        CuentaResponseDTO response = new CuentaResponseDTO();

            BanCuenta cuentaCreada = cuentaService.crearCuenta(request);

            response.setCuenta(CuentaResponseMapper.toDetalle(cuentaCreada));
            response.setMensaje("Cuenta creada correctamente.");
            response.setFechaEjecucion(new Date());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    /** Listar todas las cuentas */
    @GetMapping("/listar")
    public ResponseEntity<CuentasResponseDTO> listarCuentas() {
        List<BanCuenta> cuentas = cuentaService.findAll();
        if (cuentas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CuentasResponseDTO response = new CuentasResponseDTO();
        response.setFechaEjecucion(new Date());
        response.setMensaje("Cuentas consultadas correctamente.");
        response.setTotal(cuentas.size());
        response.setCuentas(cuentas.stream()
                .map(CuentaResponseMapper::toDetalle)
                .toList());
        return ResponseEntity.ok(response);
    }

    /** Eliminar cuenta por número de cuenta */
    @DeleteMapping("/eliminar/{numeroCuenta}")
    public ResponseEntity<MensajeResponseDTO> eliminarCuenta(@PathVariable String numeroCuenta) {
        MensajeResponseDTO response = new MensajeResponseDTO();

        Optional<BanCuenta> cuentaOpt = cuentaService.findByNumeroCuenta(numeroCuenta.trim());

        if (cuentaOpt.isEmpty()) {
            response.setMensaje("Cuenta con número: " + numeroCuenta + " no encontrada.");
            response.setFechaEjecucion(new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        cuentaService.eliminarCuentaPorNumeroCuenta(numeroCuenta.trim());
        response.setMensaje("Cuenta eliminada correctamente.");
        response.setFechaEjecucion(new Date());
        return ResponseEntity.ok(response);
    }

    /** Actualizar cuenta bancaria */
    @PutMapping("/actualizar/{numeroCuenta}")
    public ResponseEntity<CuentaResponseDTO> actualizarCuenta(@PathVariable String numeroCuenta, @Valid @RequestBody CuentaRequestDTO request) {
        CuentaResponseDTO response = new CuentaResponseDTO();

        Optional<BanCuenta> cuentaOpt = cuentaService.findByNumeroCuenta(numeroCuenta.trim());

        if (cuentaOpt.isEmpty()) {
            response.setMensaje("Cuenta con número: " + numeroCuenta + " no encontrada.");
            response.setFechaEjecucion(new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        BanCuenta cuenta = cuentaOpt.get();
        cuenta.setTipoCuenta(request.getTipoCuenta());
        cuenta.setSaldoInicial(request.getSaldoInicial());
        cuenta.setEstado(request.isEstado());
        cuenta.setFechaActualizacion(new Date());
        cuenta.setIp(ServerIpAddressResolver.resolve());

        BanCuenta cuentaActualizada = cuentaService.actualizarCuenta(cuenta);

        response.setCuenta(CuentaResponseMapper.toDetalle(cuentaActualizada));
        response.setMensaje("Cuenta actualizada correctamente.");
        response.setFechaEjecucion(new Date());

        return ResponseEntity.ok(response);
    }

}
