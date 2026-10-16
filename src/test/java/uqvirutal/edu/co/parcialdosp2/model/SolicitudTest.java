package uqvirutal.edu.co.parcialdosp2.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SolicitudTest {

    private static final String LAURA = "1094123456";

    private TechSolutions techSolutions;
    private ServicioBase preventivo;

    @BeforeEach
    void iniciar() {
        TechSolutions.reiniciar();
        techSolutions = TechSolutions.getInstancia();
        preventivo = techSolutions.consultarCatalogo().get(0);
    }

    private Resultado<Solicitud> registrar(Solicitud.Builder builder) {
        return techSolutions.registrarSolicitud(builder);
    }

    @Test
    void solicitudConSoloLosDatosObligatoriosQuedaPendiente() {
        Solicitud.Builder builder = techSolutions.nuevaSolicitud(LAURA).getValor().agregarServicio(preventivo);
        Resultado<Solicitud> resultado = registrar(builder);
        assertTrue(resultado.isExitoso());
        Solicitud solicitud = resultado.getValor();
        assertTrue(solicitud.getCodigo().equals("SOL-0001"));
        assertTrue(solicitud.getFechaCreacion() != null);
        assertTrue(solicitud.getEstado() == EstadoSolicitud.PENDIENTE);
        assertTrue(solicitud.getFechaProgramada() == null);
        assertTrue(solicitud.getObservaciones() == null);
        assertTrue(solicitud.getPrioridad() == null);
        assertTrue(techSolutions.consultarSolicitudes().size() == 1);
    }

    @Test
    void solicitudConDatosOpcionalesLosConserva() {
        LocalDate fecha = LocalDate.now().plusDays(3);
        Solicitud.Builder builder = techSolutions.nuevaSolicitud(LAURA).getValor()
                .agregarServicio(preventivo)
                .fechaProgramada(fecha)
                .observaciones("  Equipo lento  ")
                .prioridad(PrioridadSolicitud.ALTA);
        Solicitud solicitud = registrar(builder).getValor();
        assertTrue(solicitud.getFechaProgramada().equals(fecha));
        assertTrue(solicitud.getObservaciones().equals("Equipo lento"));
        assertTrue(solicitud.getPrioridad() == PrioridadSolicitud.ALTA);
    }

    @Test
    void solicitudSinElementosEsRechazada() {
        Resultado<Solicitud> resultado = registrar(techSolutions.nuevaSolicitud(LAURA).getValor());
        assertTrue(!resultado.isExitoso());
        assertTrue(techSolutions.consultarSolicitudes().isEmpty());
    }

    @Test
    void clienteInexistenteOSinSeleccionarEsRechazado() {
        assertTrue(!techSolutions.nuevaSolicitud("000").isExitoso());
        assertTrue(!techSolutions.nuevaSolicitud(null).isExitoso());
    }

    @Test
    void fechaProgramadaEnElPasadoEsRechazada() {
        Solicitud.Builder builder = techSolutions.nuevaSolicitud(LAURA).getValor()
                .agregarServicio(preventivo)
                .fechaProgramada(LocalDate.now().minusDays(1));
        assertTrue(!registrar(builder).isExitoso());
    }

    @Test
    void elMismoElementoDosVecesEsRechazadoParaNoCobrarDoble() {
        Solicitud.Builder builder = techSolutions.nuevaSolicitud(LAURA).getValor()
                .agregarServicio(preventivo)
                .agregarServicio(preventivo);
        assertTrue(!registrar(builder).isExitoso());
    }

    @Test
    void laSolicitudAdmiteServiciosPersonalizadosYPaquetes() {
        Servicio personalizado = techSolutions.personalizarServicio(
                techSolutions.consultarCatalogo().get(1),
                java.util.List.of(TipoComplemento.SOPORTE_TECNICO_30_DIAS)).getValor();
        PaqueteTecnologico paquete = techSolutions.crearPaquete("P", "").getValor();
        techSolutions.agregarComponente(paquete, techSolutions.consultarCatalogo().get(2));
        Solicitud solicitud = registrar(techSolutions.nuevaSolicitud(LAURA).getValor()
                .agregarServicio(personalizado).agregarServicio(paquete)).getValor();
        assertTrue(solicitud.calcularCostoServicios() == 170_000 + 180_000);
    }

    @Test
    void cadaSolicitudRecibeUnCodigoUnico() {
        Solicitud primera = registrar(techSolutions.nuevaSolicitud(LAURA).getValor().agregarServicio(preventivo)).getValor();
        Solicitud segunda = registrar(techSolutions.nuevaSolicitud(LAURA).getValor().agregarServicio(preventivo)).getValor();
        assertTrue(!primera.getCodigo().equals(segunda.getCodigo()));
        assertTrue(techSolutions.buscarSolicitud(segunda.getCodigo()) == segunda);
        assertTrue(techSolutions.buscarSolicitud("SOL-9999") == null);
    }

    @Test
    void hayTresClientesPrecargadosIdentificadosPorDocumento() {
        assertTrue(techSolutions.consultarClientes().size() == 3);
        assertTrue(techSolutions.buscarCliente(LAURA).getNombreCompleto().equals("Laura Gómez Ríos"));
        assertTrue(techSolutions.buscarCliente("000") == null);
    }
}