package uqvirutal.edu.co.parcialdosp2.viewController;

import javafx.fxml.FXML;
import uqvirutal.edu.co.parcialdosp2.App;

public class PrimaryViewController {

    private App app;

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    void onAbrirServicios() {
        app.openServiciosView();
    }

    @FXML
    void onAbrirPaquetes() {
        app.openPaquetesView();
    }

    @FXML
    void onAbrirSolicitudes() {
        app.openSolicitudesView();
    }

    @FXML
    void onAbrirTecnicosCostos() {
        app.openTecnicosView();
    }

    @FXML
    void onSalir() {
        System.exit(0);
    }
}