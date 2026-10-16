package uqvirutal.edu.co.parcialdosp2.model;

/** Metodo que crea servicios de mantenimiento preventivo. */

public class FactoryMantenimientoPreventivo extends FactoryServicios {
    @Override
    protected ServicioBase crearServicio(String codigo) {
        return new MantenimientoPreventivo(codigo) {
            @Override
            public Especialidad getEspecialidad() {
                return null;
            }

            @Override
            public String getCondiciones() {
                return "";
            }
        };
    }

    private abstract class MantenimientoPreventivo extends ServicioBase {
        public MantenimientoPreventivo(String codigo) {
        }
    }
}