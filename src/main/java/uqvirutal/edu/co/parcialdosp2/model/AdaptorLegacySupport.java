package uqvirutal.edu.co.parcialdosp2.model;

import uqvirutal.edu.co.parcialdosp2.servicios.LegacySupport;

class AdaptadorLegacySupport implements ProveedorTecnicos {

    private final LegacySupport legacySupport;

    public AdaptadorLegacySupport(LegacySupport legacySupport) {
        this.legacySupport = legacySupport;
    }

    private int codigoLegacy(Especialidad especialidad) {
        if (especialidad == Especialidad.MANTENIMIENTO_EQUIPOS) {
            return 101;
        }
        if (especialidad == Especialidad.REDES) {
            return 102;
        }
        if (especialidad == Especialidad.RECUPERACION_DATOS) {
            return 103;
        }
        return 0;
    }

    @Override
    public String getNombre() { return "LegacySupport"; }

    @Override
    public Tecnico consultarTecnico(Especialidad especialidad) {
        int codigo = codigoLegacy(especialidad);
        if (codigo == 0) {
            return sinTecnico(especialidad);
        }
        return convertir(legacySupport.consultarDisponibilidad(codigo), especialidad);
    }

    private Tecnico convertir(String respuesta, Especialidad especialidad) {
        if (respuesta == null) {
            return sinTecnico(especialidad);
        }
        String[] partes = respuesta.split("\\|");
        if (partes.length != 3 || !partes[2].matches("\\d+(\\.\\d+)?")) {
            return sinTecnico(especialidad);
        }
        DisponibilidadTecnico disponibilidad = null;
        for (DisponibilidadTecnico valor : DisponibilidadTecnico.values()) {
            if (valor.name().equals(partes[1])) {
                disponibilidad = valor;
            }
        }
        if (disponibilidad == null) {
            return sinTecnico(especialidad);
        }
        double desplazamiento = Double.parseDouble(partes[2]);
        return new Tecnico(partes[0], especialidad, disponibilidad,
                disponibilidad == DisponibilidadTecnico.DISPONIBLE ? desplazamiento : 0);
    }

    private Tecnico sinTecnico(Especialidad especialidad) {
        return new Tecnico("SIN_TECNICO", especialidad, DisponibilidadTecnico.NO_DISPONIBLE, 0);
    }
}