package uqvirutal.edu.co.parcialdosp2.model;

import java.util.List;

public enum Especialidad {
    MANTENIMIENTO_EQUIPOS("Mantenimiento de equipos"),
    REDES("Redes"),
    RECUPERACION_DATOS("Recuperación de datos");

    private final String nombre;

    Especialidad(String nombre) { this.nombre = nombre; }

    public String getNombre() { return nombre; }

    public static void agregarSinRepetir(List<Especialidad> destino, List<Especialidad> origen) {
        for (Especialidad especialidad : origen) {
            if (!destino.contains(especialidad)) {
                destino.add(especialidad);
            }
        }
    }

    @Override
    public String toString() { return nombre; }
}