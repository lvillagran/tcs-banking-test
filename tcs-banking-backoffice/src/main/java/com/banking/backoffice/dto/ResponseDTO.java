package com.banking.backoffice.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class ResponseDTO {
    private Date fechaEjecucion;
    private String mensaje;
}
