package uqvirutal.edu.co.parcialdosp2.model;

public class Cliente {

    private final String documento;
    private final String nombreCompleto;
    private final String correo;

    public Cliente(String documento, String nombreCompleto, String correo) {
        this.documento = documento;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
    }

    public String getDocumento() { return documento; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getCorreo() { return correo; }

    @Override
    public String toString() { return nombreCompleto + " (" + documento + ")"; }
}