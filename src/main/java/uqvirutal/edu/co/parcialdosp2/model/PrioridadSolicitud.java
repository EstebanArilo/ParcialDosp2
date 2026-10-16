package uqvirutal.edu.co.parcialdosp2.model;

public enum PrioridadSolicitud {
    BAJA("Baja"), MEDIA("Media"), ALTA("Alta");

    private final String nombre;

    PrioridadSolicitud(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() { return nombre; }
}