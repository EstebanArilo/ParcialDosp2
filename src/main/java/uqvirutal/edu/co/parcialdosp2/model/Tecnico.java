package uqvirutal.edu.co.parcialdosp2.model;

/**
 * Técnico que devuelve el proveedor para una especialidad. Tiene su id, su
 * especialidad, si está disponible o no y lo que cuesta el desplazamiento.
 */

public class Tecnico {

    private final String id;
    private final Especialidad especialidad;
    private final DisponibilidadTecnico disponibilidad;
    private final double costoDesplazamiento;

    public Tecnico(String id, Especialidad especialidad, DisponibilidadTecnico disponibilidad,
                   double costoDesplazamiento) {
        this.id = id;
        this.especialidad = especialidad;
        this.disponibilidad = disponibilidad;
        this.costoDesplazamiento = costoDesplazamiento;
    }

    public String getId() {
        return id;
    }
    public Especialidad getEspecialidad() {
        return especialidad;
    }
    public DisponibilidadTecnico getDisponibilidad() {
        return disponibilidad;
    }
    public boolean isDisponible() {
        return disponibilidad == DisponibilidadTecnico.DISPONIBLE;
    }
    public double getCostoDesplazamiento() {
        return costoDesplazamiento;
    }

    @Override
    public String toString() {
        return isDisponible()
                ? id + " (" + especialidad + ", desplazamiento $" + String.format("%,.0f", costoDesplazamiento) + ")"
                : "Sin técnico disponible para " + especialidad;
    }
}
