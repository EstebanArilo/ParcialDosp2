package uqvirutal.edu.co.parcialdosp2.model;

/**
 * Servicio de mantenimiento correctivo: arreglar equipos que ya presentan fallas.
 * Es de la especialidad MANTENIMIENTO_EQUIPOS.
 */

public class MantenimientoCorrectivo extends ServicioBase {

    public MantenimientoCorrectivo(String codigo) {
        super(
        );
    }

    @Override public Especialidad getEspecialidad() { return Especialidad.MANTENIMIENTO_EQUIPOS; }

    @Override
    public String getCondiciones() {
        return "Incluye diagnóstico; los repuestos se cotizan aparte.";
    }

    @Override public boolean requierePresencialPorNaturaleza() {
        return true;
    }
}