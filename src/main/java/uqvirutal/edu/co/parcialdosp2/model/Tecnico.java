package uqvirutal.edu.co.parcialdosp2.model;

public class Tecnico {
    private String identificacion;
    private String nombre;
    private String especialidad;
    private double tarifa;

    public Tecnico(String identificacion, String nombre, String especialidad, double tarifa) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.especialidad = especialidad;
        setTarifa(tarifa);
    }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public double getTarifa() { return tarifa; }

    public void setTarifa(double tarifa) {
        if (tarifa < 0) throw new IllegalArgumentException("Tarifa negativa");
        this.tarifa = tarifa;
    }
}