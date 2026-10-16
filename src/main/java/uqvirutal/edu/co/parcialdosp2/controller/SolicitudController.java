package uqvirutal.edu.co.parcialdosp2.controller;

import uqvirutal.edu.co.parcialdosp2.model.*;
import java.util.List;

public class SolicitudController {

    private final RepositorioSolicitudes repositorio;

    public SolicitudController() {
        this.repositorio = RepositorioSolicitudes.getInstancia();
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
}