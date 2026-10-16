package uqvirutal.edu.co.parcialdosp2.servicios;

public class LegacySupport {

    public String consultarDisponibilidad(int codigoEspecialidad) {
        switch (codigoEspecialidad) {
            case 101:
                return "TEC-105|DISPONIBLE|30000";
            case 102:
                return "TEC-210|DISPONIBLE|45000";
            default:
                return "SIN_TECNICO|NO_DISPONIBLE|0";
        }
    }
}