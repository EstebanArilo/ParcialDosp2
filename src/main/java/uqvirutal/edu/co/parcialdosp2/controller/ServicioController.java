package uqvirutal.edu.co.parcialdosp2.controller;

import uqvirutal.edu.co.parcialdosp2.model.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioController {

    private final TechSolutions techSolutions;

    public ServicioController() {
        this.techSolutions = TechSolutions.getInstancia();
    }

    public List<Servicio> obtenerServiciosDisponibles() {
        List<Servicio> lista = new ArrayList<>();
        TechSolutions techSolutions = TechSolutions.getInstancia();

        lista.addAll(techSolutions.consultarCatalogo());

        return lista;
    }

    public Servicio agregarComplementoAServicio(Servicio servicioOriginal, String nombreComplemento, String descripcionComplemento, double costoAdicional) {
        return new ServicioPersonalizado(servicioOriginal, nombreComplemento, descripcionComplemento, costoAdicional);
    }
}