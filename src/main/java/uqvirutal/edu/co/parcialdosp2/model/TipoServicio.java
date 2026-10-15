package uqvirutal.edu.co.parcialdosp2.model;


public enum TipoServicio {

    MANTENIMIENTO_PREVENTIVO(80000),
    MANTENIMIENTO_CORRECTIVO(120000),
    INSTALACION_DE_REDES(180000),
    RECUPERACION_DE_INFORMACION(150000);

    private final double costoBase;

    TipoServicio(double costoBase) {
        this.costoBase = costoBase;
    }

    public double getCostoBase() {
        return costoBase;
    }
}
