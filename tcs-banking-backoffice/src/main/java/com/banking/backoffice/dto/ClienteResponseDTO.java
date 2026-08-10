package com.banking.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteResponseDTO extends ResponseDTO {

    private ClienteDetalleResponseDTO cliente;
}
