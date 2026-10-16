package uqvirutal.edu.co.parcialdosp2.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uqvirutal.edu.co.parcialdosp2.servicios.LegacySupport;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TecnicoYCostoTest {

    private static final String LAURA = "1094123456";

    private TechSolutions techSolutions;
    private List<ServicioBase> catalogo;

    @BeforeEach
    void iniciar() {
        TechSolutions.reiniciar();
        techSolutions = TechSolutions.getInstancia();
        catalogo = techSolutions.consultarCatalogo();
    }

    private Solicitud solicitud(Servicio... elementos) {
        Solicitud.Builder builder = techSolutions.nuevaSolicitud(LAURA).getValor();
        for (Servicio elemento : elementos) {
            builder.agregarServicio(elemento);
        }
        return techSolutions.registrarSolicitud(builder).getValor();
    }

    @Test
    void elAdaptadorConvierteLaRespuestaDeLegacySupportEnUnTecnico() {
        Tecnico mantenimiento = techSolutions.consultarTecnico(Especialidad.MANTENIMIENTO_EQUIPOS);
        assertTrue(mantenimiento.getId().equals("TEC-105"));
        assertTrue(mantenimiento.isDisponible());
        assertTrue(mantenimiento.getCostoDesplazamiento() == 30_000);
        Tecnico redes = techSolutions.consultarTecnico(Especialidad.REDES);
        assertTrue(redes.getId().equals("TEC-210"));
        assertTrue(redes.getCostoDesplazamiento() == 45_000);
    }

    @Test
    void sinTecnicoDisponibleSeInformaComoNoDisponible() {
        Tecnico tecnico = techSolutions.consultarTecnico(Especialidad.RECUPERACION_DATOS);
        assertTrue(!tecnico.isDisponible());
        assertTrue(tecnico.getDisponibilidad() == DisponibilidadTecnico.NO_DISPONIBLE);
        assertTrue(tecnico.getCostoDesplazamiento() == 0);
    }

    @Test
    void unaRespuestaIrreconocibleDeLegacySupportNoRompeElSistema() {
        LegacySupport defectuoso = new LegacySupport() {
            @Override
            public String consultarDisponibilidad(int codigoEspecialidad) {
                return "respuesta rara";
            }
        };
        techSolutions.setProveedorTecnicos(new AdaptadorLegacySupport(defectuoso));
        assertTrue(!techSolutions.consultarTecnico(Especialidad.REDES).isDisponible());
    }

    @Test
    void solicitudPresencialConTecnicoSeConfirmaYCobraDesplazamiento() {
        Solicitud solicitud = solicitud(catalogo.get(0));
        assertTrue(solicitud.requierePresencial());
        assertTrue(techSolutions.hayTecnicosDisponibles(solicitud));
        assertTrue(techSolutions.calcularCostoTotal(solicitud) == 110_000);
        Resultado<Solicitud> resultado = techSolutions.confirmarSolicitud(solicitud);
        assertTrue(resultado.isExitoso());
        assertTrue(solicitud.getEstado() == EstadoSolicitud.CONFIRMADA);
        assertTrue(solicitud.getCostoDesplazamiento() == 30_000);
        assertTrue(solicitud.calcularCostoTotal() == 110_000);
    }

    @Test
    void solicitudPresencialSinTecnicoNoSePuedeConfirmar() {
        Servicio recuperacionADomicilio = techSolutions.personalizarServicio(
                catalogo.get(3), List.of(TipoComplemento.ATENCION_DOMICILIO)).getValor();
        Solicitud solicitud = solicitud(recuperacionADomicilio);
        assertTrue(solicitud.requierePresencial());
        assertTrue(!techSolutions.hayTecnicosDisponibles(solicitud));
        Resultado<Solicitud> resultado = techSolutions.confirmarSolicitud(solicitud);
        assertTrue(!resultado.isExitoso());
        assertTrue(resultado.getMensaje().contains("Recuperación de datos"));
        assertTrue(solicitud.getEstado() == EstadoSolicitud.PENDIENTE);
    }

    @Test
    void solicitudNoPresencialSeConfirmaSinTecnicoNiDesplazamiento() {
        Solicitud solicitud = solicitud(catalogo.get(3));
        assertTrue(!solicitud.requierePresencial());
        assertTrue(techSolutions.consultarTecnicosRequeridos(solicitud).isEmpty());
        assertTrue(techSolutions.confirmarSolicitud(solicitud).isExitoso());
        assertTrue(solicitud.calcularCostoTotal() == 150_000);
    }

    @Test
    void elDesplazamientoSeCobraUnaSolaVezPorEspecialidad() {
        Solicitud solicitud = solicitud(catalogo.get(0), catalogo.get(1));
        assertTrue(techSolutions.consultarTecnicosRequeridos(solicitud).size() == 1);
        assertTrue(techSolutions.calcularCostoTotal(solicitud) == 80_000 + 120_000 + 30_000);
    }

    @Test
    void especialidadesDistintasCobranCadaDesplazamiento() {
        Solicitud solicitud = solicitud(catalogo.get(0), catalogo.get(2));
        assertTrue(techSolutions.consultarTecnicosRequeridos(solicitud).size() == 2);
        assertTrue(techSolutions.calcularCostoTotal(solicitud) == 80_000 + 180_000 + 30_000 + 45_000);
    }

    @Test
    void losComplementosNoSeCobranDosVeces() {
        Servicio personalizado = techSolutions.personalizarServicio(
                catalogo.get(1), List.of(TipoComplemento.ATENCION_PRIORITARIA)).getValor();
        Solicitud solicitud = solicitud(personalizado);
        assertTrue(solicitud.calcularCostoServicios() == 155_000);
        double antes = techSolutions.calcularCostoTotal(solicitud);
        techSolutions.confirmarSolicitud(solicitud);
        assertTrue(techSolutions.calcularCostoTotal(solicitud) == antes);
        assertTrue(antes == 155_000 + 30_000);
    }

    @Test
    void unaSolicitudYaConfirmadaNoSeConfirmaOtraVez() {
        Solicitud solicitud = solicitud(catalogo.get(3));
        techSolutions.confirmarSolicitud(solicitud);
        assertTrue(!techSolutions.confirmarSolicitud(solicitud).isExitoso());
        assertTrue(!techSolutions.confirmarSolicitud(null).isExitoso());
    }
}