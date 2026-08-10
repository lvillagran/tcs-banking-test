package com.banking.core.infraestrutura.model;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@MappedSuperclass
public abstract class EntidadAuditable<Id extends Serializable> extends EntidadBase<Id> implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String observacion;
	protected Date fechaRegistro;
	protected Date fechaActualizacion;
	protected String auditoria;

	@Column(name = "OBSERVACION")
	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_REGISTRO")
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_ACTUALIZACION")
	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	@Column(name = "CAMPO_AUDITORIA")
	public String getAuditoria() {
		return auditoria;
	}

	public void setAuditoria(String auditoria) {
		this.auditoria = auditoria;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof EntidadAuditable)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		EntidadAuditable<Id> other = (EntidadAuditable<Id>) obj;
		if (this.getId() == null) {
			return false;
		} else if (!this.getId().equals(other.getId())) {
			return false;
		}
		return true;
	}

}
