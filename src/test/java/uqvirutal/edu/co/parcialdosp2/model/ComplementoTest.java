package uqvirutal.edu.co.parcialdosp2.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ComplementoTest {

    private TechSolutions techSolutions;
    private ServicioBase correctivo;
    private ServicioBase recuperacion;

    @BeforeEach
    void iniciar() {
        TechSolutions.reiniciar();
        techSolutions = TechSolutions.getInstancia();
        correctivo = techSolutions.consultarCatalogo().get(1);
        recuperacion = techSolutions.consultarCatalogo().get(3);
    }

    @Test
    void complementoIncrementaElCostoSinAlterarElServicioDelCatalogo() {
        Resultado<Servicio> resultado = techSolutions.personalizarServicio(
                correctivo, List.of(TipoComplemento.ATENCION_PRIORITARIA));
        assertTrue(resultado.isExitoso());
        assertTrue(resultado.getValor().getCostoBase() == 120_000);
        assertTrue(resultado.getValor().getIncrementos() == 35_000);
        assertTrue(resultado.getValor().calcularCosto() == 155_000);
        assertTrue(correctivo.calcularCosto() == 120_000);
        assertTrue(techSolutions.consultarCatalogo().get(1) == correctivo);
    }

    @Test
    void variosComplementosSeSuman() {
        Resultado<Servicio> resultado = techSolutions.personalizarServicio(correctivo, List.of(
                TipoComplemento.ATENCION_PRIORITARIA,
                TipoComplemento.SOPORTE_TECNICO_30_DIAS,
                TipoComplemento.ATENCION_DOMICILIO));
        assertTrue(resultado.getValor().getIncrementos() == 110_000);
        assertTrue(resultado.getValor().calcularCosto() == 230_000);
    }

    @Test
    void unComplementoRepetidoEsRechazado() {
        Servicio personalizado = techSolutions.personalizarServicio(
                correctivo, List.of(TipoComplemento.ATENCION_PRIORITARIA)).getValor();
        Resultado<Servicio> repetido = techSolutions.personalizarServicio(
                personalizado, List.of(TipoComplemento.ATENCION_PRIORITARIA));
        assertTrue(!repetido.isExitoso());
        assertTrue(personalizado.calcularCosto() == 155_000);
    }

    @Test
    void personalizarSinServicioOSinComplementosEsRechazado() {
        assertTrue(!techSolutions.personalizarServicio(null, List.of(TipoComplemento.ATENCION_DOMICILIO)).isExitoso());
        assertTrue(!techSolutions.personalizarServicio(correctivo, List.of()).isExitoso());
        assertTrue(techSolutions.consultarServiciosPersonalizados().isEmpty());
    }

    @Test
    void laAtencionADomicilioVuelvePresencialLaRecuperacionDeInformacion() {
        assertTrue(!recuperacion.requierePresencial());
        Servicio conDomicilio = techSolutions.personalizarServicio(
                recuperacion, List.of(TipoComplemento.ATENCION_DOMICILIO)).getValor();
        assertTrue(conDomicilio.requierePresencial());
        assertTrue(conDomicilio.getEspecialidadesPresenciales().contains(Especialidad.RECUPERACION_DATOS));
    }
}