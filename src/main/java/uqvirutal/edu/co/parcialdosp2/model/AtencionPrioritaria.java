package uqvirutal.edu.co.parcialdosp2.model;

/** Complemento que le da atención prioritaria al servicio. Suma $35.000. */

public class AtencionPrioritaria extends Complemento {
    public AtencionPrioritaria(Servicio servicio) {
        super(servicio, TipoComplemento.ATENCION_PRIORITARIA);
    }
}