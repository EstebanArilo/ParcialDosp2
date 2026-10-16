package uqvirutal.edu.co.parcialdosp2.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private TechSolutions techSolutions;
    private PaqueteController paqueteController;
    private SolicitudController solicitudController;
    private TecnicoController tecnicoController;
    private List<ServicioBase> catalogo;

    @BeforeEach
    void iniciar() {
        TechSolutions.reiniciar();
        techSolutions = TechSolutions.getInstancia();
        catalogo = techSolutions.consultarCatalogo();
        paqueteController = new PaqueteController();
        solicitudController = new SolicitudController();
        tecnicoController = new TecnicoController();
    }

    @Test
    void elControladorDeCatalogoMuestraCostoBaseIncrementosYCostoFinal() {
        Resultado<Servicio> resultado = techSolutions.personalizarServicio(
                catalogo.get(1), List.of(TipoComplemento.ATENCION_PRIORITARIA));
        assertTrue(resultado.isExitoso());
        assertTrue(resultado.getValor().getCostoBase() == 120_000);
        assertTrue(resultado.getValor().getIncrementos() == 35_000);
        assertTrue(resultado.getValor().calcularCosto() == 155_000);
        assertTrue(techSolutions.consultarServiciosPersonalizados().size() == 1);
        assertTrue(techSolutions.consultarTiposComplemento().length == 3);
    }

    @Test
    void elControladorDePaquetesRechazaCiclosYNoOfreceAlPropioPaquete() {
        PaqueteTecnologico a = paqueteController.crearPaquete("A", "").getValor();
        PaqueteTecnologico b = paqueteController.crearPaquete("B", "").getValor();
        assertTrue(!paqueteController.agregarComponente(a, a).isExitoso());
        assertTrue(paqueteController.agregarComponente(a, b).isExitoso());
        assertTrue(!paqueteController.agregarComponente(b, a).isExitoso());
        assertTrue(!paqueteController.consultarComponentesDisponibles(a).contains(a));
        assertTrue(paqueteController.consultarComponentes(a).size() == 1);
    }

    @Test
    void elControladorDeSolicitudesRegistraConOpcionalesNulosYRechazaLoInvalido() {
        Resultado<Solicitud> valida = solicitudController.registrarSolicitud(
                LAURA, List.of(catalogo.get(0)), null, null, null);
        assertTrue(valida.isExitoso());
        assertTrue(solicitudController.consultarSolicitudes().size() == 1);
        assertTrue(!solicitudController.registrarSolicitud(null, List.of(catalogo.get(0)), null, null, null).isExitoso());
        assertTrue(!solicitudController.registrarSolicitud(LAURA, new ArrayList<>(), null, null, null).isExitoso());
        assertTrue(!solicitudController.registrarSolicitud(LAURA, null, null, null, null).isExitoso());
        assertTrue(solicitudController.consultarSolicitudes().size() == 1);
    }

    @Test
    void losElementosContratablesIncluyenCatalogoPersonalizadosYPaquetes() {
        techSolutions.personalizarServicio(catalogo.get(0), List.of(TipoComplemento.ATENCION_PRIORITARIA));
        paqueteController.crearPaquete("P", "");
        assertTrue(solicitudController.consultarElementosContratables().size() == 4 + 1 + 1);
    }

    @Test
    void elControladorDeTecnicosConfirmaYReportaLaFaltaDeTecnico() {
        Solicitud conTecnico = solicitudController.registrarSolicitud(
                LAURA, List.of(catalogo.get(0)), null, null, null).getValor();
        assertTrue(tecnicoController.hayTecnicosDisponibles(conTecnico));
        assertTrue(tecnicoController.calcularCostoTotal(conTecnico) == 110_000);
        assertTrue(tecnicoController.confirmarSolicitud(conTecnico).isExitoso());

        Servicio recuperacionADomicilio = techSolutions.personalizarServicio(
                catalogo.get(3), List.of(TipoComplemento.ATENCION_DOMICILIO)).getValor();
        Solicitud sinTecnico = solicitudController.registrarSolicitud(
                LAURA, List.of(recuperacionADomicilio), null, null, null).getValor();
        assertTrue(!tecnicoController.hayTecnicosDisponibles(sinTecnico));
        Resultado<Solicitud> resultado = tecnicoController.confirmarSolicitud(sinTecnico);
        assertTrue(!resultado.isExitoso());
    }
}