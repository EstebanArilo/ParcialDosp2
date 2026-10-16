package uqvirutal.edu.co.parcialdosp2.model;

/** Nivel de prioridad que se le puede dar a una solicitud: baja, media o alta. */

public enum PrioridadSolicitud {
    BAJA("Baja"), MEDIA("Media"), ALTA("Alta");

    private final String nombre;

    PrioridadSolicitud(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() { return nombre; }
}