package uqvirutal.edu.co.parcialdosp2.model;

/** Metodo que crea servicios de mantenimiento correctivo. */

public class FactoryMantenimientoCorrectivo extends FactoryServicios {
    @Override
    protected ServicioBase crearServicio(String codigo) {
        return new MantenimientoCorrectivo(codigo);
    }
}