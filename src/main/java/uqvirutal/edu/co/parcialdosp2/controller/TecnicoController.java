package uqvirutal.edu.co.parcialdosp2.controller;

import uqvirutal.edu.co.parcialdosp2.model.*;
import java.util.List;

public class TecnicoController {

    private final RepositorioSolicitudes repositorioSolicitudes;
    private final TechSolutions techSolutions;

    public TecnicoController() {
        this.repositorioSolicitudes = RepositorioSolicitudes.getInstancia();
        this.techSolutions = TechSolutions.getInstancia();
    }

    // Consultar un técnico a través del proveedor según una especialidad requerida
    public Tecnico consultarTecnicoPorEspecialidad(ProveedorTecnicos proveedor, Especialidad especialidad) {
        if (proveedor != null && especialidad != null) {
            return proveedor.consultarTecnico(especialidad);
        }
        return null;
    }

    // Calcular el costo total de una solicitud (servicios + desplazamiento)
    public double calcularCostoTotalSolicitud(Solicitud solicitud) {
        if (solicitud != null) {
            return solicitud.calcularCostoTotal();
        }
        return 0.0;
    }

    // Obtener las especialidades presenciales requeridas por una solicitud
    public List<Especialidad> obtenerEspecialidadesRequeridas(Solicitud solicitud) {
        if (solicitud != null) {
            return solicitud.getEspecialidadesPresenciales();
        }
        return List.of();
    }

    public boolean hayTecnicosDisponibles(Solicitud solicitud) {
        return techSolutions.hayTecnicosDisponibles(solicitud);
    }

    public double calcularCostoTotal(Solicitud solicitud) {
        return techSolutions.calcularCostoTotal(solicitud);
    }

    public Resultado<Solicitud> confirmarSolicitud(Solicitud solicitud) {
        return techSolutions.confirmarSolicitud(solicitud);
    }
}