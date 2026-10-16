package uqvirutal.edu.co.parcialdosp2.model;

import java.util.function.Function;

public enum TipoComplemento {
    ATENCION_PRIORITARIA("Atención prioritaria", 35_000, AtencionPrioritaria::new),
    SOPORTE_TECNICO_30_DIAS("Soporte técnico 30 días", 50_000, SoporteTecnico30Dias::new),
    ATENCION_DOMICILIO("Atención a domicilio", 25_000, AtencionDomicilio::new);

    private final String nombre;
    private final double incremento;
    private final Function<Servicio, Complemento> creador;

    TipoComplemento(String nombre, double incremento, Function<Servicio, Complemento> creador) {
        this.nombre = nombre;
        this.incremento = incremento;
        this.creador = creador;
    }

    public String getNombre() { return nombre; }
    public double getIncremento() { return incremento; }

    public Complemento aplicarA(Servicio servicio) {
        return creador.apply(servicio);
    }

    @Override
    public String toString() { return nombre; }
}