package uqvirutal.edu.co.parcialdosp2.model;


public class ServicioPersonalizado extends Complemento {

    private String nombreComplemento;
    private String descripcionComplemento;
    private double costoAdicional;

    public ServicioPersonalizado(
            Servicio servicio,
            String nombreComplemento,
            String descripcionComplemento,
            double costoAdicional) {

        super(servicio);

        this.nombreComplemento = nombreComplemento;
        this.descripcionComplemento = descripcionComplemento;
        setCostoAdicional(costoAdicional);
    }

    public String getNombreComplemento() {
        return nombreComplemento;
    }

    public void setNombreComplemento(String nombreComplemento) {
        this.nombreComplemento = nombreComplemento;
    }

    public String getDescripcionComplemento() {
        return descripcionComplemento;
    }

    public void setDescripcionComplemento(
            String descripcionComplemento) {
        this.descripcionComplemento = descripcionComplemento;
    }

    public double getCostoAdicional() {
        return costoAdicional;
    }

    public void setCostoAdicional(double costoAdicional) {
        if (costoAdicional < 0) {
            throw new IllegalArgumentException(
                    "El costo adicional no puede ser negativo"
            );
        }
        this.costoAdicional = costoAdicional;
    }

    @Override
    public String getNombre() {
        return servicio.getNombre() + " + " + nombreComplemento;
    }

    @Override
    public String getDescripcion() {
        return servicio.getDescripcion()
                + " | " + descripcionComplemento;
    }

    @Override
    public double calcularCosto() {
        return servicio.calcularCosto() + costoAdicional;
    }
}

