package com.banking.operaciones.client;

import com.banking.operaciones.exception.BackofficeNoDisponibleException;
import com.banking.operaciones.exception.ClienteNoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class RestClienteClient implements ClienteClient {

    private static final Logger log = LoggerFactory.getLogger(RestClienteClient.class);
    private final RestClient restClient;

    public RestClienteClient(RestClient backofficeRestClient) {
        this.restClient = backofficeRestClient;
    }

    @Override
    public ClienteResponseDTO buscarPorIdentificacion(String identificacion) {
        try {
            log.info("Consultando cliente en Backoffice para creación de cuenta");
            ClienteResponseDTO cliente = restClient.get()
                    .uri("/api/v1/mantenimiento/clientes/identificacion/{identificacion}", identificacion)
                    .retrieve()
                    .body(ClienteResponseDTO.class);
            if (cliente == null) {
                throw new BackofficeNoDisponibleException("Backoffice devolvió una respuesta vacía");
            }
            log.debug("Cliente localizado. clienteId={}", cliente.id());
            return cliente;
        } catch (HttpClientErrorException.NotFound exception) {
            throw new ClienteNoEncontradoException(identificacion);
        } catch (ResourceAccessException | RestClientResponseException exception) {
            log.error("No fue posible consultar Backoffice para crear la cuenta", exception);
            throw new BackofficeNoDisponibleException("Servicio Backoffice no disponible", exception);
        }
    }
}
