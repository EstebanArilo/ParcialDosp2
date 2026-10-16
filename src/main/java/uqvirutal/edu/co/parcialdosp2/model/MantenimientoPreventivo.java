package uqvirutal.edu.co.parcialdosp2.model;

public class FactoryMantenimientoPreventivo extends FactoryServicios {
    @Override
    protected ServicioBase crearServicio(String codigo) {
        return new MantenimientoPreventivo(codigo);
    }
}