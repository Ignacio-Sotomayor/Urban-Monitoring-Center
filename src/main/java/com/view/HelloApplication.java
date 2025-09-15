package com.view;

import com.model.UrbanMonitoringCenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private HelloController controller;

    @Override
    public void start(Stage stage) throws IOException {
        // 1. Inicializa los datos del UrbanMonitoringCenter
        UrbanMonitoringCenter.Initialize();

        // 2. Carga la vista FXML
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        controller = fxmlLoader.getController();

        // 3. Obtiene los dispositivos y se los pasa al controlador
        UrbanMonitoringCenter umc = UrbanMonitoringCenter.getUrbanMonitoringCenter();
        controller.setDeviceData(umc.getWorkingDevices());

        // 4. Muestra la aplicación
        stage.setTitle("Urban Monitoring Center");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        if (controller != null) {
            controller.shutdown();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}
