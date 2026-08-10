package com.banking.operaciones.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CuentaResponseDTO extends ResponseDTO {

    private CuentaDetalleResponseDTO cuenta;
}
