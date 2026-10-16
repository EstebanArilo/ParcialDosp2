package uqvirutal.edu.co.parcialdosp2.model;

import uqvirutal.edu.co.parcialdosp2.servicios.LegacySupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal del modelo. Es la que usan los controladores para todo: manejar el
 * catálogo, personalizar servicios, armar paquetes, registrar solicitudes, consultar
 * técnicos y calcular costos. Solo existe una instancia (Singleton) y se pide con
 * getInstancia().
 */

public class TechSolutions {

    private static TechSolutions instancia;

    private ProveedorTecnicos proveedorTecnicos = new AdaptadorLegacySupport(new LegacySupport());

    private final List<ServicioBase> catalogo = new ArrayList<>();
    private final List<Servicio> serviciosPersonalizados = new ArrayList<>();
    private final List<PaqueteTecnologico> paquetes = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Solicitud> solicitudes = new ArrayList<>();
    private int consecutivoServicios;
    private int consecutivoPaquetes;
    private int consecutivoSolicitudes;

    private TechSolutions() {
        cargarCatalogoInicial();
        cargarClientes();
    }

    public static synchronized TechSolutions getInstancia() {
        if (instancia == null) {
            instancia = new TechSolutions();
        }
        return instancia;
    }

    /** Descarta la instancia actual (útil para pruebas). */
    public static synchronized void reiniciar() {
        instancia = null;
    }

    private void cargarCatalogoInicial() {
        for (TipoServicio tipo : TipoServicio.values()) {
            crearServicio(tipo);
        }
    }

    private void cargarClientes() {
        clientes.add(new Cliente("1094123456", "Laura Gómez Ríos", "laura.gomez@empresa.com"));
        clientes.add(new Cliente("1094987654", "Carlos Herrera Díaz", "carlos.herrera@pyme.co"));
        clientes.add(new Cliente("900123456", "Distribuciones El Cafetal S.A.S.", "contacto@elcafetal.co"));
    }

    private String codigoServicio() {
        consecutivoServicios++;
        return String.format("SRV-%04d", consecutivoServicios);
    }

    private String codigoPaquete() {
        consecutivoPaquetes++;
        return String.format("PAQ-%04d", consecutivoPaquetes);
    }

    private String codigoSolicitud() {
        consecutivoSolicitudes++;
        return String.format("SOL-%04d", consecutivoSolicitudes);
    }

    /** Permite integrar otro proveedor tecnológico sin modificar el resto del sistema. */
    public boolean setProveedorTecnicos(ProveedorTecnicos proveedorTecnicos) {
        if (proveedorTecnicos == null) {
            return false;
        }
        this.proveedorTecnicos = proveedorTecnicos;
        return true;
    }

    // ------------------------------------------------------------------
    // 1. Gestión y personalización de servicios tecnológicos
    // ------------------------------------------------------------------

    /** Crea un servicio de alguno de los tipos establecidos y lo agrega al catálogo. */
    public Resultado<ServicioBase> crearServicio(TipoServicio tipo) {
        FactoryServicios fabrica = tipo == null ? null : tipo.getFabrica();
        if (fabrica == null) {
            return Resultado.error("Debe seleccionar un tipo de servicio válido.");
        }
        ServicioBase servicio = fabrica.crear(codigoServicio());
        catalogo.add(servicio);
        return Resultado.exito(servicio);
    }

    public List<ServicioBase> consultarCatalogo() {
        return new ArrayList<>(catalogo);
    }

    /**
     * Incorpora una o varias características complementarias a un servicio.
     * Devuelve un servicio personalizado; el servicio del catálogo no se modifica.
     */
    public Resultado<Servicio> personalizarServicio(Servicio servicio, List<TipoComplemento> complementos) {
        if (servicio == null) {
            return Resultado.error("Debe seleccionar un servicio.");
        }
        if (complementos == null || complementos.isEmpty()) {
            return Resultado.error("Debe seleccionar al menos una característica complementaria.");
        }
        Servicio personalizado = servicio;
        for (TipoComplemento complemento : complementos) {
            if (complemento == null) {
                return Resultado.error("Característica complementaria no válida.");
            }
            if (Complemento.complementosDe(personalizado).contains(complemento)) {
                return Resultado.error(
                        "El complemento '" + complemento.getNombre() + "' ya fue aplicado a este servicio.");
            }
            personalizado = complemento.aplicarA(personalizado);
        }
        serviciosPersonalizados.add(personalizado);
        return Resultado.exito(personalizado);
    }

    public List<Servicio> consultarServiciosPersonalizados() {
        return new ArrayList<>(serviciosPersonalizados);
    }

    // ------------------------------------------------------------------
    // 2. Configuración de paquetes tecnológicos
    // ------------------------------------------------------------------

    public Resultado<PaqueteTecnologico> crearPaquete(String nombre, String descripcion) {
        if (nombre == null || nombre.isBlank()) {
            return Resultado.error("El nombre del paquete es obligatorio.");
        }
        PaqueteTecnologico paquete = new PaqueteTecnologico(codigoPaquete(), nombre.trim(), descripcion);
        paquetes.add(paquete);
        return Resultado.exito(paquete);
    }

