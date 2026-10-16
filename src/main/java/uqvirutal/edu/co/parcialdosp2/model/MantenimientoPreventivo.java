package uqvirutal.edu.co.parcialdosp2.model;

/**
 * Servicio de mantenimiento preventivo: revisión periódica de equipos para evitar fallas.
 * Es de la especialidad MANTENIMIENTO_EQUIPOS y requiere visita de un técnico.
 */

public class MantenimientoPreventivo extends ServicioBase {

    public MantenimientoPreventivo(String codigo) {
        super(codigo, TipoServicio.MANTENIMIENTO_PREVENTIVO,
                "Revisión periódica de equipos para evitar fallas.");
    }

    @Override public Especialidad getEspecialidad() { return Especialidad.MANTENIMIENTO_EQUIPOS; }

    @Override
    public String getCondiciones() {
        return "Incluye limpieza y revisión general del equipo.";
    }

    @Override public boolean requierePresencialPorNaturaleza() {
        return true;
    }
}