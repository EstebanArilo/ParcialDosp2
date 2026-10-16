package uqvirutal.edu.co.parcialdosp2.viewController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uqvirutal.edu.co.parcialdosp2.App;
import uqvirutal.edu.co.parcialdosp2.model.*;

import java.util.List;

public class TecnicoViewController {

    private App app;

    private final ObservableList<Solicitud> listaSolicitudes = FXCollections.observableArrayList();
    private final ObservableList<Tecnico> listaTecnicos = FXCollections.observableArrayList();

    @FXML private ComboBox<Solicitud> cmbSolicitudesConsulta;
    @FXML private Label lblCostoServicios;
    @FXML private Label lblCostoDesplazamiento;
    @FXML private Label lblCostoTotal;

    @FXML private TableView<Tecnico> tblTecnicos;
    @FXML private TableColumn<Tecnico, String> colNombreTecnico;
    @FXML private TableColumn<Tecnico, String> colCedulaTecnico;
    @FXML private TableColumn<Tecnico, String> colEspecialidadTecnico;

    @FXML
    void initialize() {
        initDataBinding();
        cargarSolicitudes();
        listenerSolicitudSelection();
    }

    private void initDataBinding() {
        if (colNombreTecnico != null) {
            colNombreTecnico.setCellValueFactory(data -> new SimpleStringProperty(
                    data.getValue() != null ? data.getValue().getId() : ""
            ));
        }
        if (colCedulaTecnico != null) {
            colCedulaTecnico.setCellValueFactory(data -> new SimpleStringProperty(
                    data.getValue() != null && data.getValue().getDisponibilidad() != null
                            ? data.getValue().getDisponibilidad().toString() : ""
            ));
        }
        if (colEspecialidadTecnico != null) {
            colEspecialidadTecnico.setCellValueFactory(data -> new SimpleStringProperty(
                    data.getValue() != null && data.getValue().getEspecialidad() != null
                            ? data.getValue().getEspecialidad().toString() : ""
            ));
        }

        if (tblTecnicos != null) {
            tblTecnicos.setItems(listaTecnicos);
        }
    }

    public void cargarSolicitudes() {
        TechSolutions ts = TechSolutions.getInstancia();
        List<Solicitud> solicitudes = ts.consultarSolicitudes();

        if (solicitudes != null) {
            listaSolicitudes.setAll(solicitudes);
            if (cmbSolicitudesConsulta != null) {
                cmbSolicitudesConsulta.setItems(listaSolicitudes);

                cmbSolicitudesConsulta.setCellFactory(param -> new ListCell<Solicitud>() {
                    @Override
                    protected void updateItem(Solicitud item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getCodigo() + " - " + (item.getCliente() != null ? item.getCliente().getNombreCompleto() : "Sin cliente") + " (" + item.getEstado() + ")");
                        }
                    }
                });

                cmbSolicitudesConsulta.setButtonCell(new ListCell<Solicitud>() {
                    @Override
                    protected void updateItem(Solicitud item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("Seleccionar Solicitud");
                        } else {
                            setText(item.getCodigo() + " - " + (item.getCliente() != null ? item.getCliente().getNombreCompleto() : "Sin cliente") + " (" + item.getEstado() + ")");
                        }
                    }
                });
            }
        }
    }

    private void listenerSolicitudSelection() {
        if (cmbSolicitudesConsulta != null) {
            cmbSolicitudesConsulta.getSelectionModel().selectedItemProperty().addListener((obs, viejo, solicitudSeleccionada) -> {
                if (solicitudSeleccionada != null) {
                    TechSolutions ts = TechSolutions.getInstancia();

                    double costoServicios = solicitudSeleccionada.calcularCostoServicios();
                    double costoTotal = ts.calcularCostoTotal(solicitudSeleccionada);
                    double desplazamiento = costoTotal - costoServicios;

                    if (lblCostoServicios != null) lblCostoServicios.setText(String.format("%.2f", costoServicios));
                    if (lblCostoDesplazamiento != null) lblCostoDesplazamiento.setText(String.format("%.2f", desplazamiento));
                    if (lblCostoTotal != null) lblCostoTotal.setText(String.format("%.2f", costoTotal));

                    List<Tecnico> tecnicosRequeridos = ts.consultarTecnicosRequeridos(solicitudSeleccionada);
                    if (tecnicosRequeridos != null) {
                        listaTecnicos.setAll(tecnicosRequeridos);
                    }
                } else {
                    listaTecnicos.clear();
                    if (lblCostoServicios != null) lblCostoServicios.setText("0.0");
                    if (lblCostoDesplazamiento != null) lblCostoDesplazamiento.setText("0.0");
                    if (lblCostoTotal != null) lblCostoTotal.setText("0.0");
                }
            });
        }
    }

    @FXML
    void onConfirmarSolicitud() {
        if (cmbSolicitudesConsulta == null) return;
        Solicitud seleccionada = cmbSolicitudesConsulta.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Advertencia", "Debe seleccionar una solicitud en el desplegable para confirmarla.");
            return;
        }

        Resultado<Solicitud> resultado = TechSolutions.getInstancia().confirmarSolicitud(seleccionada);
        if (resultado.isExitoso()) {
            mostrarAlerta("Éxito", "¡Solicitud " + seleccionada.getCodigo() + " confirmada correctamente con técnicos disponibles!");
            cargarSolicitudes();
        } else {
            mostrarAlerta("Restricción de Disponibilidad", resultado.getMensaje());
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