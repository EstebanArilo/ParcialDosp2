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

    public Resultado<PaqueteTecnologico> crearPaquete(String nombre, String descripcion) {
        return techSolutions.crearPaquete(nombre, descripcion);
    }

    public Resultado<Void> agregarComponente(PaqueteTecnologico paquete, Servicio componente) {
        return techSolutions.agregarComponente(paquete, componente);
    }

    /** Elementos que se pueden agregar al paquete: sin él mismo, sin repetidos y sin ciclos. */
    public List<Servicio> consultarComponentesDisponibles(PaqueteTecnologico paquete) {
        List<Servicio> disponibles = new ArrayList<>();
        List<Servicio> actuales = paquete.getComponentes();
        for (Servicio elemento : techSolutions.consultarElementosContratables()) {
            boolean esElMismo = elemento == paquete;
            boolean yaEsta = actuales.contains(elemento);
            boolean haceCiclo = elemento instanceof PaqueteTecnologico otro && otro.contieneA(paquete);
            if (!esElMismo && !yaEsta && !haceCiclo) {
                disponibles.add(elemento);
            }
        }
        return disponibles;
    }

    public List<Servicio> consultarComponentes(PaqueteTecnologico paquete) {
        return techSolutions.consultarComponentes(paquete);
    }
}