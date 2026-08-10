package com.banking.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientesResponseDTO extends ResponseDTO {

    private int total;
    private List<ClienteDetalleResponseDTO> clientes;
}
