package uqvirutal.edu.co.parcialdosp2.model;

import java.util.List;

/**
 * Todo lo que se puede contratar: un servicio del catálogo, un servicio con
 * complementos o un paquete. Define código, nombre, descripción y costos, y calcula el
 * costo final sumando el costo base con los incrementos. También indica si hace falta
 * atención presencial.
 */

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