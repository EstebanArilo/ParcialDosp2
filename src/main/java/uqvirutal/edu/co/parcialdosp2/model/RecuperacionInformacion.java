package uqvirutal.edu.co.parcialdosp2.model;

/**
 * Servicio de recuperación de datos de discos o medios dañados. Es de la especialidad
 * RECUPERACION_DATOS y se hace en laboratorio, por eso no requiere visita.
 */

public class RecuperacionInformacion extends ServicioBase {

    public RecuperacionInformacion(String codigo) {
        super(
        );
    }

    @Override public Especialidad getEspecialidad() { return Especialidad.RECUPERACION_DATOS; }

    @Override
    public String getCondiciones() {
        return "Se realiza en laboratorio; el cliente entrega el medio de almacenamiento.";
    }
}