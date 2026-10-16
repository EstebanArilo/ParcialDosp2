package uqvirutal.edu.co.parcialdosp2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Guarda las solicitudes en memoria. Permite agregarlas, buscarlas por código y
 * eliminarlas. Es un Singleton, así que todo el programa comparte la misma lista.
 */
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

    public Solicitud buscarPorCodigo(String codigo) {
        for (Solicitud solicitud : solicitudes) {
            if (solicitud.getCodigo().equals(codigo)) {
                return solicitud;
            }
        }

        return null;
    }

    public boolean eliminarSolicitud(String codigo) {
        return solicitudes.removeIf(
                solicitud -> solicitud.getCodigo().equals(codigo));
    }
}