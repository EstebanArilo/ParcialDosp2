package uqvirutal.edu.co.parcialdosp2.model;


public abstract class Complemento implements Servicio {

    protected Servicio servicio;

    public Complemento(Servicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException(
                    "El servicio no puede ser nulo"
            );
        }
        this.servicio = servicio;
    }

    @Override
    public String getNombre() {
        return servicio.getNombre();
    }

    @Override
    public String getDescripcion() {
        return servicio.getDescripcion();
    }

    @Override
    public double calcularCosto() {
        return servicio.calcularCosto();
    }
}
