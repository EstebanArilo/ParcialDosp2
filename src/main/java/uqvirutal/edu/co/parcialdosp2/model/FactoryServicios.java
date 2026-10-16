package uqvirutal.edu.co.parcialdosp2.model;

public abstract class FactoryServicios {

    protected abstract ServicioBase crearServicio(String codigo);

    public ServicioBase crear(String codigo) {
        return crearServicio(codigo);
    }
}
