package com.banking.backoffice.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TAB_CLIENTE", schema = "backoffice")
public class BanCliente extends BanPersona implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String contrasena;
    private String usuario;
    private Boolean estado;
    private TipoIdentificacion tipoIdentificacion;

    // Constructor sin parámetros
    public BanCliente() {}

    public BanCliente(long id, String contrasena, String usuario, boolean estado) {
        this.id = id;
        this.contrasena = contrasena;
        this.usuario = usuario;
        this.estado = estado;
    }

    @Id
    @GeneratedValue(generator = "secTabCliente", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "secTabCliente", allocationSize = 1, initialValue = 1, sequenceName = "SEC_CLIENTE")
    @Column(name = "ID_CLIENTE")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "CONTRASENA")
    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Column(name = "ESTADO")
    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Column(name = "USUARIO")
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_IDENTIFICACION", length = 3)
    public TipoIdentificacion getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }
}
