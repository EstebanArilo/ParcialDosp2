package uqvirutal.edu.co.parcialdosp2.viewController;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import uqvirutal.edu.co.parcialdosp2.App;
import uqvirutal.edu.co.parcialdosp2.model.Servicio;
import uqvirutal.edu.co.parcialdosp2.model.TechSolutions;
import uqvirutal.edu.co.parcialdosp2.model.TipoComplemento;

public class ServicioViewController {

    private App app;

    private final ObservableList<Servicio> listaServicios = FXCollections.observableArrayList();
    private Servicio servicioSeleccionado;

    @FXML private TableView<Servicio> tblServicios;
    @FXML private TableColumn<Servicio, String> colNombre;
    @FXML private TableColumn<Servicio, String> colDescripcion;
    @FXML private TableColumn<Servicio, Number> colCosto;

    @FXML private ComboBox<TipoComplemento> cmbTipoComplemento;
    @FXML private TextField txtDescripcionComplemento;
    @FXML private TextField txtCostoComplemento;

    @FXML private Label lblCostoBase;
    @FXML private Label lblIncremento;
    @FXML private Label lblCostoFinal;


    @FXML
    void initialize() {
        initDataBinding();
        initComplementosBinding();
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
                    nombre = "Servicio sin tipo";
                }
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

    private void initComplementosBinding() {
        if (cmbTipoComplemento != null) {
            cmbTipoComplemento.setItems(FXCollections.observableArrayList(TipoComplemento.values()));

            cmbTipoComplemento.setConverter(new StringConverter<TipoComplemento>() {
                @Override
                public String toString(TipoComplemento tipo) {
                    return tipo != null ? tipo.getNombre() : "Seleccionar Complemento";
                }

                @Override
                public TipoComplemento fromString(String string) {
                    return null;
                }
            });

            cmbTipoComplemento.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
                if (nuevo != null) {
                    if (txtDescripcionComplemento != null) {
                        txtDescripcionComplemento.setText("Complemento: " + nuevo.getNombre());
                    }
                    if (txtCostoComplemento != null) {
                        txtCostoComplemento.setText(String.valueOf(nuevo.getIncremento()));
                    }

                    if (servicioSeleccionado != null) {
                        double costoBase = servicioSeleccionado.calcularCosto();
                        double incremento = nuevo.getIncremento();
                        lblCostoBase.setText(String.valueOf(costoBase));
                        lblIncremento.setText(String.valueOf(incremento));
                        lblCostoFinal.setText(String.valueOf(costoBase + incremento));
                    }
                } else {
                    if (txtDescripcionComplemento != null) txtDescripcionComplemento.clear();
                    if (txtCostoComplemento != null) txtCostoComplemento.clear();
                    if (servicioSeleccionado != null) {
                        double costoBase = servicioSeleccionado.calcularCosto();
                        lblCostoBase.setText(String.valueOf(costoBase));
                        lblIncremento.setText("0.0");
                        lblCostoFinal.setText(String.valueOf(costoBase));
                    }
                }
            });
        }
    }

    private void cargarServicios() {
        if (tblServicios != null) {
            listaServicios.clear();
            var catalogo = TechSolutions.getInstancia().consultarCatalogo();
            if (catalogo != null) {
                listaServicios.addAll(catalogo);
            }
            tblServicios.setItems(listaServicios);
        }
    }

    private void listenerSelection() {
        if (tblServicios != null) {
            tblServicios.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nuevo) -> {
                servicioSeleccionado = nuevo;
                if (servicioSeleccionado != null) {
                    double costoBaseVal = servicioSeleccionado.calcularCosto();
                    lblCostoBase.setText(String.valueOf(costoBaseVal));

                    TipoComplemento seleccionado = cmbTipoComplemento.getSelectionModel().getSelectedItem();
                    double incremento = (seleccionado != null) ? seleccionado.getIncremento() : 0.0;
                    lblIncremento.setText(String.valueOf(incremento));
                    lblCostoFinal.setText(String.valueOf(costoBaseVal + incremento));
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

        TipoComplemento tipoSeleccionado = cmbTipoComplemento.getSelectionModel().getSelectedItem();
        if (tipoSeleccionado == null) {
            mostrarAlerta("Advertencia", "Debe seleccionar un complemento del desplegable.");
            return;
        }

        try {
            Servicio servicioDecorado = tipoSeleccionado.aplicarA(servicioSeleccionado);

            int index = listaServicios.indexOf(servicioSeleccionado);
            if (index >= 0) {
                listaServicios.set(index, servicioDecorado);
                servicioSeleccionado = servicioDecorado;
            }

            tblServicios.refresh();

            double costoTotalActual = servicioSeleccionado.calcularCosto();
            double incremento = tipoSeleccionado.getIncremento();
            double costoBaseOriginal = costoTotalActual - incremento;

            lblCostoBase.setText(String.valueOf(costoBaseOriginal));
            lblIncremento.setText(String.valueOf(incremento));
            lblCostoFinal.setText(String.valueOf(costoTotalActual));

            mostrarAlerta("Éxito", "¡Complemento '" + tipoSeleccionado.getNombre() + "' aplicado correctamente al servicio!");
            cmbTipoComplemento.getSelectionModel().clearSelection();

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo aplicar el complemento: " + e.getMessage());
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