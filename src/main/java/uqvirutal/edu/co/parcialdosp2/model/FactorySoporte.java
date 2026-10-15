package uqvirutal.edu.co.parcialdosp2.model;


public class FactorySoporte extends FactoryServicios {

    @Override
    public Servicio crearServicio() {
        return new ServicioBase(
                TipoServicio.MANTENIMIENTO_PREVENTIVO,
                "Mantenimiento preventivo de equipos"
        );
    }
}
