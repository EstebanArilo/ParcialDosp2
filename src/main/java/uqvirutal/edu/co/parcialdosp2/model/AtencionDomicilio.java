package uqvirutal.edu.co.parcialdosp2.model;

import java.util.List;

public class AtencionDomicilio extends Complemento {

    public AtencionDomicilio(Servicio servicio) {
        super(servicio, TipoComplemento.ATENCION_DOMICILIO);
    }

    @Override
    public List<Especialidad> getEspecialidadesPresenciales() {
        return servicio.getEspecialidades();
    }
}