package uqvirutal.edu.co.parcialdosp2.model;

import java.util.ArrayList;
import java.util.List;

public class RepositorioSolicitudes {

    private static final RepositorioSolicitudes INSTANCIA =
            new RepositorioSolicitudes();

    private List<Solicitud> solicitudes;

    private RepositorioSolicitudes() {
        solicitudes = new ArrayList<>();
    }

    public static RepositorioSolicitudes getInstancia() {
        return INSTANCIA;
    }

    public void agregarSolicitud(Solicitud solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException(
                    "La solicitud no puede ser nula");
        }

        solicitudes.add(solicitud);
    }

    public List<Solicitud> getSolicitudes() {
        return solicitudes;
    }

    public Solicitud buscarPorCodigo(int codigo) {
        for (Solicitud solicitud : solicitudes) {
            if (solicitud.getCodigo() == codigo) {
                return solicitud;
            }
        }

        return null;
    }

    public boolean eliminarSolicitud(int codigo) {
        return solicitudes.removeIf(
                solicitud -> solicitud.getCodigo() == codigo);
    }
}