    /** Agrega un servicio, servicio personalizado u otro paquete (valida que no haya ciclos). */
    public Resultado<Void> agregarComponente(PaqueteTecnologico paquete, Servicio componente) {
        if (paquete == null) {
            return Resultado.error("Debe seleccionar un paquete.");
        }
        return paquete.agregarComponente(componente);
    }

    public List<Servicio> consultarComponentes(PaqueteTecnologico paquete) {
        return paquete.getComponentes();
    }

    public List<PaqueteTecnologico> consultarPaquetes() {
        return new ArrayList<>(paquetes);
    }

    // ------------------------------------------------------------------
    // 3. Registro de solicitudes de servicios
    // ------------------------------------------------------------------

    public List<Cliente> consultarClientes() {
        return new ArrayList<>(clientes);
    }

    // Devuelve null si no existe un cliente con ese documento
    public Cliente buscarCliente(String documento) {
        for (Cliente cliente : clientes) {
            if (cliente.getDocumento().equals(documento)) {
                return cliente;
            }
        }
        return null;
    }

    /** Inicia una solicitud para un cliente previamente registrado, con su código único. */
    public Resultado<Solicitud.Builder> nuevaSolicitud(String documentoCliente) {
        Cliente cliente = buscarCliente(documentoCliente);
        if (cliente == null) {
            return Resultado.error("Debe seleccionar un cliente registrado.");
        }
        return Resultado.exito(new Solicitud.Builder(codigoSolicitud(), cliente));
    }

    /** Valida la información obligatoria, construye la solicitud y la registra (queda PENDIENTE). */
    public Resultado<Solicitud> registrarSolicitud(Solicitud.Builder builder) {
        Resultado<Solicitud> resultado = builder.build();
        if (resultado.isExitoso()) {
            solicitudes.add(resultado.getValor());
        }
        return resultado;
    }

    public List<Solicitud> consultarSolicitudes() {
        return new ArrayList<>(solicitudes);
    }

    // Devuelve null si no existe una solicitud con ese código
    public Solicitud buscarSolicitud(String codigo) {
        for (Solicitud solicitud : solicitudes) {
            if (solicitud.getCodigo().equals(codigo)) {
                return solicitud;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------
    // 4. Consulta de técnicos y cálculo del costo de las solicitudes
    // ------------------------------------------------------------------

    /** Consulta la disponibilidad de un técnico por nombre de especialidad (vía el proveedor). */
    public Tecnico consultarTecnico(Especialidad especialidad) {
        return proveedorTecnicos.consultarTecnico(especialidad);
    }

    /** Técnicos que necesita la solicitud: uno por cada especialidad presencial distinta. */
    public List<Tecnico> consultarTecnicosRequeridos(Solicitud solicitud) {
        List<Tecnico> tecnicos = new ArrayList<>();
        for (Especialidad especialidad : solicitud.getEspecialidadesPresenciales()) {
            tecnicos.add(consultarTecnico(especialidad));
        }
        return tecnicos;
    }

    /** Verdadero si no requiere atención presencial o si hay técnico para cada especialidad. */
    public boolean hayTecnicosDisponibles(Solicitud solicitud) {
        return consultarTecnicosRequeridos(solicitud).stream().allMatch(Tecnico::isDisponible);
    }

    /**
     * Costo total = servicios + paquetes + complementos + desplazamiento (si aplica).
     * El desplazamiento se cobra una sola vez por especialidad: sin cargos duplicados.
     */
    public double calcularCostoTotal(Solicitud solicitud) {
        if (solicitud.getEstado() == EstadoSolicitud.CONFIRMADA) {
            return solicitud.calcularCostoTotal();
        }
        return solicitud.calcularCostoServicios() + sumarDesplazamiento(consultarTecnicosRequeridos(solicitud));
    }

    /**
     * Confirma la solicitud. Si requiere atención presencial y no hay técnicos
     * disponibles, informa la situación y no la confirma.
     */
    public Resultado<Solicitud> confirmarSolicitud(Solicitud solicitud) {
        if (solicitud == null) {
            return Resultado.error("Debe seleccionar una solicitud.");
        }
        if (solicitud.getEstado() == EstadoSolicitud.CONFIRMADA) {
            return Resultado.error("La solicitud " + solicitud.getCodigo() + " ya fue confirmada.");
        }
        List<Tecnico> tecnicos = consultarTecnicosRequeridos(solicitud);
        List<String> faltantes = new ArrayList<>();
        for (Tecnico tecnico : tecnicos) {
            if (!tecnico.isDisponible()) {
                faltantes.add(tecnico.getEspecialidad().getNombre());
            }
        }
        if (!faltantes.isEmpty()) {
            return Resultado.error("No se puede confirmar la solicitud " + solicitud.getCodigo()
                    + ": no hay técnicos disponibles para " + String.join(", ", faltantes) + ".");
        }
        solicitud.confirmar(sumarDesplazamiento(tecnicos));
        return Resultado.exito(solicitud);
    }

    private double sumarDesplazamiento(List<Tecnico> tecnicos) {
        double total = 0;
        for (Tecnico tecnico : tecnicos) {
            if (tecnico.isDisponible()) {
                total += tecnico.getCostoDesplazamiento();
            }
        }
        return total;
    }
}