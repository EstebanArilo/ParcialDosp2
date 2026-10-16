package uqvirutal.edu.co.parcialdosp2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Paquete que agrupa varios servicios, servicios personalizados u otros paquetes
 * (Composite). Su costo es la suma del costo de sus componentes y sus especialidades
 * salen de ellos, sin repetirse. Evita que un paquete se agregue a sí mismo, directa o
 * indirectamente, y que se repita un componente.
 */

public class PaqueteTecnologico implements Servicio {

    private final String codigo;
    private String nombre;
    private String descripcion;
    private final List<Servicio> componentes = new ArrayList<>();

    public PaqueteTecnologico(String codigo, String nombre, String descripcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion == null ? "" : descripcion;
    }

    public Resultado<Void> agregarComponente(Servicio componente) {
        if (componente == null) {
            return Resultado.error("Debe seleccionar el componente a agregar.");
        }
        Servicio base = Complemento.desenvolver(componente);
        if (base == this || (base instanceof PaqueteTecnologico otro && otro.contieneA(this))) {
            return Resultado.error("No se puede agregar '" + componente.getNombre()
                    + "' al paquete '" + nombre + "': el paquete quedaría incluido dentro de sí mismo.");
        }
        if (componentes.contains(componente)) {
            return Resultado.error(
                    "'" + componente.getNombre() + "' ya hace parte del paquete '" + nombre + "'.");
        }
        componentes.add(componente);
        return Resultado.exito();
    }

    public boolean eliminarComponente(Servicio componente) {
        return componentes.remove(componente);
    }

    public boolean contieneA(PaqueteTecnologico objetivo) {
        for (Servicio componente : componentes) {
            Servicio base = Complemento.desenvolver(componente);
            if (base == objetivo || (base instanceof PaqueteTecnologico hijo && hijo.contieneA(objetivo))) {
                return true;
            }
        }
        return false;
    }

    public List<Servicio> getComponentes() {
        return new ArrayList<>(componentes);
    }

    public boolean setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return false;
        }
        this.nombre = nombre;
        return true;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion == null ? "" : descripcion;
    }

    @Override public String getCodigo() { return codigo; }
    @Override public String getNombre() { return nombre; }
    @Override public String getDescripcion() { return descripcion; }

    @Override
    public double getCostoBase() {
        double total = 0;
        for (Servicio componente : componentes) {
            total += componente.calcularCosto();
        }
        return total;
    }

    @Override public double getIncrementos() { return 0; }

    @Override
    public List<Especialidad> getEspecialidades() {
        List<Especialidad> resultado = new ArrayList<>();
        for (Servicio componente : componentes) {
            Especialidad.agregarSinRepetir(resultado, componente.getEspecialidades());
        }
        return resultado;
    }

    @Override
    public List<Especialidad> getEspecialidadesPresenciales() {
        List<Especialidad> resultado = new ArrayList<>();
        for (Servicio componente : componentes) {
            Especialidad.agregarSinRepetir(resultado, componente.getEspecialidadesPresenciales());
        }
        return resultado;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + componentes.size() + " componentes)";
    }
}