package uqvirutal.edu.co.parcialdosp2.model;

/**
 * Tipos de servicio del catálogo. Cada uno tiene su nombre, su costo base y la fábrica
 * que lo crea.
 */

public enum TipoServicio {

    MANTENIMIENTO_PREVENTIVO("Mantenimiento preventivo", 80_000, new FactoryMantenimientoPreventivo()),
    MANTENIMIENTO_CORRECTIVO("Mantenimiento correctivo", 120_000, new FactoryMantenimientoCorrectivo()),
    INSTALACION_DE_REDES("Instalación de redes", 180_000, new FactoryInstalacionRedes()),
    RECUPERACION_DE_INFORMACION("Recuperación de información", 150_000, new FactoryRecuperacionInformacion());

    private final String nombre;
    private final double costoBase;
    private final FactoryServicios fabrica;

    TipoServicio(String nombre, double costoBase, FactoryServicios fabrica) {
        this.nombre = nombre;
        this.costoBase = costoBase;
        this.fabrica = fabrica;
    }

    public String getNombre() { return nombre; }
    public double getCostoBase() { return costoBase; }
    public FactoryServicios getFabrica() { return fabrica; }

    @Override
    public String toString() { return nombre; }
}