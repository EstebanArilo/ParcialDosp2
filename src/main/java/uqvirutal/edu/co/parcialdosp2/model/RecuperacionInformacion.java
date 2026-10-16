package uqvirutal.edu.co.parcialdosp2.model;

public class RecuperacionInformacion extends ServicioBase {

    public RecuperacionInformacion(String codigo) {
        super(codigo, TipoServicio.RECUPERACION_DE_INFORMACION,
                "Recuperación de datos de discos y medios de almacenamiento dañados.");
    }

    @Override public Especialidad getEspecialidad() { return Especialidad.RECUPERACION_DATOS; }

    @Override
    public String getCondiciones() {
        return "Se realiza en laboratorio; el cliente entrega el medio de almacenamiento.";
    }
}