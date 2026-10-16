package uqvirutal.edu.co.parcialdosp2.model;

/**
 * Metodo abstracto de servicios (Factory Method). Cada fábrica hija decide qué tipo
 * de servicio crea; quien la usa solo llama a crear(codigo).
 */

public abstract class FactoryServicios {

    protected abstract ServicioBase crearServicio(String codigo);

    public ServicioBase crear(String codigo) {
        return crearServicio(codigo);
    }
}
