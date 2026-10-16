package uqvirutal.edu.co.parcialdosp2.viewController;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uqvirutal.edu.co.parcialdosp2.App;
import uqvirutal.edu.co.parcialdosp2.controller.PaqueteController;
import uqvirutal.edu.co.parcialdosp2.model.PaqueteTecnologico;
import uqvirutal.edu.co.parcialdosp2.model.Servicio;

public class PaqueteViewController {

    private App app;
    private PaqueteController paqueteController;

    private PaqueteTecnologico paqueteActual;
    private final ObservableList<Servicio> listaServiciosPaquete = FXCollections.observableArrayList();

    @FXML private TextField txtCodigoPaquete;
    @FXML private TextField txtNombrePaquete;
    @FXML private TextField txtDescripcionPaquete;

    @FXML private TableView<Servicio> tblServiciosPaquete;
    @FXML private TableColumn<Servicio, String> colNombreServicio;
    @FXML private TableColumn<Servicio, String> colDescripcionServicio;
    @FXML private TableColumn<Servicio, Number> colCostoServicio;

    @FXML private Label lblCostoTotalPaquete;

    @FXML
    void initialize() {
        paqueteController = new PaqueteController();
        initDataBinding();
    }

    private void initDataBinding() {
        colNombreServicio.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colDescripcionServicio.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescripcion()));
        colCostoServicio.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().calcularCosto()));
    }

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    void onCrearPaquete() {
        String codigo = txtCodigoPaquete.getText();
        String nombre = txtNombrePaquete.getText();
        String descripcion = txtDescripcionPaquete.getText();

        if (codigo.isEmpty() || nombre.isEmpty() || descripcion.isEmpty()) {
            mostrarAlerta("Advertencia", "Debe ingresar el código, nombre y la descripción del paquete.");
            return;
        }

        paqueteActual = paqueteController.crearPaquete(codigo, nombre, descripcion);
        listaServiciosPaquete.clear();
        tblServiciosPaquete.setItems(listaServiciosPaquete);
        lblCostoTotalPaquete.setText("0.0");

        mostrarAlerta("Éxito", "Paquete tecnológico creado correctamente.");
    }

    @FXML
    void onAgregarServicioAPaquete() {
        if (paqueteActual == null) {
            mostrarAlerta("Advertencia", "Primero debe crear o inicializar un paquete tecnológico.");
            return;
        }
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