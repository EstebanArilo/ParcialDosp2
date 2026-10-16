package uqvirutal.edu.co.parcialdosp2.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PaqueteTest {

    private TechSolutions techSolutions;
    private List<ServicioBase> catalogo;

    @BeforeEach
    void iniciar() {
        TechSolutions.reiniciar();
        techSolutions = TechSolutions.getInstancia();
        catalogo = techSolutions.consultarCatalogo();
    }

    @Test
    void paqueteSumaElCostoDeSusServicios() {
        PaqueteTecnologico paquete = techSolutions.crearPaquete("Oficina", "Basico").getValor();
        assertTrue(techSolutions.agregarComponente(paquete, catalogo.get(0)).isExitoso());
        assertTrue(techSolutions.agregarComponente(paquete, catalogo.get(2)).isExitoso());
        assertTrue(paquete.calcularCosto() == 260_000);
    }

    @Test
    void paqueteDentroDeOtroPaqueteCalculaElCostoRecursivo() {
        PaqueteTecnologico interno = techSolutions.crearPaquete("Interno", "").getValor();
        techSolutions.agregarComponente(interno, catalogo.get(0));
        techSolutions.agregarComponente(interno, catalogo.get(2));
        PaqueteTecnologico externo = techSolutions.crearPaquete("Externo", "").getValor();
        techSolutions.agregarComponente(externo, interno);
        techSolutions.agregarComponente(externo, catalogo.get(3));
        assertTrue(externo.calcularCosto() == 410_000);
    }

    @Test
    void paqueteAdmiteServiciosPersonalizados() {
        Servicio personalizado = techSolutions.personalizarServicio(
                catalogo.get(1), List.of(TipoComplemento.ATENCION_PRIORITARIA)).getValor();
        PaqueteTecnologico paquete = techSolutions.crearPaquete("Con extra", "").getValor();
        assertTrue(techSolutions.agregarComponente(paquete, personalizado).isExitoso());
        assertTrue(paquete.calcularCosto() == 155_000);
    }

    @Test
    void unPaqueteNoPuedeIncluirseASiMismo() {
        PaqueteTecnologico paquete = techSolutions.crearPaquete("A", "").getValor();
        Resultado<Void> resultado = techSolutions.agregarComponente(paquete, paquete);
        assertTrue(!resultado.isExitoso());
        assertTrue(paquete.getComponentes().isEmpty());
    }

    @Test
    void unPaqueteNoPuedeIncluirseIndirectamente() {
        PaqueteTecnologico a = techSolutions.crearPaquete("A", "").getValor();
        PaqueteTecnologico b = techSolutions.crearPaquete("B", "").getValor();
        PaqueteTecnologico c = techSolutions.crearPaquete("C", "").getValor();
        assertTrue(techSolutions.agregarComponente(a, b).isExitoso());
        assertTrue(techSolutions.agregarComponente(b, c).isExitoso());
        assertTrue(!techSolutions.agregarComponente(c, a).isExitoso());
        assertTrue(!techSolutions.agregarComponente(b, a).isExitoso());
        assertTrue(c.getComponentes().isEmpty());
    }

    @Test
    void nombreVacioOComponenteRepetidoSonRechazados() {
        assertTrue(!techSolutions.crearPaquete("  ", "").isExitoso());
        PaqueteTecnologico paquete = techSolutions.crearPaquete("A", "").getValor();
        techSolutions.agregarComponente(paquete, catalogo.get(0));
        assertTrue(!techSolutions.agregarComponente(paquete, catalogo.get(0)).isExitoso());
        assertTrue(paquete.getComponentes().size() == 1);
    }
}