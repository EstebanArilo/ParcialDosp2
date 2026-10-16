package uqvirutal.edu.co.parcialdosp2.controller;

import uqvirutal.edu.co.parcialdosp2.model.*;
import java.util.ArrayList;
import java.util.List;

public class PaqueteController {

    private final TechSolutions techSolutions;

    public PaqueteController() {
        this.techSolutions = TechSolutions.getInstancia();
    }

    public PaqueteTecnologico crearPaquete(String codigo, String nombre, String descripcion) {
        return new PaqueteTecnologico(codigo, nombre, descripcion);
    }

    public boolean agregarServicioAPaquete(PaqueteTecnologico paquete, Servicio servicio) {
        if (paquete != null && servicio != null) {
            var resultado = paquete.agregarComponente(servicio);

            return true;
        }
        return false;
    }

    public List<Servicio> obtenerServiciosDelPaquete(PaqueteTecnologico paquete) {
        if (paquete != null) {
            return paquete.getComponentes();
        }
        return new ArrayList<>();
    }
}