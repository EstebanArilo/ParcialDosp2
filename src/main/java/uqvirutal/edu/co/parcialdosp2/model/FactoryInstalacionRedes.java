package uqvirutal.edu.co.parcialdosp2.model;

public class FactoryInstalacionRedes extends FactoryServicios {
    @Override
    protected ServicioBase crearServicio(String codigo) {
        return new InstalacionRedes(codigo);
    }
}