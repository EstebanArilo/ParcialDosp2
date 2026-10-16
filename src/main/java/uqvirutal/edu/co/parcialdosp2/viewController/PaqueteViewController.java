package uqvirutal.edu.co.parcialdosp2.viewController;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uqvirutal.edu.co.parcialdosp2.App;
import uqvirutal.edu.co.parcialdosp2.controller.PaqueteController;
import uqvirutal.edu.co.parcialdosp2.model.PaqueteTecnologico;
import uqvirutal.edu.co.parcialdosp2.model.Resultado;
import uqvirutal.edu.co.parcialdosp2.model.Servicio;
import uqvirutal.edu.co.parcialdosp2.model.TechSolutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaqueteViewController {

    private App app;
    private PaqueteController paqueteController;

    private PaqueteTecnologico paqueteActual;
    private final ObservableList<PaqueteTecnologico> listaPaquetes = FXCollections.observableArrayList();
    private final ObservableList<Servicio> listaServiciosPaquete = FXCollections.observableArrayList();

    @FXML private TableView<PaqueteTecnologico> tblPaquetes;
    @FXML private TableColumn<PaqueteTecnologico, String> colNombrePaquete;
    @FXML private TableColumn<PaqueteTecnologico, String> colDescripcionPaquete;
    @FXML private TableColumn<PaqueteTecnologico, Number> colCostoPaquete;

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
        listenerSelection();
    }

    private void initDataBinding() {
        if (colNombrePaquete != null) {
            colNombrePaquete.setCellValueFactory(data -> new SimpleStringProperty(
                    data.getValue() != null ? data.getValue().getNombre() : ""
            ));
        }

        if (colDescripcionPaquete != null) {
            colDescripcionPaquete.setCellValueFactory(data -> new SimpleStringProperty(
                    data.getValue() != null ? data.getValue().getDescripcion() : ""
            ));
        }

        if (colCostoPaquete != null) {
            colCostoPaquete.setCellValueFactory(data -> new SimpleObjectProperty<>(
                    data.getValue() != null ? data.getValue().calcularCosto() : 0.0
            ));
        }

        if (tblPaquetes != null) {
            tblPaquetes.setItems(listaPaquetes);
        }

        if (colNombreServicio != null) {
            colNombreServicio.setCellValueFactory(data -> new SimpleStringProperty(
                    data.getValue() != null ? data.getValue().getNombre() : ""
            ));
        }

        if (colDescripcionServicio != null) {
            colDescripcionServicio.setCellValueFactory(data -> {
                String desc = "";
                try {
                    if (data.getValue() != null) {
                        desc = data.getValue().getDescripcion();
                    }
                } catch (Exception e) {
                    desc = "";
                }
                return new SimpleStringProperty(desc != null ? desc : "");
            });
        }

        if (colCostoServicio != null) {
            colCostoServicio.setCellValueFactory(data -> {
                double costo = 0.0;
                try {
                    if (data.getValue() != null) {
                        costo = data.getValue().calcularCosto();
                    }
                } catch (Exception e) {
                    costo = 0.0;
                }
                return new SimpleObjectProperty<>(costo);
            });
        }

        if (tblServiciosPaquete != null) {
            tblServiciosPaquete.setItems(listaServiciosPaquete);
        }
    }

    private void listenerSelection() {
        if (tblPaquetes != null) {
            tblPaquetes.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
                paqueteActual = nuevo;
                actualizarVistaPaqueteSeleccionado();
            });
        }
    }

    private void actualizarVistaPaqueteSeleccionado() {
        if (paqueteActual != null) {
            listaServiciosPaquete.setAll(paqueteActual.getComponentes());
            if (lblCostoTotalPaquete != null) {
                lblCostoTotalPaquete.setText(String.format("%.2f", paqueteActual.calcularCosto()));
            }
        } else {
            listaServiciosPaquete.clear();
            if (lblCostoTotalPaquete != null) {
                lblCostoTotalPaquete.setText("0.0");
            }
        }
        tblPaquetes.refresh();
    }


    @FXML
    void onCrearPaquete() {
        if (txtNombrePaquete == null) return;
        String nombre = txtNombrePaquete.getText();
        String descripcion = txtDescripcionPaquete.getText();

        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarAlerta("Advertencia", "El nombre del paquete es obligatorio.");
            return;
        }

        Resultado<PaqueteTecnologico> resultado = TechSolutions.getInstancia().crearPaquete(nombre, descripcion);

        if (resultado.isExitoso()) {
            PaqueteTecnologico nuevoPaquete = resultado.getValor();

            listaPaquetes.setAll(TechSolutions.getInstancia().consultarPaquetes());

            paqueteActual = nuevoPaquete;
            tblPaquetes.getSelectionModel().select(nuevoPaquete);
            actualizarVistaPaqueteSeleccionado();

            mostrarAlerta("Éxito", "¡Paquete tecnológico creado y registrado correctamente!");
            txtNombrePaquete.clear();
            txtDescripcionPaquete.clear();

        } else {
            mostrarAlerta("Error", "No se pudo crear el paquete: " + resultado.getMensaje());
        }
    }

    @FXML
    void onAgregarServicioAPaquete() {
        if (paqueteActual == null) {
            mostrarAlerta("Advertencia", "Primero debe crear o seleccionar un paquete tecnológico en la tabla.");
            return;
        }

        List<Servicio> opcionesDisponibles = new ArrayList<>();

        var catalogo = TechSolutions.getInstancia().consultarCatalogo();
        if (catalogo != null) {
            opcionesDisponibles.addAll(catalogo);
        }

        for (PaqueteTecnologico p : listaPaquetes) {
            if (!p.equals(paqueteActual)) {
                opcionesDisponibles.add(p);
            }
        }

        if (opcionesDisponibles.isEmpty()) {
            mostrarAlerta("Información", "No hay servicios ni otros paquetes disponibles para agregar.");
            return;
        }

        ChoiceDialog<Servicio> dialog = new ChoiceDialog<>(opcionesDisponibles.get(0), opcionesDisponibles);
        dialog.setTitle("Configuración de Paquete (Composite)");
        dialog.setHeaderText("Añadir componente a: " + paqueteActual.getNombre());
        dialog.setContentText("Seleccione el servicio o paquete:");

        Optional<Servicio> resultadoDialogo = dialog.showAndWait();
        if (resultadoDialogo.isPresent()) {
            Servicio componenteSeleccionado = resultadoDialogo.get();

            Resultado<Void> resultado = paqueteActual.agregarComponente(componenteSeleccionado);

            if (resultado.isExitoso()) {
                actualizarVistaPaqueteSeleccionado();
                mostrarAlerta("Éxito", "Componente incorporado correctamente al paquete.");
            } else {
                mostrarAlerta("Restricción del Parcial (Anti-ciclos)", resultado.getMensaje());
            }
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
