package com.view;

import com.model.Devices.*;
import com.controller.UrbanMonitoringCenter;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MapController {

    @FXML
    private WebView webView;

    private WebEngine webEngine;
    private List<Device> devices;
    private Timer simulationTimer;

    public MapController(WebView webView) {
        this.webView = webView;
    }

    public class JavaConnector {
        public void updateCursorPosition(String coordinates) {
            System.out.println("Cursor position: " + coordinates); // Log to console instead
        }
    }

    public void setDeviceData(List<Device> devices) {
        this.devices = new ArrayList<>(devices);
    }

    public void initialize() {
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaConnector", new JavaConnector());
                prepareAndSendInitialData();
                startSimulation();
            }
        });

        URL url = getClass().getResource("map.html");
        if (url != null) {
            webEngine.load(url.toExternalForm());
        } else {
            System.err.println("No se pudo encontrar el fichero map.html");
        }
    }

    private void prepareAndSendInitialData() {
        new Thread(() -> {
            if (devices == null || devices.isEmpty()) return;

            Random random = new Random();
            UrbanMonitoringCenter umc = UrbanMonitoringCenter.getUrbanMonitoringCenter();

            String allDevicesJson = devices.stream()
                .map(d -> {
                    // 20% de probabilidad de que un dispositivo empiece como NO OPERATIVO
                    boolean isOperative = random.nextDouble() > 0.02;
                    String status = isOperative ? "OPERATIVO" : "NO OPERATIVO";
                    if (!isOperative) {
                        umc.issuedDevices(d);
                    }
                    return String.format(Locale.US, "{\"id\":\"%s\", \"lat\":%f, \"lon\":%f, \"popup\":\"%s\", \"status\":\"%s\"}",
                        d.getAddress(), d.getLocation().getLatitude(), d.getLocation().getLongitude(), getPopupTextForDevice(d).replace("\"", "\\\""), status);
                })
                .collect(Collectors.joining(",", "[", "]"));

            Platform.runLater(() -> {
                webEngine.executeScript(String.format("setMarkers(%s)", allDevicesJson));
            });
        }).start();
    }

    private void startSimulation() {
        simulationTimer = new Timer(true); // Usar un hilo daemon
        simulationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (devices == null || devices.isEmpty()) return;

                Random random = new Random();
                Device deviceToChange = devices.get(random.nextInt(devices.size()));

                UrbanMonitoringCenter umc = UrbanMonitoringCenter.getUrbanMonitoringCenter();
                boolean isWorking = deviceToChange.getState();

                String newStatus = isWorking ? "NO OPERATIVO" : "OPERATIVO";
                if (isWorking) {
                    umc.issuedDevices(deviceToChange);
                }

                String popupText = getPopupTextForDevice(deviceToChange);

                Platform.runLater(() -> {
                    String script = String.format(Locale.US, "updateMarkerState(\"%s\", \"%s\", \"%s\")",
                        deviceToChange.getAddress(), newStatus, popupText.replace("\"", "\\\""));
                    webEngine.executeScript(script);
                });
            }
        }, 3000, 3000); // Cambiado de 10000 a 3000
    }

    private String getPopupTextForDevice(Device device) {
        String type = "Dispositivo";
        if (device instanceof TrafficLightController) type = "Controlador de Semáforos";
        else if (device instanceof Radar) type = "Radar de Velocidad";
        else if (device instanceof ParkingLotSecurityCamera) type = "Cámara de Aparcamiento";
        else if (device instanceof SecurityCamera) type = "Cámara de Seguridad";
        return "<b>" + device.getAddress() + "</b><br>" + type;
    }

    public void shutdown() {
        if (simulationTimer != null) {
            simulationTimer.cancel();
        }
        UrbanMonitoringCenter.getUrbanMonitoringCenter().saveDevices("devices.ser");
    }
}