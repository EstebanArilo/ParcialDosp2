package uqvirutal.edu.co.parcialdosp2.model;

/**
 * Servicio de instalación y configuración de redes de datos. Es de la especialidad
 * REDES y siempre necesita visita de un técnico.
 */

public class InstalacionRedes extends ServicioBase {

    public InstalacionRedes(String codigo) {
        super(
        );
    }

    @Override public Especialidad getEspecialidad() {
        return Especialidad.REDES;
    }

    @Override
    public String getCondiciones() {
        return "Requiere visita técnica para el cableado y la configuración de los equipos de red.";
    }

    @Override public boolean requierePresencialPorNaturaleza() {
        return true;
    }
}