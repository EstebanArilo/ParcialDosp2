package uqvirutal.edu.co.parcialdosp2.model;

/** Metodo que crea servicios de mantenimiento preventivo. */

public class FactoryMantenimientoPreventivo extends FactoryServicios {
    @Override
    protected ServicioBase crearServicio(String codigo) {
        return new MantenimientoPreventivo(codigo);
    }
}