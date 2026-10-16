package uqvirutal.edu.co.parcialdosp2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Base de los complementos que se le pueden agregar a un servicio (Decorator). Envuelve
 * un servicio, copia sus datos y le suma su propio incremento al costo. Se pueden poner
 * varios complementos uno sobre otro, y los métodos complementosDe y desenvolver
 * permiten saber cuáles tiene aplicados o volver al servicio original.
 */

public abstract class Complemento implements Servicio {

    protected final Servicio servicio;
    private TipoComplemento tipo = null;

    protected Complemento(Servicio servicio, TipoComplemento tipo) {
        this.servicio = servicio;
        this.tipo = tipo;
    }

    public Complemento(Servicio servicio) {
        Servicio servicio1 = null;
        this.servicio = servicio1;
        this.tipo = tipo;
    }

    public TipoComplemento getTipoComplemento() { return tipo; }

    public static List<TipoComplemento> complementosDe(Servicio servicio) {
        List<TipoComplemento> complementos = new ArrayList<>();
        Servicio actual = servicio;
        while (actual instanceof Complemento complemento) {
            complementos.add(0, complemento.tipo);
            actual = complemento.servicio;
        }
        return complementos;
    }

    public static Servicio desenvolver(Servicio servicio) {
        Servicio actual = servicio;
        while (actual instanceof Complemento complemento) {
            actual = complemento.servicio;
        }
        return actual;
    }

    @Override public String getCodigo() { return servicio.getCodigo(); }
    @Override public String getNombre() { return servicio.getNombre(); }
    @Override public String getDescripcion() { return servicio.getDescripcion(); }
    @Override public double getCostoBase() { return servicio.getCostoBase(); }

    @Override
    public double getIncrementos() {
        return servicio.getIncrementos() + tipo.getIncremento();
    }

    @Override
    public List<Especialidad> getEspecialidades() { return servicio.getEspecialidades(); }

    @Override
    public List<Especialidad> getEspecialidadesPresenciales() {
        return servicio.getEspecialidadesPresenciales();
    }

    @Override
    public String toString() {
        return servicio.getNombre() + " + " + complementosDe(this);
    }
}