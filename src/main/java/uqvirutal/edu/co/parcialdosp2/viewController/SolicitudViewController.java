package uqvirutal.edu.co.parcialdosp2.viewController;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uqvirutal.edu.co.parcialdosp2.App;
import uqvirutal.edu.co.parcialdosp2.model.*;

import java.util.List;

public class SolicitudViewController {

    private App app;

    private final ObservableList<Solicitud> listaSolicitudes = FXCollections.observableArrayList();
    private final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private final ObservableList<Servicio> listaElementosContratables = FXCollections.observableArrayList();

    @FXML private ComboBox<Cliente> cmbClientes;
    @FXML private TextField txtCodigoSolicitud;
    @FXML private TextField txtCedulaCliente;
    @FXML private TextField txtNombreCliente;
    @FXML private TextField txtCorreoCliente;

    @FXML private ComboBox<Servicio> cmbServicios;
    @FXML private ComboBox<PrioridadSolicitud> cmbPrioridad;

    @FXML private TableView<Solicitud> tblSolicitudes;
    @FXML private TableColumn<Solicitud, String> colCodigo;
    @FXML private TableColumn<Solicitud, String> colCliente;
    @FXML private TableColumn<Solicitud, EstadoSolicitud> colServicio; // Muestra el estado
    @FXML private TableColumn<Solicitud, PrioridadSolicitud> colPrioridad;

    private Solicitud.Builder builderActual;

    @FXML
    void initialize() {
        initDataBinding();
        cargarDatosIniciales();
        listenerClienteSelection();
    }

    private void initDataBinding() {
        if (colCodigo != null) {
            colCodigo.setCellValueFactory(data -> new SimpleStringProperty(
                    data.getValue() != null ? data.getValue().getCodigo() : ""
            ));
        }
        if (colCliente != null) {
            colCliente.setCellValueFactory(data -> new SimpleStringProperty(
                    data.getValue() != null && data.getValue().getCliente() != null
                            ? data.getValue().getCliente().getNombreCompleto() : ""
            ));
        }
        if (colServicio != null) {
            colServicio.setCellValueFactory(data -> new SimpleObjectProperty<>(
                    data.getValue() != null ? data.getValue().getEstado() : null
            ));
        }
        if (colPrioridad != null) {
            colPrioridad.setCellValueFactory(data -> new SimpleObjectProperty<>(
                    data.getValue() != null ? data.getValue().getPrioridad() : null
            ));
        }

        if (tblSolicitudes != null) {
            tblSolicitudes.setItems(listaSolicitudes);
        }
    }

    private void cargarDatosIniciales() {
        TechSolutions ts = TechSolutions.getInstancia();

        List<Cliente> clientes = ts.consultarClientes();
        if (clientes != null) {
            listaClientes.setAll(clientes);
            if (cmbClientes != null) {
                cmbClientes.setItems(listaClientes);
            }
        }

        ObservableList<Servicio> elementosContratables = FXCollections.observableArrayList();

        if (ts.consultarCatalogo() != null) {
            elementosContratables.addAll(ts.consultarCatalogo());
        }

        if (ts.consultarPaquetes() != null) {
            elementosContratables.addAll(ts.consultarPaquetes());
        }

        if (cmbServicios != null) {
            listaElementosContratables.setAll(elementosContratables);
            cmbServicios.setItems(listaElementosContratables);

            cmbServicios.setConverter(new javafx.util.StringConverter<Servicio>() {
                @Override
                public String toString(Servicio servicio) {
                    if (servicio == null) {
                        return "Seleccionar Servicio o Paquete";
                    }
                    return servicio.getNombre() + " (Costo: $" + String.format("%,.0f", servicio.calcularCosto()) + ")";
                }

                @Override
                public Servicio fromString(String string) {
                    return null;
                }
            });
        }

        if (cmbPrioridad != null) {
            cmbPrioridad.setItems(FXCollections.observableArrayList(PrioridadSolicitud.values()));
        }

        List<Solicitud> solicitudes = ts.consultarSolicitudes();
        if (solicitudes != null) {
            listaSolicitudes.setAll(solicitudes);
        }
    }

    private void listenerClienteSelection() {
        if (cmbClientes != null) {
            cmbClientes.getSelectionModel().selectedItemProperty().addListener((obs, viejo, clienteSeleccionado) -> {
                if (clienteSeleccionado != null) {
                    if (txtCedulaCliente != null) txtCedulaCliente.setText(clienteSeleccionado.getDocumento());
                    if (txtNombreCliente != null) txtNombreCliente.setText(clienteSeleccionado.getNombreCompleto());
                    if (txtCorreoCliente != null) txtCorreoCliente.setText(clienteSeleccionado.getCorreo());

                    Resultado<Solicitud.Builder> resultadoBuilder = TechSolutions.getInstancia().nuevaSolicitud(clienteSeleccionado.getDocumento());
                    if (resultadoBuilder.isExitoso()) {
                        builderActual = resultadoBuilder.getValor();
                    }
                }
            });
        }
    }

    @FXML
    void onAgregarElementoASolicitud() {
        if (builderActual == null) {
            mostrarAlerta("Advertencia", "Debe seleccionar un cliente del desplegable primero.");
            return;
        }

        Servicio servicioSeleccionado = cmbServicios.getSelectionModel().getSelectedItem();
        if (servicioSeleccionado == null) {
            mostrarAlerta("Advertencia", "Debe seleccionar un servicio o paquete para agregar.");
            return;
        }

        builderActual.agregarServicio(servicioSeleccionado);
        mostrarAlerta("Éxito", "Elemento '" + servicioSeleccionado.getNombre() + "' agregado a la solicitud.");
    }

    @FXML
    void onRegistrarSolicitud() {
        if (builderActual == null) {
            mostrarAlerta("Advertencia", "Debe seleccionar un cliente y agregar al menos un servicio.");
            return;
        }

        PrioridadSolicitud prioridadSeleccionada = cmbPrioridad.getSelectionModel().getSelectedItem();
        if (prioridadSeleccionada != null) {
            builderActual.prioridad(prioridadSeleccionada);
        }

        Resultado<Solicitud> resultado = TechSolutions.getInstancia().registrarSolicitud(builderActual);

        if (resultado.isExitoso()) {
            Solicitud nuevaSol = resultado.getValor();
            listaSolicitudes.add(nuevaSol);
            mostrarAlerta("Éxito", "¡Solicitud registrada correctamente! Código: " + nuevaSol.getCodigo());

            builderActual = null;
            if (cmbClientes != null) cmbClientes.getSelectionModel().clearSelection();
            if (txtCedulaCliente != null) txtCedulaCliente.clear();
            if (txtNombreCliente != null) txtNombreCliente.clear();
            if (txtCorreoCliente != null) txtCorreoCliente.clear();
            if (cmbServicios != null) cmbServicios.getSelectionModel().clearSelection();
            if (cmbPrioridad != null) cmbPrioridad.getSelectionModel().clearSelection();

        } else {
            mostrarAlerta("Error de Validación", resultado.getMensaje());
        }
    }

    public void setApp(App app) {

        this.app = app;

    }
    @FXML
    void onVolver() {
        if (app != null) {
            app.openPrimary();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}