package uqvirutal.edu.co.parcialdosp2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uqvirutal.edu.co.parcialdosp2.viewController.PrimaryViewController;

import java.io.IOException;

public class App extends Application {

    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("TechSolutions - Sistema de Gestión");
        openPrimary();
    }

    // Método para volver al menú principal
    public void openPrimary() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml")); // Ajusta la ruta si está en una subcarpeta (ej: "view/primary.fxml")
            Parent root = loader.load();

            PrimaryViewController controller = loader.getController();
            controller.setApp(this);

            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Métodos para abrir cada uno de los módulos de la aplicación
    public void openServiciosView() {
        cargarVista("servicioView2.fxml", "Gestión de Servicios");
    }

    public void openPaquetesView() {
        cargarVista("paqueteView.fxml", "Configuración de Paquetes");
    }

    public void openSolicitudesView() {
        cargarVista("solicitudView.fxml", "Registro de Solicitudes");
    }

    public void openTecnicosView() {
        cargarVista("tecnicoView.fxml", "Consulta de Técnicos y Costos");
    }

    private void fxLoader(String fxmlFile) {

    }

    private void cargarVista(String fxmlFileName, String tituloVentana) {
        try {

            java.net.URL fxmlUrl = getClass().getResource(fxmlFileName);

            if (fxmlUrl == null) {

                fxmlUrl = getClass().getResource("/uqvirutal/edu/co/parcialdosp2/" + fxmlFileName);
            }

            if (fxmlUrl == null) {
                System.err.println("No se pudo encontrar el archivo FXML: " + fxmlFileName);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller != null) {
                try {
                    controller.getClass().getMethod("setApp", App.class).invoke(controller, this);
                } catch (Exception ignored) {
                }
            }

            primaryStage.setTitle("TechSolutions - " + tituloVentana);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}