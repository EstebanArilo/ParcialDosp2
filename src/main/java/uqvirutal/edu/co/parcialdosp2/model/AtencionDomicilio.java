package uqvirutal.edu.co.parcialdosp2.model;

import java.util.List;

/**
 * Complemento que hace que el servicio se preste en la casa o sede del cliente. Suma
 * $25.000 y vuelve presenciales todas las especialidades del servicio.
 */

public class AtencionDomicilio extends Complemento {

    public AtencionDomicilio(Servicio servicio) {

        super(servicio, TipoComplemento.ATENCION_DOMICILIO);
    }

    @Override
    public List<Especialidad> getEspecialidadesPresenciales() {

        return servicio.getEspecialidades();
    }
}