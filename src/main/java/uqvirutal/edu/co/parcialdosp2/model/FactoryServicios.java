package uqvirutal.edu.co.parcialdosp2.model;

public abstract class FactoryServicios {
    public abstract Servicio crearServicio();

    public Servicio crear() {
        return crearServicio();
    }
}