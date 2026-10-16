package uqvirutal.edu.co.parcialdosp2.model;

public class FactoryRecuperacionInformacion extends FactoryServicios {
    @Override
    protected ServicioBase crearServicio(String codigo) {
        return new RecuperacionInformacion(codigo);
    }
}