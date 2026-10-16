package uqvirutal.edu.co.parcialdosp2.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Solicitud {

    private final String codigo;
    private final LocalDateTime fechaCreacion;
    private final Cliente cliente;
    private final List<Servicio> elementosContratados;

    // Datos opcionales (pueden ser null)
    private final LocalDate fechaProgramada;
    private final String observaciones;
    private final PrioridadSolicitud prioridad;

    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;
    private double costoDesplazamiento;

    private Solicitud(Builder builder) {
        this.codigo = builder.codigo;
        this.fechaCreacion = LocalDateTime.now();
        this.cliente = builder.cliente;
        this.elementosContratados = new ArrayList<>(builder.elementos);
        this.fechaProgramada = builder.fechaProgramada;
        this.observaciones = builder.observaciones;
        this.prioridad = builder.prioridad;
    }

    public String getCodigo() { return codigo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public Cliente getCliente() { return cliente; }

    public List<Servicio> getElementosContratados() {
        return new ArrayList<>(elementosContratados);
    }

    public LocalDate getFechaProgramada() { return fechaProgramada; }
    public String getObservaciones() { return observaciones; }
    public PrioridadSolicitud getPrioridad() { return prioridad; }
    public EstadoSolicitud getEstado() { return estado; }
    public double getCostoDesplazamiento() { return costoDesplazamiento; }

    public double calcularCostoServicios() {
        double total = 0;
        for (Servicio elemento : elementosContratados) {
            total += elemento.calcularCosto();
        }
        return total;
    }

    public double calcularCostoTotal() {
        return calcularCostoServicios() + costoDesplazamiento;
    }

    public List<Especialidad> getEspecialidadesPresenciales() {
        List<Especialidad> especialidades = new ArrayList<>();
        for (Servicio elemento : elementosContratados) {
            Especialidad.agregarSinRepetir(especialidades, elemento.getEspecialidadesPresenciales());
        }
        return especialidades;
    }

    public boolean requierePresencial() {
        return !getEspecialidadesPresenciales().isEmpty();
    }

    // Solo TechSolutions confirma, después de verificar los técnicos (mismo paquete)
    void confirmar(double costoDesplazamiento) {
        this.costoDesplazamiento = costoDesplazamiento;
        this.estado = EstadoSolicitud.CONFIRMADA;
    }

    @Override
    public String toString() {
        return codigo + " - " + cliente.getNombreCompleto() + " - " + estado;
    }

    // Patrón Builder
    public static class Builder {

        private final String codigo;
        private final Cliente cliente;
        private final List<Servicio> elementos = new ArrayList<>();
        private final List<String> errores = new ArrayList<>();
        private LocalDate fechaProgramada;
        private String observaciones;
        private PrioridadSolicitud prioridad;

        public Builder(String codigo, Cliente cliente) {
            this.codigo = codigo;
            this.cliente = cliente;
        }

        // Si el elemento es nulo o está repetido no se agrega; el error se informa en build()
        public Builder agregarServicio(Servicio servicio) {
            if (servicio == null) {
                errores.add("El elemento a contratar es obligatorio.");
            } else if (elementos.contains(servicio)) {
                errores.add("'" + servicio.getNombre()
                        + "' ya fue agregado a la solicitud (evita cobros duplicados).");
            } else {
                elementos.add(servicio);
            }
            return this;
        }

        public Builder fechaProgramada(LocalDate fechaProgramada) {
            this.fechaProgramada = fechaProgramada;
            return this;
        }

        public Builder observaciones(String observaciones) {
            this.observaciones = (observaciones == null || observaciones.isBlank()) ? null : observaciones.trim();
            return this;
        }

        public Builder prioridad(PrioridadSolicitud prioridad) {
            this.prioridad = prioridad;
            return this;
        }

        public Resultado<Solicitud> build() {
            if (!errores.isEmpty()) {
                return Resultado.error(errores.get(0));
            }
            if (elementos.isEmpty()) {
                return Resultado.error(
                        "La solicitud debe incluir al menos un servicio, servicio personalizado o paquete.");
            }
            if (fechaProgramada != null && fechaProgramada.isBefore(LocalDate.now())) {
                return Resultado.error("La fecha programada no puede estar en el pasado.");
            }
            return Resultado.exito(new Solicitud(this));
        }
    }
}