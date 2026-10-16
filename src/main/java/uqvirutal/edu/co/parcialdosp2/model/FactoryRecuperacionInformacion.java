package uqvirutal.edu.co.parcialdosp2.model;

/** Metodo que crea servicios de recuperación de información. */

public class FactoryRecuperacionInformacion extends FactoryServicios {
    @Override
    protected ServicioBase crearServicio(String codigo) {
        return new RecuperacionInformacion(codigo);
    }
}