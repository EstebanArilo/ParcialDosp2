package uqvirutal.edu.co.parcialdosp2.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Solicitud {

    private int codigo;
    private Cliente cliente;
    private List<Servicio> servicios;
    private Tecnico tecnico;
    private LocalDateTime fecha;
    private LocalDateTime fechaProgramada;
    private String observaciones;
    private PrioridadSolicitud prioridad;
    private String estado;

    private Solicitud(Builder builder) {
        this.codigo = builder.codigo;
        this.cliente = builder.cliente;
        this.servicios = new ArrayList<>(builder.servicios);
        this.tecnico = builder.tecnico;
        this.fecha = builder.fecha;
        this.fechaProgramada = builder.fechaProgramada;
        this.observaciones = builder.observaciones;
        this.prioridad = builder.prioridad;
        this.estado = builder.estado;
    }

    // Getters

    public int getCodigo() {
        return codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<Servicio> getServicios() {
        return new ArrayList<>(servicios);
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public LocalDateTime getFechaProgramada() {
        return fechaProgramada;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public PrioridadSolicitud getPrioridad() {
        return prioridad;
    }

    public String getEstado() {
        return estado;
    }

    // Setters

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFechaProgramada(LocalDateTime fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setPrioridad(PrioridadSolicitud prioridad) {
        if (prioridad == null) {
            throw new IllegalArgumentException(
                    "La prioridad no puede ser nula"
            );
        }
        this.prioridad = prioridad;
    }

    // Agregar un servicio después de crear la solicitud

    public void agregarServicio(Servicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException(
                    "El servicio no puede ser nulo"
            );
        }

        if (servicios.contains(servicio)) {
            throw new IllegalArgumentException(
                    "Este elemento ya fue agregado a la solicitud"
            );
        }

        servicios.add(servicio);
    }

    // Eliminar un servicio de la solicitud

    public boolean eliminarServicio(Servicio servicio) {
        return servicios.remove(servicio);
    }

    // Calcular el costo de todos los elementos contratados

    public double calcularCostoServicios() {
        double total = 0;

        for (Servicio servicio : servicios) {
            total += servicio.calcularCosto();
        }

        return total;
    }

    // Calcular el costo total de la solicitud

    public double calcularCostoTotal() {
        double total = calcularCostoServicios();

        if (tecnico != null) {
            total += tecnico.getTarifa();
        }

        return total;
    }

    // Patrón Builder

    public static class Builder {

        private int codigo;
        private Cliente cliente;
        private List<Servicio> servicios;
        private Tecnico tecnico;
        private LocalDateTime fecha;
        private LocalDateTime fechaProgramada;
        private String observaciones;
        private PrioridadSolicitud prioridad;
        private String estado;

        // Constructor para solicitudes con varios servicios

        public Builder(int codigo, Cliente cliente) {
            this.codigo = codigo;
            this.cliente = cliente;
            this.servicios = new ArrayList<>();
            this.fecha = LocalDateTime.now();
            this.prioridad = PrioridadSolicitud.MEDIA;
            this.estado = "Pendiente";
        }

        // Constructor compatible con el código anterior

        public Builder(
                int codigo,
                Cliente cliente,
                Servicio servicio
        ) {
            this(codigo, cliente);
            agregarServicio(servicio);
        }

        public Builder agregarServicio(Servicio servicio) {
            if (servicio == null) {
                throw new IllegalArgumentException(
                        "El servicio no puede ser nulo"
                );
            }

            if (servicios.contains(servicio)) {
                throw new IllegalArgumentException(
                        "Este elemento ya fue agregado a la solicitud"
                );
            }

            servicios.add(servicio);
            return this;
        }

        public Builder tecnico(Tecnico tecnico) {
            this.tecnico = tecnico;
            return this;
        }

        public Builder fecha(LocalDateTime fecha) {
            this.fecha = fecha;
            return this;
        }

        public Builder fechaProgramada(
                LocalDateTime fechaProgramada
        ) {
            this.fechaProgramada = fechaProgramada;
            return this;
        }

        public Builder observaciones(String observaciones) {
            this.observaciones = observaciones;
            return this;
        }

        public Builder prioridad(PrioridadSolicitud prioridad) {
            this.prioridad = prioridad;
            return this;
        }

        public Builder estado(String estado) {
            this.estado = estado;
            return this;
        }

        public Solicitud build() {
            if (cliente == null) {
                throw new IllegalStateException(
                        "La solicitud requiere un cliente"
                );
            }

            if (servicios.isEmpty()) {
                throw new IllegalStateException(
                        "La solicitud debe contener al menos un servicio"
                );
            }

            if (prioridad == null) {
                throw new IllegalStateException(
                        "La solicitud requiere una prioridad"
                );
            }

            return new Solicitud(this);
        }
    }
}
