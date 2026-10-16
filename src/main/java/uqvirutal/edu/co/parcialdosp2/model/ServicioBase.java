package uqvirutal.edu.co.parcialdosp2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio normal del catálogo. Su nombre y su costo base salen del TipoServicio, y
 * cada clase hija define su especialidad, sus condiciones y si necesita visita
 * presencial por su naturaleza.
 */

public abstract class ServicioBase implements Servicio {

    private final String codigo;
    private final TipoServicio tipo;
    private final String descripcion;

    protected ServicioBase(String codigo, TipoServicio tipo, String descripcion) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.descripcion = descripcion == null ? "" : descripcion;
    }

    public TipoServicio getTipo() {
        return tipo;
    }

    public abstract Especialidad getEspecialidad();
    public abstract String getCondiciones();

    public boolean requierePresencialPorNaturaleza() { return false; }

    @Override public String getCodigo() {
        return codigo;
    }
    @Override public String getNombre() {
        return tipo.getNombre();
    }
    @Override public String getDescripcion() {
        return descripcion;
    }
    @Override public double getCostoBase() {
        return tipo.getCostoBase();
    }
    @Override public double getIncrementos() {
        return 0;
    }

    @Override
    public List<Especialidad> getEspecialidades() {
        List<Especialidad> especialidades = new ArrayList<>();
        especialidades.add(getEspecialidad());
        return especialidades;
    }

    @Override
    public List<Especialidad> getEspecialidadesPresenciales() {
        if (requierePresencialPorNaturaleza()) {
            return getEspecialidades();
        }
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return codigo + " - " + getNombre() + " ($" + String.format("%,.0f", getCostoBase()) + ")";
    }
}