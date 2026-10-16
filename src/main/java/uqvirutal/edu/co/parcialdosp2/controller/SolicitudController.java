package uqvirutal.edu.co.parcialdosp2.controller;

import uqvirutal.edu.co.parcialdosp2.model.*;
import java.time.LocalDate;
import java.util.List;

public class SolicitudController {

    private final RepositorioSolicitudes repositorio;
    private final TechSolutions techSolutions;

    public SolicitudController() {
        this.repositorio = RepositorioSolicitudes.getInstancia();
        this.techSolutions = TechSolutions.getInstancia();
    }

    public boolean registrarSolicitud(String codigo, Cliente cliente, Servicio servicio,
                                      PrioridadSolicitud prioridad) {
        try {
            var resultadoBuilder = new Solicitud.Builder(codigo, cliente)
                    .agregarServicio(servicio)
                    .prioridad(prioridad)
                    .build();
            if (resultadoBuilder != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Solicitud> obtenerSolicitudes() {
        return repositorio.getSolicitudes();
    }

    public Resultado<Solicitud> registrarSolicitud(String documentoCliente, List<? extends Servicio> elementos,
                                                   PrioridadSolicitud prioridad, LocalDate fechaProgramada,
                                                   String observaciones) {
        Resultado<Solicitud.Builder> inicio = techSolutions.nuevaSolicitud(documentoCliente);
        if (!inicio.isExitoso()) {
            return Resultado.error(inicio.getMensaje());
        }
        if (elementos == null) {
            return Resultado.error("Debe seleccionar al menos un elemento a contratar.");
        }
        Solicitud.Builder builder = inicio.getValor();
        for (Servicio elemento : elementos) {
            builder.agregarServicio(elemento);
        }
        builder.prioridad(prioridad).fechaProgramada(fechaProgramada).observaciones(observaciones);
        return techSolutions.registrarSolicitud(builder);
    }

    public List<Solicitud> consultarSolicitudes() {
        return techSolutions.consultarSolicitudes();
    }

    public List<Servicio> consultarElementosContratables() {
        return techSolutions.consultarElementosContratables();
    }
}