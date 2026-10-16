package uqvirutal.edu.co.parcialdosp2.viewController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uqvirutal.edu.co.parcialdosp2.App;
import uqvirutal.edu.co.parcialdosp2.controller.TecnicoController;
import uqvirutal.edu.co.parcialdosp2.model.Especialidad;
import uqvirutal.edu.co.parcialdosp2.model.Solicitud;
import uqvirutal.edu.co.parcialdosp2.model.Tecnico;

public class TecnicoViewController {

    private App app;
    private TecnicoController tecnicoController;

    @FXML private ComboBox<Solicitud> cmbSolicitudesConsulta;
    @FXML private ComboBox<Especialidad> cmbEspecialidades;

    @FXML private Label lblNombreTecnicoEncontrado;
    @FXML private Label lblCedulaTecnicoEncontrado;
    @FXML private Label lblCostoServicios;
    @FXML private Label lblCostoDesplazamiento;
    @FXML private Label lblCostoTotal;

    @FXML
    void initialize() {
        tecnicoController = new TecnicoController();
    }

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    void onSeleccionarSolicitud() {
        Solicitud solicitudSeleccionada = cmbSolicitudesConsulta.getValue();
        if (solicitudSeleccionada != null) {
            lblCostoServicios.setText(String.valueOf(solicitudSeleccionada.calcularCostoServicios()));
            lblCostoDesplazamiento.setText(String.valueOf(solicitudSeleccionada.getCostoDesplazamiento()));
            lblCostoTotal.setText(String.valueOf(tecnicoController.calcularCostoTotalSolicitud(solicitudSeleccionada)));
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