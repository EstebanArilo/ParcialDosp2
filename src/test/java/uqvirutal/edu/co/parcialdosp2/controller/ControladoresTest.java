package uqvirutal.edu.co.parcialdosp2.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uqvirutal.edu.co.parcialdosp2.model.EstadoSolicitud;
import uqvirutal.edu.co.parcialdosp2.model.PaqueteTecnologico;
import uqvirutal.edu.co.parcialdosp2.model.Resultado;
import uqvirutal.edu.co.parcialdosp2.model.Servicio;
import uqvirutal.edu.co.parcialdosp2.model.ServicioBase;
import uqvirutal.edu.co.parcialdosp2.model.Solicitud;
import uqvirutal.edu.co.parcialdosp2.model.TechSolutions;
import uqvirutal.edu.co.parcialdosp2.model.TipoComplemento;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ControladoresTest {

    private static final String LAURA = "1094123456";

    private PaqueteController paqueteController;
    private SolicitudController solicitudController;
    private TecnicoController tecnicoController;
    private List<ServicioBase> catalogo;

    @BeforeEach
    void iniciar() {
        TechSolutions.reiniciar();
        paqueteController = new PaqueteController();
        solicitudController = new SolicitudController();
        tecnicoController = new TecnicoController();
    }

    @Test
    void elControladorDeCatalogoMuestraCostoBaseIncrementosYCostoFinal() {
        TechSolutions catalogoController = null;
        Resultado<Servicio> resultado = catalogoController.personalizarServicio(
                catalogo.get(1), List.of(TipoComplemento.ATENCION_PRIORITARIA));
        assertTrue(resultado.isExitoso());
        assertTrue(resultado.getValor().getCostoBase() == 120_000);
        assertTrue(resultado.getValor().getIncrementos() == 35_000);
        assertTrue(resultado.getValor().calcularCosto() == 155_000);
        assertTrue(catalogoController.consultarServiciosPersonalizados().size() == 1);
        assertTrue(catalogoController.consultarTiposComplemento().length == 3);
    }

    @Test
    void elControladorDePaquetesRechazaCiclosYNoOfreceAlPropioPaquete() {
        PaqueteTecnologico a = (PaqueteTecnologico) paqueteController.crearPaquete("A", "").getValor();
        PaqueteTecnologico b = (PaqueteTecnologico) paqueteController.crearPaquete("B", "").getValor();
        assertTrue(!paqueteController.agregarComponente(a, a).isExitoso());
        assertTrue(paqueteController.agregarComponente(a, b).isExitoso());
        assertTrue(!paqueteController.agregarComponente(b, a).isExitoso());
        assertTrue(!paqueteController.consultarComponentesDisponibles(a).contains(a));
        assertTrue(paqueteController.consultarComponentes(a).size() == 1);
    }

    @Test
    void elControladorDeSolicitudesRegistraConOpcionalesNulosYRechazaLoInvalido() {
        boolean valida = solicitudController.registrarSolicitud(
                LAURA, List.of(catalogo.get(0)), null, null, null);
        assertTrue(valida);
        assertTrue(solicitudController.consultarSolicitudes().size() == 1);
        assertTrue(!solicitudController.registrarSolicitud(null, List.of(catalogo.get(0)), null, null, null));
        assertTrue(!solicitudController.registrarSolicitud(LAURA, new ArrayList<>(), null, null, null));
        assertTrue(!solicitudController.registrarSolicitud(LAURA, null, null, null, null));
        assertTrue(solicitudController.consultarSolicitudes().size() == 1);
    }

    @Test
    void losElementosContratablesIncluyenCatalogoPersonalizadosYPaquetes() {
        TechSolutions catalogoController = null;
        catalogoController.personalizarServicio(catalogo.get(0), List.of(TipoComplemento.ATENCION_PRIORITARIA));
        paqueteController.crearPaquete("P", "");
        assertTrue(solicitudController.consultarElementosContratables().size() == 4 + 1 + 1);
    }

    @Test
    void elControladorDeTecnicosConfirmaYReportaLaFaltaDeTecnico() {
        boolean conTecnico = solicitudController.registrarSolicitud(
                LAURA, List.of(catalogo.get(0)), null, null, null);
        assertTrue(tecnicoController.hayTecnicosDisponibles(conTecnico));
        assertTrue(tecnicoController.calcularCostoTotal(conTecnico) == 110_000);
        assertTrue(tecnicoController.confirmarSolicitud(conTecnico).isExitoso());

        TechSolutions catalogoController = null;
        Servicio recuperacionADomicilio = catalogoController.personalizarServicio(
                catalogo.get(3), List.of(TipoComplemento.ATENCION_DOMICILIO)).getValor();
        boolean sinTecnico = solicitudController.registrarSolicitud(
                LAURA, List.of(recuperacionADomicilio), null, null, null);
        assertTrue(!tecnicoController.hayTecnicosDisponibles(sinTecnico));
        Resultado<Object> resultado = tecnicoController.confirmarSolicitud(sinTecnico);
        assertTrue(!resultado.isExitoso());

    }
}