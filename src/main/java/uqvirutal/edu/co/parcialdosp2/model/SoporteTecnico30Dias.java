package uqvirutal.edu.co.parcialdosp2.model;

/** Complemento que agrega 30 días de soporte técnico al servicio. Suma $50.000. */

public class SoporteTecnico30Dias extends Complemento {
    public SoporteTecnico30Dias(Servicio servicio) {
        super(servicio, TipoComplemento.SOPORTE_TECNICO_30_DIAS);
    }
}