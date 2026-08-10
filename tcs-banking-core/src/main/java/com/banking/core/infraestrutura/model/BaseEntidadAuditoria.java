package com.banking.core.infraestrutura.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.io.Serializable;
import java.util.Date;

/** Campos de auditoría comunes, sin reglas pertenecientes a un dominio bancario. */
@MappedSuperclass
public abstract class BaseEntidadAuditoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_ACTUALIZACION")
    private Date fechaActualizacion;

    @Column(name = "OBSERVACION")
    private String observacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_REGISTRO")
    private Date fechaRegistro;

    @Column(name = "IP")
    private String ip;

    public Date getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(Date fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
}
