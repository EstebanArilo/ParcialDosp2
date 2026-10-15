package uqvirutal.edu.co.parcialdosp2.model;



public class FactoryMantenimiento extends FactoryServicios {

    @Override
    public Servicio crearServicio() {
        return new ServicioBase(
                TipoServicio.MANTENIMIENTO_CORRECTIVO,
                "Mantenimiento correctivo de equipos"
        );
    }
}
