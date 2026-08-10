package com.banking.backoffice.model;

public enum TipoIdentificacion {

    CED("Cédula", 10),
    RUC("Registro Único de Contribuyentes", 13);

    private final String descripcion;
    private final int longitud;

    TipoIdentificacion(String descripcion, int longitud) {
        this.descripcion = descripcion;
        this.longitud = longitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getLongitud() {
        return longitud;
    }

    public boolean esIdentificacionValida(String identificacion) {
        return identificacion != null
                && identificacion.matches("\\d{" + longitud + "}");
    }
}
