package uqvirutal.edu.co.parcialdosp2.model;


import java.util.ArrayList;
import java.util.List;

public class PaqueteTecnologico implements Servicio {

    private String nombre;
    private String descripcion;
    private List<Servicio> servicios;

    public PaqueteTecnologico(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.servicios = new ArrayList<>();
    }

    public void agregarServicio(Servicio servicio) {
        servicios.add(servicio);
    }

    public void eliminarServicio(Servicio servicio) {
        servicios.remove(servicio);
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public double calcularCosto() {
        double total = 0;

        for (Servicio servicio : servicios) {
            total += servicio.calcularCosto();
        }

        return total;
    }
}

