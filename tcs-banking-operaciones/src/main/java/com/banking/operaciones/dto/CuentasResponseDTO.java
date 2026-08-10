package com.banking.operaciones.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CuentasResponseDTO extends ResponseDTO {

    private int total;
    private List<CuentaDetalleResponseDTO> cuentas;
}
