package uqvirutal.edu.co.parcialdosp2.model;

public class InstalacionRedes extends ServicioBase {

    public InstalacionRedes(String codigo) {
        super(codigo, TipoServicio.INSTALACION_DE_REDES,
                "Instalación y configuración de redes de datos para la empresa.");
    }

    @Override public Especialidad getEspecialidad() { return Especialidad.REDES; }

    @Override
    public String getCondiciones() {
        return "Requiere visita técnica para el cableado y la configuración de los equipos de red.";
    }

    @Override public boolean requierePresencialPorNaturaleza() { return true; }
}