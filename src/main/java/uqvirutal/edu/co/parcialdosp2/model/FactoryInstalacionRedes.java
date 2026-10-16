package uqvirutal.edu.co.parcialdosp2.model;

/** Metodo que crea servicios de instalación de redes. */

public class FactoryInstalacionRedes extends FactoryServicios {
    @Override
    protected ServicioBase crearServicio(String codigo) {
        return new InstalacionRedes(codigo);
    }
}