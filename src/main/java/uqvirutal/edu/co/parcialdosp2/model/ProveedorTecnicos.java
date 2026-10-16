package uqvirutal.edu.co.parcialdosp2.model;

/**
 * Contrato para cualquier proveedor que consulte técnicos. Gracias a esto se puede
 * cambiar de proveedor sin tocar el resto del sistema.
 */

public interface ProveedorTecnicos {
    String getNombre();
    Tecnico consultarTecnico(Especialidad especialidad);
}