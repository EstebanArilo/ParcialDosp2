package uqvirutal.edu.co.parcialdosp2.model;


public class ServicioBase implements Servicio {

    private TipoServicio tipo;
    private String descripcion;

    public ServicioBase(TipoServicio tipo, String descripcion) {
        if (tipo == null) {
            throw new IllegalArgumentException(
                    "El tipo de servicio es obligatorio"
            );
        }

        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public TipoServicio getTipo() {
        return tipo;
    }

    public void setTipo(TipoServicio tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException(
                    "El tipo de servicio es obligatorio"
            );
        }

        this.tipo = tipo;
    }

    @Override
    public String getNombre() {
        return tipo.name().replace('_', ' ');
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public double calcularCosto() {
        return tipo.getCostoBase();
    }
}
