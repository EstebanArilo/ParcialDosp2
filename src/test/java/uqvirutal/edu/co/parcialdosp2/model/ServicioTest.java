package uqvirutal.edu.co.parcialdosp2.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ServicioTest {

    private TechSolutions techSolutions;

    @BeforeEach
    void iniciar() {
        TechSolutions.reiniciar();
        techSolutions = TechSolutions.getInstancia();
    }

    @Test
    void catalogoInicialTieneLosCuatroServiciosConSuCostoBase() {
        List<ServicioBase> catalogo = techSolutions.consultarCatalogo();
        assertTrue(catalogo.size() == 4);
        assertTrue(catalogo.get(0).getCostoBase() == 80_000);
        assertTrue(catalogo.get(1).getCostoBase() == 120_000);
        assertTrue(catalogo.get(2).getCostoBase() == 180_000);
        assertTrue(catalogo.get(3).getCostoBase() == 150_000);
    }

    @Test
    void cadaFabricaCreaSuTipoDeServicio() {

        assertTrue(TipoServicio.MANTENIMIENTO_CORRECTIVO.getFabrica().crear("X-2") instanceof MantenimientoCorrectivo);
        assertTrue(TipoServicio.INSTALACION_DE_REDES.getFabrica().crear("X-3") instanceof InstalacionRedes);
        assertTrue(TipoServicio.RECUPERACION_DE_INFORMACION.getFabrica().crear("X-4") instanceof RecuperacionInformacion);
    }

    @Test
    void crearServicioLoAgregaAlCatalogoConCodigoConsecutivo() {
        Resultado<ServicioBase> resultado = techSolutions.crearServicio(TipoServicio.INSTALACION_DE_REDES);
        assertTrue(resultado.isExitoso());
        assertTrue(resultado.getValor().getCodigo().equals("SRV-0005"));
        assertTrue(techSolutions.consultarCatalogo().size() == 5);
    }

    @Test
    void crearServicioSinTipoEsRechazado() {
        Resultado<ServicioBase> resultado = techSolutions.crearServicio(null);
        assertTrue(!resultado.isExitoso());
        assertTrue(techSolutions.consultarCatalogo().size() == 4);
    }
}