package uqvirutal.edu.co.parcialdosp2.viewController;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import uqvirutal.edu.co.parcialdosp2.App;
import uqvirutal.edu.co.parcialdosp2.model.Servicio;
import uqvirutal.edu.co.parcialdosp2.model.TechSolutions;

public class ServicioViewController {

    private App app;

    private final ObservableList<Servicio> listaServicios = FXCollections.observableArrayList();
    private Servicio servicioSeleccionado;

    @FXML private TableView<Servicio> tblServicios;
    @FXML private TableColumn<Servicio, String> colNombre;
    @FXML private TableColumn<Servicio, String> colDescripcion;
    @FXML private TableColumn<Servicio, Number> colCosto;

    @FXML private TextField txtNombreComplemento;
    @FXML private TextField txtDescripcionComplemento;
    @FXML private TextField txtCostoComplemento;

    @FXML private Label lblCostoBase;
    @FXML private Label lblIncremento;
    @FXML private Label lblCostoFinal;

    @FXML
    void initialize() {
        initDataBinding();
        listenerSelection();
        cargarServicios();
    }

    private void initDataBinding() {
        if (colNombre != null) {
            colNombre.setCellValueFactory(data -> {
                String nombre = "";
                try {
                    if (data.getValue() != null) {
                        nombre = data.getValue().getNombre();
                    }
                } catch (Exception e) {
                    nombre = "Servicio sin tipo";     }
                return new javafx.beans.property.SimpleStringProperty(nombre != null ? nombre : "");
            });
        }

        if (colDescripcion != null) {
            colDescripcion.setCellValueFactory(data -> {
                String desc = "";
                try {
                    if (data.getValue() != null) {
                        desc = data.getValue().getDescripcion();
                    }
                } catch (Exception e) {
                    desc = "";
                }
                return new javafx.beans.property.SimpleStringProperty(desc != null ? desc : "");
            });
        }

        if (colCosto != null) {
            colCosto.setCellValueFactory(data -> {
                double costo = 0.0;
                try {
                    if (data.getValue() != null) {
                        costo = data.getValue().calcularCosto();
                    }
                } catch (Exception e) {
                    costo = 0.0;
                }
                return new javafx.beans.property.SimpleObjectProperty<>(costo);
            });
        }
    }

    private void cargarServicios() {
        System.out.println("🔍 ¿La tabla tblServicios es nula?: " + (tblServicios == null ? "SÍ (¡Aquí está el problema!)" : "NO (Inyectada correctamente)"));

        if (tblServicios != null) {
            listaServicios.clear();
            var catalogo = TechSolutions.getInstancia().consultarCatalogo();
            if (catalogo != null) {
                listaServicios.addAll(catalogo);
            }
            tblServicios.setItems(listaServicios);
            System.out.println("🔍 Elementos pintados exitosamente en la TableView: " + tblServicios.getItems().size());
        }
    }

    private void listenerSelection() {
        if (tblServicios != null) {
            tblServicios.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nuevo) -> {
                servicioSeleccionado = nuevo;
                if (servicioSeleccionado != null) {
                    lblCostoBase.setText(String.valueOf(servicioSeleccionado.calcularCosto()));
                    lblCostoFinal.setText(lblCostoBase.getText());
                    lblIncremento.setText("0.0");
                }
            });
        }
    }

    @FXML
    void onAgregarComplemento() {
        if (servicioSeleccionado == null) {
            mostrarAlerta("Advertencia", "Debe seleccionar un servicio del catálogo.");
            return;
        }

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