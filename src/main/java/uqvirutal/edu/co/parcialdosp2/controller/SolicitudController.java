package uqvirutal.edu.co.parcialdosp2.controller;

import uqvirutal.edu.co.parcialdosp2.model.*;
import java.util.List;
import java.util.Map;

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

    public <E> boolean registrarSolicitud(String laura, List<E> es, Object o, Object o1, Object o2) {
        return false;
    }

    public Map<Object, Object> consultarSolicitudes() {
        return Map.of();
    }

    public Map<Object, Object> consultarElementosContratables() {
        return Map.of();
    }
}