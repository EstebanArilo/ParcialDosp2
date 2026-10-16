package uqvirutal.edu.co.parcialdosp2.model;

public class MantenimientoCorrectivo extends ServicioBase {

    public MantenimientoCorrectivo(String codigo) {
        super(codigo, TipoServicio.MANTENIMIENTO_CORRECTIVO,
                "Diagnóstico y reparación de fallas en equipos de cómputo.");
    }

    @Override public Especialidad getEspecialidad() { return Especialidad.MANTENIMIENTO_EQUIPOS; }

    @Override
    public String getCondiciones() {
        return "Incluye diagnóstico; los repuestos se cotizan aparte.";
    }

    @Override public boolean requierePresencialPorNaturaleza() { return true; }
}