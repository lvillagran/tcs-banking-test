package com.banking.backoffice.model;

import java.io.Serializable;
import com.banking.core.infraestrutura.model.BaseEntidadAuditoria;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

    @MappedSuperclass
    public abstract class BanPersona extends BaseEntidadAuditoria implements Serializable {

            private static final long serialVersionUID = 1L;

            @Column(name = "NOMBRE")
            protected String nombre;

            @Column(name = "GENERO", length = 1)
            protected String genero;

            @Column(name = "EDAD")
            protected String edad;

            @Column(name = "IDENTIFICACION")
            protected String identificacion;

            @Column(name = "DIRECCION")
            protected String direccion;

            @Column(name = "TELEFONO")
            protected String telefono;

            public BanPersona() {}

            public BanPersona(String nombre, String genero, String edad,
                              String identificacion, String direccion, String telefono) {
                this.nombre = nombre;
                this.genero = genero;
                this.edad = edad;
                this.identificacion = identificacion;
                this.direccion = direccion;
                this.telefono = telefono;
            }

            public String getNombre() { return nombre; }
            public void setNombre(String nombre) { this.nombre = nombre; }

            public String getGenero() { return genero; }
            public void setGenero(String genero) { this.genero = genero; }

            public String getEdad() { return edad; }
            public void setEdad(String edad) { this.edad = edad; }

            public String getIdentificacion() { return identificacion; }
            public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

            public String getDireccion() { return direccion; }
            public void setDireccion(String direccion) { this.direccion = direccion; }

            public String getTelefono() { return telefono; }
            public void setTelefono(String telefono) { this.telefono = telefono; }
        }
