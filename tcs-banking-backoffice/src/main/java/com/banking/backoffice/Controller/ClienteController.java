package com.banking.backoffice.Controller;

import com.banking.backoffice.dto.ClienteRequestDTO;
import com.banking.backoffice.dto.ClienteResponseDTO;
import com.banking.backoffice.dto.ClienteConsultaDTO;
import com.banking.backoffice.dto.ClientesResponseDTO;
import com.banking.backoffice.dto.MensajeResponseDTO;
import com.banking.backoffice.mapper.ClienteResponseMapper;
import com.banking.core.infraestrutura.exceptions.GenericException;
import com.banking.backoffice.model.BanCliente;
import com.banking.backoffice.serviceImpl.BanClienteServiceImpl;
import com.banking.core.infraestrutura.util.ServerIpAddressResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.web.bind.annotation.*;

import static com.banking.backoffice.util.MensajesSistema.*;

@RestController
@RequestMapping("/api/v1/mantenimiento/clientes")
public class ClienteController {

    @Autowired
    private BanClienteServiceImpl banClienteService;

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);


    /** Registrar cliente */
    @PostMapping("/crear")
    public ResponseEntity<ClienteResponseDTO> createCliente(@RequestBody ClienteRequestDTO request) {
        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO();
        BanCliente cliente = new BanCliente();

        try {
            if (request.getIdentificacion() == null || request.getIdentificacion().trim().isEmpty()) {
                clienteResponseDTO.setMensaje("La identificación es requerida.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(clienteResponseDTO);
            }

            if (request.getTipoIdentificacion() == null) {
                clienteResponseDTO.setFechaEjecucion(new Date());
                clienteResponseDTO.setMensaje("El tipo de identificación es requerido. Valores permitidos: CED, RUC.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(clienteResponseDTO);
            }

            String identificacion = request.getIdentificacion().trim();
            if (!request.getTipoIdentificacion().esIdentificacionValida(identificacion)) {
                clienteResponseDTO.setFechaEjecucion(new Date());
                clienteResponseDTO.setMensaje("La identificación tipo "
                        + request.getTipoIdentificacion()
                        + " debe contener exactamente "
                        + request.getTipoIdentificacion().getLongitud()
                        + " dígitos.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(clienteResponseDTO);
            }

            Optional<BanCliente> clienteExistente = banClienteService.findByIdentificacion(identificacion);

            if (clienteExistente.isPresent()) {
                clienteResponseDTO.setFechaEjecucion(new Date());
                clienteResponseDTO.setMensaje(CLIENTE_EXISTE);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(clienteResponseDTO); // 409 Conflict
            }else{
            cliente.setIdentificacion(identificacion);
            cliente.setTipoIdentificacion(request.getTipoIdentificacion());
            cliente.setNombre(request.getNombre().trim().toUpperCase());
            cliente.setGenero(request.getGenero().trim().toUpperCase());
            cliente.setEdad(request.getEdad().trim());
            cliente.setUsuario(identificacion);
            cliente.setDireccion(request.getDireccion().trim().toUpperCase());
            cliente.setTelefono(request.getTelefono().trim());
            cliente.setContrasena(generarContrasena());
            cliente.setEstado(true);
            cliente.setObservacion(REGISTRO_CLIENTE);
            cliente.setIp(ServerIpAddressResolver.resolve());
            cliente.setFechaRegistro(new Date());

            BanCliente clienteCreado = banClienteService.crearCliente(cliente);

            /** Validamos si el cliente fue creado correctamente */
            if (clienteCreado != null && clienteCreado.getIdentificacion() != null) {
                clienteResponseDTO.setMensaje(TRANSACCION_EXITOSA);
                clienteResponseDTO.setFechaEjecucion(new Date());
                clienteResponseDTO.setCliente(ClienteResponseMapper.toDetalle(clienteCreado));
                return ResponseEntity.status(HttpStatus.CREATED).body(clienteResponseDTO);
            } else {
                clienteResponseDTO.setMensaje("Error al registrar el cliente");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(clienteResponseDTO);
              }
            }

        } catch (GenericException e) {
            clienteResponseDTO.setMensaje("Error al registrar el cliente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(clienteResponseDTO);
        } catch (Exception e) {
            // Manejo de errores inesperados
            clienteResponseDTO.setMensaje("Error desconocido al registrar el cliente.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(clienteResponseDTO);
        }
    }

    /** Listar clientes registrados */
    @GetMapping("/listar")
    public ResponseEntity<ClientesResponseDTO> listarClientes() {
        List<BanCliente> clientes = banClienteService.findAll();
        if (clientes == null || clientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        ClientesResponseDTO response = new ClientesResponseDTO();
        response.setFechaEjecucion(new Date());
        response.setMensaje("Clientes consultados correctamente.");
        response.setTotal(clientes.size());
        response.setClientes(clientes.stream()
                .map(ClienteResponseMapper::toDetalle)
                .toList());
        return ResponseEntity.ok(response);
    }

    /** Eliminar cliente por numero de identificacion */
    @DeleteMapping("/eliminar/{identificacion}")
    public ResponseEntity<MensajeResponseDTO> eliminarCliente(@PathVariable String identificacion) {
        MensajeResponseDTO response = new MensajeResponseDTO();

        return banClienteService.findByIdentificacion(identificacion)
                .map(cliente -> {
                    banClienteService.eliminarClientePorIdentificacion(identificacion);
                    response.setMensaje("Cliente eliminado correctamente.");
                    response.setFechaEjecucion(new Date());
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.setMensaje("Cliente con Identificacion: " + identificacion + " no encontrado");
                    response.setFechaEjecucion(new Date());
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }


    /** Actualizar cliente por identificación */
    @PutMapping("/actualizar/{identificacion}")
    public ResponseEntity<ClienteResponseDTO> actualizarCliente(@PathVariable String identificacion, @RequestBody ClienteRequestDTO request) {

        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO();

        try {
            Optional<BanCliente> clienteOptional = banClienteService.findByIdentificacion(identificacion.trim());

            if (clienteOptional.isEmpty()) {
                clienteResponseDTO.setMensaje("Cliente con Identificacion: " + identificacion + " no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(clienteResponseDTO);
            }
            BanCliente cliente = clienteOptional.get();

            if (request.getTipoIdentificacion() == null) {
                clienteResponseDTO.setFechaEjecucion(new Date());
                clienteResponseDTO.setMensaje("El tipo de identificación es requerido. Valores permitidos: CED, RUC.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(clienteResponseDTO);
            }

            if (!request.getTipoIdentificacion().esIdentificacionValida(identificacion.trim())) {
                clienteResponseDTO.setFechaEjecucion(new Date());
                clienteResponseDTO.setMensaje("La identificación tipo "
                        + request.getTipoIdentificacion()
                        + " debe contener exactamente "
                        + request.getTipoIdentificacion().getLongitud()
                        + " dígitos.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(clienteResponseDTO);
            }

            cliente.setNombre(request.getNombre().trim().toUpperCase());
            cliente.setTipoIdentificacion(request.getTipoIdentificacion());
            cliente.setGenero(request.getGenero().trim().toUpperCase());
            cliente.setEdad(request.getEdad().trim());
            cliente.setDireccion(request.getDireccion().trim().toUpperCase());
            cliente.setTelefono(request.getTelefono().trim());
            cliente.setContrasena(request.getContrasena().trim());
            cliente.setObservacion("ACTUALIZACIÓN DE DATOS");
            cliente.setFechaActualizacion(new Date());
            cliente.setIp(ServerIpAddressResolver.resolve());

            BanCliente clienteActualizado = banClienteService.actualizarCliente(cliente);

            clienteResponseDTO.setMensaje("Cliente actualizado exitosamente");
            clienteResponseDTO.setFechaEjecucion(new Date());
            clienteResponseDTO.setCliente(ClienteResponseMapper.toDetalle(clienteActualizado));

            return ResponseEntity.ok(clienteResponseDTO);

        } catch (Exception e) {
            clienteResponseDTO.setMensaje("Error al actualizar el cliente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(clienteResponseDTO);
        }
    }


    /** Consulta interna de cliente por identificación. */
    @GetMapping("/consultarPersonaPorIdentificacion/{identificacion}")
    public ResponseEntity<ClienteResponseDTO> consultarPersonaPorIdentificacion(@PathVariable String identificacion) {
        Optional<BanCliente> personaOpt = banClienteService.findByIdentificacion(identificacion);

        if (personaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setFechaEjecucion(new Date());
        response.setMensaje("Cliente consultado correctamente.");
        response.setCliente(ClienteResponseMapper.toDetalle(personaOpt.get()));
        return ResponseEntity.ok(response);
    }

    /** Contrato seguro para integraciones entre microservicios; nunca expone la contraseña. */
    @GetMapping("/identificacion/{identificacion}")
    public ResponseEntity<ClienteConsultaDTO> consultarClientePorIdentificacion(@PathVariable String identificacion) {
        return banClienteService.findByIdentificacion(identificacion.trim())
                .map(cliente -> new ClienteConsultaDTO(
                        cliente.getId(),
                        cliente.getIdentificacion(),
                        cliente.getTipoIdentificacion(),
                        cliente.getNombre(),
                        cliente.getEstado()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


        public static String generarContrasena() {
            Random random = new Random();
            StringBuilder numeroCuenta = new StringBuilder();

            for (int i = 0; i < 5; i++) {
                numeroCuenta.append(random.nextInt(10)); // 0 al 9
            }

            return numeroCuenta.toString();
        }
}
