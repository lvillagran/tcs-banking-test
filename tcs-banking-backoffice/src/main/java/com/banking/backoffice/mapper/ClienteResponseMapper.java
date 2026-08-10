package com.banking.backoffice.mapper;

import com.banking.backoffice.dto.ClienteDetalleResponseDTO;
import com.banking.backoffice.model.BanCliente;

public final class ClienteResponseMapper {

    private ClienteResponseMapper() {
    }

    public static ClienteDetalleResponseDTO toDetalle(BanCliente cliente) {
        return new ClienteDetalleResponseDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getGenero(),
                cliente.getEdad(),
                cliente.getIdentificacion(),
                cliente.getTipoIdentificacion(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getEstado());
    }
}
