package uqvirutal.edu.co.parcialdosp2.viewController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uqvirutal.edu.co.parcialdosp2.App;
import uqvirutal.edu.co.parcialdosp2.controller.SolicitudController;
import uqvirutal.edu.co.parcialdosp2.model.*;

public class SolicitudViewController {

    private App app;
    private SolicitudController solicitudController;

    private final ObservableList<Solicitud> listaSolicitudes = FXCollections.observableArrayList();

    @FXML private TextField txtCodigoSolicitud;
    @FXML private TextField txtNombreCliente;
    @FXML private TextField txtCedulaCliente;
    @FXML private TextField txtCorreoCliente;

    // Suponiendo que permites seleccionar un servicio base o paquete del catálogo
    @FXML private ComboBox<Servicio> cmbServicios;
    @FXML private ComboBox<PrioridadSolicitud> cmbPrioridad;

    @FXML private TableView<Solicitud> tblSolicitudes;
    @FXML private TableColumn<Solicitud, String> colCodigo;
    @FXML private TableColumn<Solicitud, String> colCliente;
    @FXML private TableColumn<Solicitud, String> colServicio;
    @FXML private TableColumn<Solicitud, String> colPrioridad;

    @FXML
    void initialize() {
        solicitudController = new SolicitudController();
        initDataBinding();
        cargarCombos();
    }

    private void initDataBinding() {
        colCodigo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodigo()));
        colCliente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCliente().getNombreCompleto()));
        // Mostramos el estado o una descripción general de los elementos contratados
        colServicio.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstado().toString()));
        colPrioridad.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrioridad() != null ? data.getValue().getPrioridad().toString() : "N/A"));
    }

    private void cargarCombos() {
        cmbPrioridad.setItems(FXCollections.observableArrayList(PrioridadSolicitud.values()));
        // Aquí puedes cargar los servicios o paquetes disponibles en el ComboBox si lo deseas
    }

    public void setApp(App app) {
        this.app = app;
        actualizarTabla();
    }

    @FXML
    void onRegistrarSolicitud() {
        String codigo = txtCodigoSolicitud.getText();
        String nombreCliente = txtNombreCliente.getText();
        String cedulaCliente = txtCedulaCliente.getText();
        String correoCliente = txtCorreoCliente != null ? txtCorreoCliente.getText() : "correo@ejemplo.com";

        PrioridadSolicitud prioridad = cmbPrioridad.getValue();
        Servicio servicioSeleccionado = cmbServicios.getValue();

        if (codigo.isEmpty() || nombreCliente.isEmpty() || cedulaCliente.isEmpty() || prioridad == null || servicioSeleccionado == null) {
            mostrarAlerta("Advertencia", "Por favor complete todos los campos de la solicitud y seleccione un servicio.");
            return;
        }

        // Creamos el cliente pasando los 3 argumentos que exige el constructor del modelo
        Cliente cliente = new Cliente(cedulaCliente, nombreCliente, correoCliente);

        boolean registrado = solicitudController.registrarSolicitud(
                codigo, cliente, servicioSeleccionado, prioridad
        );

        if (registrado) {
            mostrarAlerta("Éxito", "Solicitud registrada correctamente.");
            actualizarTabla();
        } else {
            mostrarAlerta("Error", "No se pudo registrar la solicitud.");
        }
    }

    private void actualizarTabla() {
        listaSolicitudes.clear();
        listaSolicitudes.addAll(solicitudController.obtenerSolicitudes());
        tblSolicitudes.setItems(listaSolicitudes);
    }

    @FXML
    void onVolver() {
        app.openPrimary();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}