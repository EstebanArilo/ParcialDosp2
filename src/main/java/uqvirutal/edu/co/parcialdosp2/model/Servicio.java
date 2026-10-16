package uqvirutal.edu.co.parcialdosp2.model;

import java.util.List;

public interface Servicio {

    String getCodigo();
    String getNombre();
    String getDescripcion();

    double getCostoBase();
    double getIncrementos();

    default double calcularCosto() {
        return getCostoBase() + getIncrementos();
    }

    List<Especialidad> getEspecialidades();
    List<Especialidad> getEspecialidadesPresenciales();

    default boolean requierePresencial() {
        return !getEspecialidadesPresenciales().isEmpty();
    }
}